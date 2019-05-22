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

package com.datatrees.spider.share.service.collector.chain;

import com.datatrees.spider.share.service.collector.search.SearchProcessor;
import com.datatrees.spider.share.service.collector.search.URLHandlerImpl;
import com.datatrees.spider.share.service.collector.worker.deduplicate.DuplicateChecker;
import com.treefinance.crawler.framework.context.function.CrawlRequest;
import com.treefinance.crawler.framework.context.function.CrawlResponse;
import com.treefinance.crawler.framework.context.function.LinkNode;
import com.treefinance.crawler.lang.AtomicAttributes;

import java.util.List;

/**
 * @author <A HREF="">Cheng Wang</A>
 * @version 1.0
 * @since 2015年7月29日 上午2:34:43
 */
public class Context extends AtomicAttributes {

    private final static String FETCHED_LINK_NODE_LIST = "FETCHED_LINK_NODE_LIST";

    private final static String CURRENT_REQUEST = "CURRENT_REQUEST";

    private final static String CURRENT_RESPONSE = "CURRENT_RESPONSE";

    // search processor filter
    private final static String SEARCH_PROCESSOR = "SEARCH_PROCESSOR";

    private final static String DUPLICATE_CHECKER = "DUPLICATE_CHECKER";

    private final static String FECHED_LINK_NODE = "FECHED_LINK_NODE";

    // hosting site & redlist filter
    private final static String URL_HANDLER = "URL_HANDLER";

    // common proerites
    private final static String CURRENT_LINK_NODE = "CURRENT_LINK_NODE";

    public LinkNode getCurrentLinkNode() {
        return (LinkNode)getAttribute(CURRENT_LINK_NODE);
    }

    public void setCurrentLinkNode(LinkNode node) {
        setAttribute(CURRENT_LINK_NODE, node);
    }

    public SearchProcessor getSearchProcessor() {
        return (SearchProcessor)getAttribute(SEARCH_PROCESSOR);
    }

    public void setSearchProcessor(SearchProcessor searchProcessor) {
        setAttribute(SEARCH_PROCESSOR, searchProcessor);
    }

    public CrawlRequest getCrawlRequest() {
        return (CrawlRequest)getAttribute(CURRENT_REQUEST);
    }

    public void setCrawlRequest(CrawlRequest request) {
        setAttribute(CURRENT_REQUEST, request);
    }

    public CrawlResponse getCrawlResponse() {
        return (CrawlResponse)getAttribute(CURRENT_RESPONSE);
    }

    public void setCrawlResponse(CrawlResponse response) {
        setAttribute(CURRENT_RESPONSE, response);
    }

    public LinkNode getFetchLinkNode() {
        return (LinkNode)getAttribute(FECHED_LINK_NODE);
    }

    public void setFetchLinkNode(LinkNode fetchLinkNode) {
        setAttribute(FECHED_LINK_NODE, fetchLinkNode);
    }

    public URLHandlerImpl getURLHandlerImpl() {
        return (URLHandlerImpl)getAttribute(URL_HANDLER);
    }

    public void setURLHandlerImpl(URLHandlerImpl handler) {
        setAttribute(URL_HANDLER, handler);
    }

    public List<LinkNode> getFetchedLinkNodeList() {
        return (List<LinkNode>)getAttribute(FETCHED_LINK_NODE_LIST);
    }

    public void setFetchedLinkNodeList(List<LinkNode> linkNodeList) {
        setAttribute(FETCHED_LINK_NODE_LIST, linkNodeList);
    }

    public DuplicateChecker getDuplicateChecker() {
        return (DuplicateChecker)getAttribute(DUPLICATE_CHECKER);
    }

    public void setDuplicateChecker(DuplicateChecker handler) {
        setAttribute(DUPLICATE_CHECKER, handler);
    }

}
