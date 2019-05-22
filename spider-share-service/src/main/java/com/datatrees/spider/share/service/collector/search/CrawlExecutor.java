/*
 * Copyright © 2015 - 2019 杭州大树网络技术有限公司. All Rights Reserved
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */

package com.datatrees.spider.share.service.collector.search;

import com.datatrees.common.util.ThreadInterruptedUtil;
import com.datatrees.spider.share.domain.ErrorCode;
import com.datatrees.spider.share.domain.model.Keyword;
import com.datatrees.spider.share.domain.model.Task;
import com.datatrees.spider.share.service.KeywordService;
import com.datatrees.spider.share.service.collector.common.CollectorConstants;
import com.datatrees.spider.share.service.util.UnifiedSysTime;
import com.treefinance.crawler.framework.config.xml.search.SearchTemplateConfig;
import com.treefinance.crawler.framework.context.function.LinkNode;
import com.treefinance.crawler.framework.exception.ResultEmptyException;
import com.treefinance.crawler.framework.process.search.SearchTemplateCombine;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;

/**
 * @author <A HREF="">Cheng Wang</A>
 * @version 1.0
 * @since 2015年7月29日 下午2:21:06
 */
@Service
public class CrawlExecutor {

    private static final Logger log = LoggerFactory.getLogger(CrawlExecutor.class);

    @Resource
    private KeywordService keywordService;

    /**
     * @param searchProcessor
     * @exception ResultEmptyException
     */
    public void execute(SearchProcessor searchProcessor) throws ResultEmptyException {
        LinkQueue linkQueue = null;
        try {
            log.info("Rawdata collector start ... searchTemplate : {}", searchProcessor.getSearchTemplate());
            SearchTemplateConfig searchTemplateConfig = searchProcessor.getSearchTemplateConfig();
            linkQueue = new LinkQueue(searchTemplateConfig);
            if (!linkQueue.init()) {
                Task task = searchProcessor.getTask();
                task.setErrorCode(ErrorCode.INIT_QUEUE_FAILED_ERROR_CODE);
                log.info("{} -- The queue is empty, the system will exit. Template: {}", searchTemplateConfig.getType(), searchProcessor.getSearchTemplate());
                return;
            }

            Integer threadCount = searchTemplateConfig.getThreadCount();
            if (searchProcessor.isKeywordSearch()) {
                List<Keyword> keywordList = keywordService.queryByWebsiteType(searchProcessor.getWebsiteType());
                for (Keyword keyword : keywordList) {
                    doExecute(searchProcessor, linkQueue, keyword.getKeyword(), threadCount);
                }
            } else {
                doExecute(searchProcessor, linkQueue, null, threadCount);
            }
        } catch (Exception e) {
            this.exceptionHandle(e, "Crawler executor encountered a problem.");
        } finally {
            if (searchProcessor.getCrawlExecutorPool() != null) {
                searchProcessor.getCrawlExecutorPool().shutdownNow();
                log.info("shutdownNow crawlExecutorPool success.");
            }
            if (null != linkQueue) {
                linkQueue.closeLinkQueue();
            }
        }
    }

    private void doExecute(SearchProcessor searchProcessor, LinkQueue linkQueue, String keyword, Integer threadCount) throws ResultEmptyException {
        searchProcessor.initWithKeyword(keyword);
        String url = SearchTemplateCombine.constructSearchURL(searchProcessor.getSearchTemplate(), keyword, searchProcessor.getEncoding(), 0, true,
            searchProcessor.getProcessorContext().getVisibleScope());

        log.info("Actual search seed url: {}", url);

        LinkNode linkNode = new LinkNode(url).setDepth(0);
        this.doLoopCrawl(searchProcessor, linkQueue, linkNode, threadCount);
    }

