/*
 * Copyright © 2015 - 2018 杭州大树网络技术有限公司. All Rights Reserved
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

package com.datatrees.spider.share.service.collector.chain.search;

import com.datatrees.spider.share.service.collector.chain.Context;
import com.datatrees.spider.share.service.collector.chain.Filter;
import com.datatrees.spider.share.service.collector.chain.FilterChain;
import com.datatrees.spider.share.service.collector.search.SearchProcessor;
import com.treefinance.crawler.framework.context.ProcessorContextUtil;
import com.treefinance.crawler.framework.context.function.LinkNode;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;

import java.util.List;

/**
 * @author Jerry
 * @since 18:41 2018/4/17
 */
abstract class LinkNodesFilter implements Filter {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public void doFilter(Context context, FilterChain filterChain) {
        SearchProcessor searchProcessor = context.getSearchProcessor();
        List<LinkNode> linkNodes = ProcessorContextUtil.getThreadLocalLinkNode(searchProcessor.getProcessorContext());
        if (CollectionUtils.isNotEmpty(linkNodes)) {
            doInternalFilter(linkNodes, searchProcessor, context);
        }

        filterChain.doFilter(context);
    }

    protected abstract void doInternalFilter(@Nonnull List<LinkNode> linkNodes, SearchProcessor searchProcessor, Context context);

}
