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

import com.datatrees.common.conf.PropertiesConfiguration;
import com.datatrees.common.util.PatternUtils;
import com.datatrees.spider.share.domain.website.WebsiteType;
import com.datatrees.spider.share.service.collector.chain.Context;
import com.datatrees.spider.share.service.collector.search.SearchProcessor;
import com.datatrees.spider.share.service.domain.data.EcommerceData;
import com.treefinance.crawler.framework.context.function.LinkNode;

public class EcommerceTitleFilter extends RemovedFetchLinkNodeFilter {

    private static String titleBlackListPattern = PropertiesConfiguration.getInstance().get("ecommerce.title.blacklist.pattern", "充值|电影票|提现|转账|话费|自动发货|代金劵");

    private static String EcommerceTitleFilterSwitch = PropertiesConfiguration.getInstance().get("ecommerce.title.filter.switch", "off");

    private static String titleWhiteListPattern = PropertiesConfiguration.getInstance().get("ecommerce.title.whiltelist.pattern", "淘宝购物");

    @Override
    protected void doProcess(LinkNode fetchLinkNode, SearchProcessor searchProcessor, Context context) {
        String websiteType = searchProcessor.getProcessorContext().getWebsite().getWebsiteType();
        Object title = fetchLinkNode.getProperty(EcommerceData.TITLE);
        if (EcommerceTitleFilterSwitch.toLowerCase().equals("on") && title != null && websiteType != null && WebsiteType.ECOMMERCE.getValue().equals(websiteType)) {
            if (PatternUtils.match(titleWhiteListPattern, title.toString())) {
                if (!PatternUtils.match(titleBlackListPattern, title.toString())) {
                    logger.info("ECOMMERCE Node: {} filter success...", fetchLinkNode);
                    fetchLinkNode.setRemoved(false);
                } else {
                    logger.info("ECOMMERCE Node: {} filtered by title blacklist...", fetchLinkNode);
                    // in black list need filter
                    fetchLinkNode.setRemoved(true);
                }
            } else {
                // not in whilte list need filter
                logger.info("ECOMMERCE Node: {} filtered by title whitelist...", fetchLinkNode);
                fetchLinkNode.setRemoved(true);
            }
        }
    }

}
