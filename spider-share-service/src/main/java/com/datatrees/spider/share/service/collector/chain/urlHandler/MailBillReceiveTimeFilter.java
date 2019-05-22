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

package com.datatrees.spider.share.service.collector.chain.urlHandler;

import com.datatrees.common.conf.PropertiesConfiguration;
import com.datatrees.spider.share.domain.website.WebsiteType;
import com.datatrees.spider.share.service.collector.chain.Context;
import com.datatrees.spider.share.service.collector.search.SearchProcessor;
import com.datatrees.spider.share.service.domain.data.MailBillData;
import com.datatrees.spider.share.service.util.UnifiedSysTime;
import com.treefinance.crawler.framework.config.enums.SearchType;
import com.treefinance.crawler.framework.context.function.LinkNode;

import java.util.Date;

/**
 * @author <A HREF="">Cheng Wang</A>
 * @version 1.0
 * @since 2015年8月7日 上午1:17:04
 */
public class MailBillReceiveTimeFilter extends RemovedFetchLinkNodeFilter {

    private static long maxMailReceiveInterval = PropertiesConfiguration.getInstance().getLong("max.mail.receive.interval", (long)(1000L * 3600L * 24 * 365 * 1.5));

    private static boolean mailReceiveTimeFilterSwitch = PropertiesConfiguration.getInstance().getBoolean("mail.receive.filter.switch", true);

    @Override
    protected void doProcess(LinkNode fetchLinkNode, SearchProcessor searchProcessor, Context context) {
        String websiteType = searchProcessor.getProcessorContext().getWebsite().getWebsiteType();
        Object receiveAt = fetchLinkNode.getPropertys().get(MailBillData.RECEIVED);
        LinkNode currentLinkNode = context.getCurrentLinkNode();

        if (searchProcessor.isLastLink() && currentLinkNode != null && currentLinkNode.getpNum() > 0) {
            logger.info("filter pageNode: {} as LastPageLink marked ,receiveAt: {}", fetchLinkNode, receiveAt);
            fetchLinkNode.setRemoved(true);
        } else if (mailReceiveTimeFilterSwitch && receiveAt instanceof Date && WebsiteType.MAIL.getValue().equals(websiteType)
            && SearchType.KEYWORD_SEARCH.equals(searchProcessor.getSearchTemplateConfig().getType())) {
            if ((UnifiedSysTime.INSTANCE.getSystemTime().getTime() - ((Date)receiveAt).getTime() > maxMailReceiveInterval)) {
                logger.info("Node: {},receiveAt: {} receive time filtered...", fetchLinkNode, receiveAt);
                fetchLinkNode.setRemoved(true);
                if (currentLinkNode != null && currentLinkNode.getpNum() > 0) {
                    logger.info("receive time come to threshold, mark as the LastPageLink ...");
                    searchProcessor.setLastLink(true);
                    searchProcessor.getProcessorContext().addProcessorResult("LastPageLink", true);
                }
            }
        }
    }

}
