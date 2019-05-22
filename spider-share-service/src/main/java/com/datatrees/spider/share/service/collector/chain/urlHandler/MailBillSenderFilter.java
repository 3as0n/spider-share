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
import com.datatrees.common.util.PatternUtils;
import com.datatrees.spider.share.domain.website.WebsiteType;
import com.datatrees.spider.share.service.collector.chain.Context;
import com.datatrees.spider.share.service.collector.search.SearchProcessor;
import com.datatrees.spider.share.service.domain.data.MailBillData;
import com.treefinance.crawler.framework.config.enums.SearchType;
import com.treefinance.crawler.framework.context.function.LinkNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author <A HREF="">Cheng Wang</A>
 * @version 1.0
 * @since 2015年8月7日 上午12:42:44
 */
public class MailBillSenderFilter extends RemovedFetchLinkNodeFilter {

    private static final Logger logger = LoggerFactory.getLogger(MailBillSenderFilter.class);

    private static String senderBlackListPattern = PropertiesConfiguration.getInstance().get("mail.sender.blacklist.pattern",
        "@lakala.com|^10086@139.com|^mail139@139.com|^10000@qq.com|@51zhangdan.com.cn|@wacai.com|magazine@shenzhenair.com|@feidee.com|@news1.elong.com|service@service.fenqile.com|no-reply@notice.jimubox.com|noreply@ppdai.com|cmpassport139@139.com|xinyue@tencent.com|Info@ctrip.com|week@feidee.net|service@iboxpay.com|noreply@firstp2p.com|@ygdai.com");

    @Override
    protected void doProcess(LinkNode fetchLinkNode, SearchProcessor searchProcessor, Context context) {
        String websiteType = searchProcessor.getProcessorContext().getWebsite().getWebsiteType();

        if (websiteType != null && WebsiteType.MAIL.getValue().equals(websiteType) && SearchType.KEYWORD_SEARCH.equals(searchProcessor.getSearchTemplateConfig().getType())) {
            Object sender = fetchLinkNode.getProperty(MailBillData.SENDER);
            if (sender != null && PatternUtils.match(senderBlackListPattern, sender.toString().toLowerCase())) {
                logger.info("Node: {} filtered as the sender is {}", fetchLinkNode, sender);
                fetchLinkNode.setRemoved(true);
            }
        }
    }
}
