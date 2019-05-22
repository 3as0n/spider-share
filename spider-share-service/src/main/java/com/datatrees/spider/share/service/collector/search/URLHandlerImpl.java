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

import com.datatrees.spider.share.service.collector.chain.Context;
import com.datatrees.spider.share.service.collector.chain.Filters;
import com.datatrees.spider.share.service.collector.worker.deduplicate.DuplicateChecker;
import com.treefinance.crawler.framework.context.function.LinkNode;
import com.treefinance.crawler.framework.process.search.URLHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <A HREF="">Cheng Wang</A>
 * @version 1.0
 * @since 2015年7月28日 下午3:53:23
 */
public class URLHandlerImpl implements URLHandler {

    private static final Logger log = LoggerFactory.getLogger(URLHandlerImpl.class);

    private List<LinkNode> tempLinkNodes = new ArrayList<LinkNode>();

    private DuplicateChecker duplicateChecker;

    private SearchProcessor searchProcessor;

    public URLHandlerImpl() {
        super();
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.datatrees.vt.core.domainimpl.page.typehandler.URLHandler#handle(com.datatrees.vt.core.domainimpl.
     * bean.LinkNode, com.datatrees.vt.core.domainimpl.bean.LinkNode)
     */
    @Override
    public boolean handle(LinkNode current, LinkNode fetched) {
        try {
            Context context = new Context();
            context.setFetchLinkNode(fetched);
            context.setCurrentLinkNode(current);
            context.setURLHandlerImpl(this);
            context.setSearchProcessor(searchProcessor);
            context.setDuplicateChecker(duplicateChecker);

            Filters.LINKNODE.doFilter(context);
        } catch (Exception e) {
            log.error("Caught Exception in HostingsiteURLHandler while invoke typehandler mthod . fetched url [" + fetched.getUrl() + "]", e);
        }
        return fetched.isRemoved();
    }

    /**
     * @return the tempLinkNodes
     */
    public List<LinkNode> getTempLinkNodes() {
        return tempLinkNodes;
    }

    /**
     * @param tempLinkNodes the tempLinkNodes to set
     */
    public void setTempLinkNodes(List<LinkNode> tempLinkNodes) {
        this.tempLinkNodes = tempLinkNodes;
    }

    /**
     * @return the duplicateChecker
     */
    public DuplicateChecker getDuplicateChecker() {
        return duplicateChecker;
    }

    /**
     * @param duplicateChecker the duplicateChecker to set
     */
    public void setDuplicateChecker(DuplicateChecker duplicateChecker) {
        this.duplicateChecker = duplicateChecker;
    }

    /**
     * @return the searchProcessor
     */
    public SearchProcessor getSearchProcessor() {
        return searchProcessor;
    }

    /**
     * @param searchProcessor the searchProcessor to set
     */
    public void setSearchProcessor(SearchProcessor searchProcessor) {
        this.searchProcessor = searchProcessor;
    }

}
