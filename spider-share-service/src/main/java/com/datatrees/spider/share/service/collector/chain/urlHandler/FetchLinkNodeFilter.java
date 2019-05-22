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

package com.datatrees.spider.share.service.collector.chain.urlHandler;

import com.datatrees.spider.share.service.collector.chain.Context;
import com.datatrees.spider.share.service.collector.chain.Filter;
import com.datatrees.spider.share.service.collector.chain.FilterChain;
import com.datatrees.spider.share.service.collector.search.SearchProcessor;
import com.treefinance.crawler.framework.context.function.LinkNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Jerry
 * @since 21:25 2018/4/17
 */
abstract class FetchLinkNodeFilter implements Filter {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void doFilter(Context context, FilterChain filterChain) {
        LinkNode fetchLinkNode = context.getFetchLinkNode();
        SearchProcessor searchProcessor = context.getSearchProcessor();

        boolean filtered = false;
        try {
            filtered = doInternalFilter(fetchLinkNode, searchProcessor, context);
        } catch (Exception e) {
            logger.error("Error invoking filter", e);
        }

        if (filtered) {
            filterChain.doFilter(context);
        }
    }

    protected abstract boolean doInternalFilter(LinkNode fetchLinkNode, SearchProcessor searchProcessor, Context context);
}
