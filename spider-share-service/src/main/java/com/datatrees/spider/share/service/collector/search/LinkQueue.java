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

import com.datatrees.spider.share.service.collector.bdb.operator.BDBOperator;
import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.EnvironmentLockedException;
import com.treefinance.crawler.framework.config.xml.search.SearchTemplateConfig;
import com.treefinance.crawler.framework.context.function.LinkNode;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author <A HREF="">Cheng Wang</A>
 * @version 1.0
 * @since 2015年7月29日 上午12:36:17
 */
public class LinkQueue {

    private static final Logger log = LoggerFactory.getLogger(LinkQueue.class);

    private BDBOperator bdbOperator;

    private SearchTemplateConfig searchTemplateConfig;

    private int queueSize = 0;

    private boolean isFull = false;

    public LinkQueue(SearchTemplateConfig searchTemplateConfig) {
        this.searchTemplateConfig = searchTemplateConfig;
    }

    /**
     * @param homePath
     * @return
     */
    public boolean init() {
        return this.init(null);
    }

    public boolean init(List<LinkNode> initLinkNodeList) {
        try {
            bdbOperator = new BDBOperator();
            if (CollectionUtils.isNotEmpty(initLinkNodeList)) {
                this.addLinks(initLinkNodeList);
            }
            return true;
        } catch (EnvironmentLockedException e) {
            log.error("LinkQueue init failed because of EnvironmentLockedException ", e);
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e1) {
                log.error("init error ", e1);
            }
        } catch (DatabaseException e) {
            log.error("LinkQueue init failed because of DatabaseException", e);
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e1) {
                log.error("init error ", e1);
            }
        } catch (Exception e) {
            log.error("LinkQueue init failed ", e);
        }
        return false;
    }

    /**
     * add batch of links into BDB queue
     * 
     * @param links
     * @param worker
     * @return new link num
     */
    public int addLinks(List<LinkNode> links) {
        synchronized (bdbOperator) {
            int newLinkNum = 0;
            long preId = bdbOperator.getCurrentId();
            for (LinkNode linkNode : links) {
                if (linkNode == null) {
                    continue;
                }
                try {
                    newLinkNum += addLink(linkNode);
                } catch (Exception ex) {
                    log.error("Catch exception when add link: [" + linkNode.getUrl() + "] to bdb");
                    log.error(ex.toString(), ex);
                }
            }

            log.info("Added [" + (bdbOperator.getCurrentId() - preId) + "] links to link queue , total [" + (bdbOperator.getCurrentId() - 1) + "] in queue current position is "
                + bdbOperator.getLastFetchId());
            log.info("New link added into BDB, new link num:" + newLinkNum);
            return newLinkNum;
        }
    }

    // need optimization later
    public int addLink(LinkNode linkNode) {
        synchronized (bdbOperator) {
            int result = 0;
            if (!checkIfBoundary(linkNode)) {
                result = bdbOperator.addLink(linkNode);
                queueSize += result;
            }
            return result;
        }
    }

    // need optimization later
    public LinkedList<LinkNode> fetchNewLinks(int size) {
        synchronized (bdbOperator) {
            return bdbOperator.fetchNewLinks(size);
        }
    }

    public void closeLinkQueue() {
        synchronized (bdbOperator) {
            if (null != bdbOperator) {
                bdbOperator.closeDatabase();
                bdbOperator = null;
            }
        }
    }

    public long getQueueSize() {
        return queueSize;
    }

    public boolean isFull() {
        return isFull;
    }

    /**
     * @param newLinkNum
     * @param linkNode
     * @param deep
     */
    private boolean checkIfBoundary(LinkNode linkNode) {
        int deep = linkNode.getDepth();
        int maxPage = searchTemplateConfig.getRequest().getMaxPages();
        int maxDepth = searchTemplateConfig.getMaxDepth();

        if (deep > maxDepth) {
            log.debug("drop link [" + linkNode.getUrl() + "] for max depth reached, link depth = [" + deep + "] max depth = [" + maxDepth + "]");

            return true;
        } else if (queueSize > maxPage) {
            isFull = true;
            log.info("max page is " + maxPage + " current queue size is " + queueSize);
            log.warn("drop link [" + linkNode.getUrl() + "] for max page reached. The link queue is full");

            return true;
        }

        return false;
    }
}
