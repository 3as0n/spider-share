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

import com.datatrees.common.util.GsonUtils;
import com.datatrees.common.util.PatternUtils;
import com.datatrees.spider.share.service.collector.chain.Context;
import com.datatrees.spider.share.service.collector.search.SearchProcessor;
import com.datatrees.spider.share.service.collector.search.URLHandlerImpl;
import com.treefinance.crawler.framework.context.function.LinkNode;
import com.treefinance.crawler.framework.process.search.SearchTemplateCombine;

import java.util.List;

/**
 * @author <A HREF="">Cheng Wang</A>
 * @version 1.0
 * @since 2015年7月29日 上午11:41:34
 */
public class AddParserTemplateUrlFilter extends RemovedFetchLinkNodeFilter {

    @Override
    protected void doProcess(LinkNode fetchLinkNode, SearchProcessor searchProcessor, Context context) {
        if (fetchLinkNode.isFromParser() && PatternUtils.match("\\#\\{page,", fetchLinkNode.getUrl())) {
            int pageNum = 1;
            String currentParserLinkUrl = SearchTemplateCombine.customTemplate(fetchLinkNode.getUrl(), pageNum);

            if (!currentParserLinkUrl.equals(fetchLinkNode.getUrl())) {
                logger.info("currentParserLinkUrl : {}", currentParserLinkUrl);
                URLHandlerImpl handler = context.getURLHandlerImpl();
                List<LinkNode> parserTemplatelinkNodeList = handler.getTempLinkNodes();

                LinkNode firsetLinkNode = GsonUtils.fromJson(GsonUtils.toJson(fetchLinkNode), LinkNode.class);
                firsetLinkNode.setUrl(currentParserLinkUrl);
                parserTemplatelinkNodeList.add(firsetLinkNode);

                String lastParserLinkUrl = "";
                while (!lastParserLinkUrl.equals(currentParserLinkUrl)) {
                    lastParserLinkUrl = currentParserLinkUrl;
                    currentParserLinkUrl = SearchTemplateCombine.customTemplate(fetchLinkNode.getUrl(), ++pageNum);
                    if (lastParserLinkUrl.equals(currentParserLinkUrl)) {
                        break;
                    }
                    logger.info("currentParserLinkUrl : {}", currentParserLinkUrl);
                    LinkNode parserLinkNode = GsonUtils.fromJson(GsonUtils.toJson(fetchLinkNode), LinkNode.class);
                    parserLinkNode.setUrl(currentParserLinkUrl);
                    parserLinkNode.setRemoved(false);
                    parserTemplatelinkNodeList.add(parserLinkNode);
                }
            }

            // remove parser template linkNode
            fetchLinkNode.setRemoved(true);
        }
    }

}
