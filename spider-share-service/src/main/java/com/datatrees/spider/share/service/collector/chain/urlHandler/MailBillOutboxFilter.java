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
import com.datatrees.spider.share.service.domain.data.MailBillData;
import com.treefinance.crawler.framework.config.enums.SearchType;
import com.treefinance.crawler.framework.context.ProcessorResult;
import com.treefinance.crawler.framework.context.function.LinkNode;
import org.apache.commons.lang.StringUtils;

/**
 * @author <A HREF="">Cheng Wang</A>
 * @version 1.0
 * @since 2015年8月7日 上午12:42:44
 */
public class MailBillOutboxFilter extends RemovedFetchLinkNodeFilter {

    private static String[] recipientPatternKeys = PropertiesConfiguration.getInstance().get("mail.recipient.pattern.key", "emailAccount,qqAccount").split(",");

    private static String outBoxFolderName = PropertiesConfiguration.getInstance().get("mail.outbox.folder.name", "已发送");

    @Override
    protected void doProcess(LinkNode fetchLinkNode, SearchProcessor searchProcessor, Context context) {
        String websiteType = searchProcessor.getProcessorContext().getWebsite().getWebsiteType();
        Object sender = fetchLinkNode.getProperty(MailBillData.SENDER);
        Object folder = fetchLinkNode.getProperty(MailBillData.FOLDER);
        // use sender to detect mail in outbox
        if (WebsiteType.MAIL.getValue().equals(websiteType) && SearchType.KEYWORD_SEARCH.equals(searchProcessor.getSearchTemplateConfig().getType())) {
            boolean inOutbox = false;
            if (folder != null && PatternUtils.match(outBoxFolderName, folder.toString())) {
                inOutbox = true;
            }

            if (!inOutbox && sender != null) {
                ProcessorResult<String, Object> result = searchProcessor.getProcessorContext().getProcessorResult();
                for (String key : recipientPatternKeys) {
                    Object value = result.get(key);
                    // mail is send by self
                    if (value != null && StringUtils.isNotBlank(value.toString()) && PatternUtils.match(value.toString(), sender.toString())) {
                        inOutbox = true;
                        break;
                    }
                }
            }
            if (inOutbox) {
                logger.info("Node: {} filtered as in outbox.", fetchLinkNode);
                fetchLinkNode.setRemoved(true);
            }
        }
    }

}
