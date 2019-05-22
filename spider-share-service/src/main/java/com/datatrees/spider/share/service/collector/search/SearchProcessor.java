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

import akka.dispatch.Future;
import com.alibaba.rocketmq.common.ThreadFactoryImpl;
import com.datatrees.spider.share.domain.model.Task;
import com.datatrees.spider.share.service.collector.actor.TaskMessage;
import com.datatrees.spider.share.service.collector.chain.Context;
import com.datatrees.spider.share.service.collector.chain.Filters;
import com.datatrees.spider.share.service.collector.common.CollectorConstants;
import com.datatrees.spider.share.service.collector.worker.ResultDataHandler;
import com.treefinance.crawler.framework.boot.Crawler;
import com.treefinance.crawler.framework.config.enums.SearchType;
import com.treefinance.crawler.framework.config.xml.SearchConfig;
import com.treefinance.crawler.framework.config.xml.properties.Properties;
import com.treefinance.crawler.framework.config.xml.search.SearchTemplateConfig;
import com.treefinance.crawler.framework.context.ProcessorContextUtil;
import com.treefinance.crawler.framework.context.RequestUtil;
import com.treefinance.crawler.framework.context.ResponseUtil;
import com.treefinance.crawler.framework.context.SearchProcessorContext;
import com.treefinance.crawler.framework.context.function.CrawlRequest;
import com.treefinance.crawler.framework.context.function.CrawlResponse;
import com.treefinance.crawler.framework.context.function.LinkNode;
import com.treefinance.crawler.framework.exception.ResultEmptyException;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author <A HREF="">Cheng Wang</A>
 * @version 1.0
 * @since 2015年7月29日 上午12:45:25
 */
public class SearchProcessor {

    private static final Logger log = LoggerFactory.getLogger(SearchProcessor.class);

    private final List<Future<Object>> futureList = new ArrayList<>();

    private final TaskMessage taskMessage;

    private String searchTemplate;

    private SearchTemplateConfig searchTemplateConfig;

    private String encoding;

    private long waitIntervalMillis;

    private boolean duplicateRemoval;

    private ResultDataHandler resultDataHandler;

    private String keyword;

    private boolean isLastLink;

    private boolean needEarlyQuit;

    private ThreadPoolExecutor crawlExecutorPool = null;

    // 任务默认超时时间，单位：毫秒
    private long defaultTimeout = -1;

    // 任务超时时间，单位：毫秒
    private long timeout = -1;

    // 任务可执行的结束时间戳，单位：毫秒
    private long deadLine = -1;

    public SearchProcessor(TaskMessage taskMessage) {
        this.taskMessage = taskMessage;

        SearchProcessorContext processorContext = getProcessorContext();
        if (processorContext != null) {
            SearchConfig config = processorContext.getSearchConfig();
            if (null != config) {
                Properties properties = config.getProperties();
                if (null != properties) {
                    String encoding = StringUtils.defaultIfBlank(properties.getEncoding(), CollectorConstants.DEFUALT_ENCODING);
                    setEncoding(encoding);
                    setWaitIntervalMillis(properties.getWaitIntervalMillis() == null ? 0 : properties.getWaitIntervalMillis());
                    setDuplicateRemoval(properties.getDuplicateRemoval());

                    Task task = taskMessage.getTask();
                    log.info("DuplicateRemoval Config is {}, workingTaskEntity_id: {}", properties.getDuplicateRemoval(), task.getId());
                }
            }
        }
    }

    /**
     *
     */
    public void initWithKeyword(String keyword) {
        this.init();

        if (keyword != null) {
            this.keyword = keyword;
            ProcessorContextUtil.setKeyword(getProcessorContext(), keyword);
        }
    }

    public void init() {
        this.needEarlyQuit = false;
        this.isLastLink = false;
    }

    public List<LinkNode> crawlOneURL(LinkNode url) throws ResultEmptyException {
        List<LinkNode> linkNodeList = null;
        try {
            URLHandlerImpl handler = initURLHandlerImpl();
            CrawlRequest request = CrawlRequest.newBuilder().setUrl(url).setSearchContext(getProcessorContext()).setTemplateId(searchTemplateConfig.getId())
                .setSeedUrl(searchTemplate).setUrlHandler(handler).build();

            RequestUtil.setKeyWord(request, keyword);
            CrawlResponse response = Crawler.crawl(request);
            List<Object> objs = ResponseUtil.getResponseObjectList(response);

            if (CollectionUtils.isNotEmpty(objs)) {
                synchronized (futureList) {
                    futureList.addAll(resultDataHandler.resultListHandler(objs, taskMessage));
                }
            }

            linkNodeList = response.getUrls();
            linkNodeList.addAll(handler.getTempLinkNodes());

            Context context = new Context();
            context.setCurrentLinkNode(url);
            context.setSearchProcessor(this);
            context.setCrawlRequest(request);
            context.setCrawlResponse(response);
            context.setFetchedLinkNodeList(linkNodeList);

            Filters.SEARCH.doFilter(context);
        } catch (ResultEmptyException e) {
            throw e;
        } catch (Exception e) {
            log.error("Caught Exception in crawlOneURL ,url [{}]", url.getUrl(), e);
        } finally {
            // clear Embedded context
            ProcessorContextUtil.clearThreadLocalLinkNode(getProcessorContext());
            ProcessorContextUtil.clearThreadLocalResponseList(getProcessorContext());
        }
        return linkNodeList;
    }