    private void doLoopCrawl(SearchProcessor searchProcessor, LinkQueue linkQueue, LinkNode linkNode, Integer threadCount) throws ResultEmptyException {
        Task task = searchProcessor.getTask();
        log.info("Start doLoopCrawl , Task id: {}, linkNode: {}", task.getId(), linkNode);
        if (linkNode != null && StringUtils.isNotBlank(linkNode.getUrl())) {
            linkQueue.addLink(linkNode);
        }
        ExecutorService crawlExecutorPool = null;
        List<Future<Boolean>> futureList = null;
        try {
            outer:
            while (!Thread.currentThread().isInterrupted() && !ThreadInterruptedUtil.isInterrupted(Thread.currentThread())) {
                LinkedList<LinkNode> nextLink = linkQueue.fetchNewLinks(CollectorConstants.MAX_QUEUE_SIZE);
                if (CollectionUtils.isEmpty(nextLink)) {
                    if (CollectionUtils.isEmpty(futureList)) {
                        log.info("Link queue is empty,program will exit ");
                        break;
                    } else {
                        Iterator<Future<Boolean>> it = futureList.iterator();
                        while (it.hasNext()) {
                            Future<Boolean> future = it.next();
                            future.get();// sync to get result may throw exception
                            it.remove();
                        }
                    }
                }
                while (CollectionUtils.isNotEmpty(nextLink) && !ThreadInterruptedUtil.isInterrupted(Thread.currentThread())) {
                    try {
                        LinkNode link = nextLink.removeFirst();
                        // Time Out Logic
                        if (isTimeOutOfTask(searchProcessor)) {
                            task.setErrorCode(ErrorCode.TASK_TIMEOUT_ERROR_CODE);
                            log.warn("TaskWorker has been timeout. The program will exit");
                            break outer;
                        }

                        if (linkQueue.isFull()) {
                            // searchProcessor.getTask().setErrorCode(ErrorCode.QUEUE_FULL_ERROR_CODE);
                        }
                        if (threadCount != null && threadCount > 1) {
                            if (crawlExecutorPool != null) {
                                // run as muti thread pool
                                try {
                                    futureList.add(crawlExecutorPool.submit(() -> {
                                        List<LinkNode> findLinks = searchProcessor.crawlOneURL(link);
                                        if (!Thread.currentThread().isInterrupted()) {
                                            linkQueue.addLinks(findLinks);
                                        }
                                        return true;
                                    }));
                                } catch (Exception e) {
                                    if (e instanceof RejectedExecutionException) {
                                        // ignore RejectedExecutionException
                                        nextLink.addFirst(link);
                                        Thread.sleep(300);
                                    } else {
                                        log.error("submit linked cause unknown exception", e);
                                    }
                                }
                            } else {
                                /*
                                 * 1. more Efficientive for muti thread mode, using web thread to
                                 * crawl the first linknode 2. can collect urlCount in mian thread
                                 */
                                List<LinkNode> findLinks = searchProcessor.crawlOneURL(link);
                                linkQueue.addLinks(findLinks);
                                log.info("get crawlExecutorPool with threadCount: {}", threadCount);
                                crawlExecutorPool = searchProcessor.getCrawlExecutorPool(threadCount);
                                futureList = new ArrayList<>();
                            }
                        } else {
                            // Url Request
                            List<LinkNode> findLinks = searchProcessor.crawlOneURL(link);
                            linkQueue.addLinks(findLinks);
                        }

                        // may block 505
                        if (searchProcessor.isNeedEarlyQuit()) {
                            log.info("TaskWorker quit early.The program will exit");
                            break outer;
                        }

                    } catch (Exception e) {
                        this.exceptionHandle(e, "TaskWorker crawlOneURL encountered a problem .");
                    }
                }
            }
        } catch (Exception e) {
            this.exceptionHandle(e, "doLoopCrawl error ");
        }
    }

    private boolean isTimeOutOfTask(SearchProcessor searchProcessor) {
        long currentTime = UnifiedSysTime.INSTANCE.getSystemTime().getTime();
        boolean timeout = searchProcessor.isTimeout(currentTime);

        if (timeout) {
            log.debug("Crawl task is time out! taskId : {}, startTime: {}, now: {}", searchProcessor.getTaskId(), searchProcessor.getStartTime(), currentTime);
        }

        return timeout;
    }

    private void exceptionHandle(Exception e, String remark) throws ResultEmptyException {
        if (e instanceof ResultEmptyException) {
            throw (ResultEmptyException)e;
        } else if (e.getCause() instanceof ResultEmptyException) {
            throw new ResultEmptyException(e);
        } else {
            log.error(remark + e.getMessage(), e);
        }
    }
}