    public boolean isDuplicateRemoval() {
        return duplicateRemoval;
    }

    public void setDuplicateRemoval(boolean duplicateRemoval) {
        this.duplicateRemoval = duplicateRemoval;
    }

    public boolean isNeedEarlyQuit() {
        return needEarlyQuit;
    }

    public void setNeedEarlyQuit(boolean needEarlyQuit) {
        this.needEarlyQuit = needEarlyQuit;
    }

    /**
     * @return the encoding
     */
    public String getEncoding() {
        return encoding;
    }

    /**
     * @param encoding the encoding to set
     */
    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    /**
     * @return the searchTemplateF
     */
    public String getSearchTemplate() {
        return searchTemplate;
    }

    /**
     * @param searchTemplate the searchTemplate to set
     */
    public SearchProcessor setSearchTemplate(String searchTemplate) {
        this.searchTemplate = searchTemplate;
        return this;
    }

    public long getWaitIntervalMillis() {
        if (searchTemplateConfig == null || searchTemplateConfig.getWaitIntervalMillis() == null) {
            return waitIntervalMillis;
        } else {
            return searchTemplateConfig.getWaitIntervalMillis();
        }
    }

    public void setWaitIntervalMillis(long waitIntervalMillis) {
        this.waitIntervalMillis = waitIntervalMillis;
    }

    public SearchTemplateConfig getSearchTemplateConfig() {
        return searchTemplateConfig;
    }

    public SearchProcessor setSearchTemplateConfig(SearchTemplateConfig searchTemplateConfig) {
        this.searchTemplateConfig = searchTemplateConfig;
        return this;
    }

    /**
     * @return the keyword
     */
    public String getKeyword() {
        return keyword;
    }

    /**
     * @param resultDataHandler the resultDataHandler to set
     */
    public SearchProcessor setResultDataHandler(ResultDataHandler resultDataHandler) {
        this.resultDataHandler = resultDataHandler;
        return this;
    }

    /**
     * @return the futureList
     */
    public List<Future<Object>> getFutureList() {
        return futureList;
    }

    /**
     * @return the isLastLink
     */
    public boolean isLastLink() {
        return isLastLink;
    }

    /**
     * @param isLastLink the isLastLink to set
     */
    public void setLastLink(boolean isLastLink) {
        this.isLastLink = isLastLink;
    }

    /**
     * @return the crawlExecutorPool
     */
    public ExecutorService getCrawlExecutorPool(Integer threadCount) {
        if (crawlExecutorPool != null) {
            crawlExecutorPool.setCorePoolSize(threadCount);
            crawlExecutorPool.setMaximumPoolSize(threadCount);
        } else {
            crawlExecutorPool = initCrawlExecutorPool(threadCount);
        }
        return crawlExecutorPool;
    }

    public ExecutorService getCrawlExecutorPool() {
        return crawlExecutorPool;
    }

    /**
     * 设置默认超时. if <code>unit</code> is null,parse <code>timeout</code> as millis.
     *
     * @param timeout 超时时间
     * @param unit 时间单位
     */
    public void setDefaultTimeout(long timeout, TimeUnit unit) {
        if (unit != null) {
            this.defaultTimeout = unit.toMillis(timeout);
        } else {
            this.defaultTimeout = timeout;
        }
        if (this.deadLine == -1) {
            resetDeadLine(this.defaultTimeout);
        }
    }

    /**
     * 设置超时. if <code>unit</code> is null,parse <code>timeout</code> as millis.
     *
     * @param timeout 超时时间
     * @param unit 时间单位
     */
    public void setTimeout(long timeout, TimeUnit unit) {
        if (unit != null && timeout > 0) {
            this.timeout = unit.toMillis(timeout);
        } else {
            this.timeout = timeout;
        }
        resetDeadLine(this.timeout);
    }

    public boolean isTimeout(long timeInMillis) {
        if (deadLine <= 0) {
            return false;
        }

        long now = timeInMillis;
        if (now <= 0) {
            now = System.currentTimeMillis();
        }

        return now > deadLine;
    }

    public Task getTask() {
        return taskMessage.getTask();
    }

    public int getTaskId() {
        return getTask().getId();
    }

    public long getStartTime() {
        if (getTask().getStartedAt() == null) {
            throw new IllegalArgumentException("Task may be created incorrect! The start time was lost!");
        }
        return getTask().getStartedAt().getTime();
    }

    public SearchProcessorContext getProcessorContext() {
        return taskMessage.getContext();
    }

    public boolean isKeywordSearch() {
        return SearchType.KEYWORD_SEARCH.equals(searchTemplateConfig.getType());
    }

    public Integer getWebsiteType() {
        return Integer.valueOf(getProcessorContext().getWebsiteType());
    }

    private URLHandlerImpl initURLHandlerImpl() {
        URLHandlerImpl handler = new URLHandlerImpl();
        handler.setSearchProcessor(this);
        return handler;
    }

    private ThreadPoolExecutor initCrawlExecutorPool(int threadCount) {
        return new ThreadPoolExecutor(threadCount, threadCount, 20L, java.util.concurrent.TimeUnit.SECONDS, new SynchronousQueue<Runnable>(),
            new ThreadFactoryImpl(Thread.currentThread().getName() + "_"));
    }

    private void resetDeadLine(long timeout) {
        if (timeout > 0) {
            this.deadLine = getStartTime() + timeout;
        } else {
            this.deadLine = 0;
        }
    }
}
