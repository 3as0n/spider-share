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
import com.treefinance.crawler.framework.context.function.LinkNode;

import java.util.regex.Pattern;

/**
 * @author <A HREF="">Cheng Wang</A>
 * @version 1.0
 * @since 2015年8月7日 上午12:42:44
 */
public class MailBillIllegalSubjectFilter extends RemovedFetchLinkNodeFilter {

    private static final Pattern subjectContainsPattern = Pattern.compile(PropertiesConfiguration.getInstance().get("mail.subject.contains.pattern", "账单|信用卡|对账"));

    private static final Pattern subjectBlackListPattern = Pattern.compile(PropertiesConfiguration.getInstance().get("mail.subject.blacklist.pattern",
        "消费提醒全面升级-每日信用管家|微·服务】|招商银行信用卡账单分期体验调查|信用卡账单分期订单已成功|每日E-Mail账单提醒|凤凰知音深航会员|天了噜~极速体验账单|分期乐提醒您|账单分期订单已成功|月里程账单|“广发信用卡”微信|账单信息Biu的|携程对账单|QQ邮箱动态|一键“狙击”账单|人人贷|携程旅行网|QQ邮箱账单|（AD）|您的京东|挖财|支付宝|话费|电信|快钱|快递|网易|话单|账单杂志|保险|电费|基金|证券|POS|自动回复|汇添富|地址|星座|易方达|人寿|有限公司|（月度）|易宝|南方电网|致谢函|贷款|电局|已读:|借记卡|温馨提示|额度调升|分期大回馈|分期感恩季"));

    private static final Pattern secondSubjectBlackListPattern = Pattern.compile(PropertiesConfiguration.getInstance().get("mail.subject.second.blacklist.pattern", "分期感恩季"));

    private static final Pattern subjectWhiteListPattern = Pattern.compile(PropertiesConfiguration.getInstance().get("mail.subject.whiltelist.pattern", "招商"));

    @Override
    protected void doProcess(LinkNode fetchLinkNode, SearchProcessor searchProcessor, Context context) {
        String websiteType = searchProcessor.getProcessorContext().getWebsite().getWebsiteType();
        Object subject = fetchLinkNode.getProperty(MailBillData.SUBJECT);
        if (subject != null && WebsiteType.MAIL.getValue().equals(websiteType) && SearchType.KEYWORD_SEARCH.equals(searchProcessor.getSearchTemplateConfig().getType())) {
            if (PatternUtils.match(subjectContainsPattern, subject.toString().replaceAll("\\s", ""))) {
                if (PatternUtils.match(subjectBlackListPattern, subject.toString())) {
                    if (!PatternUtils.match(subjectWhiteListPattern, subject.toString()) || PatternUtils.match(secondSubjectBlackListPattern, subject.toString())) {
                        logger.info("Node: {}, subject: {} filtered by subject blacklist.", fetchLinkNode, subject);
                        fetchLinkNode.setRemoved(true);
                    }
                }
            } else {
                fetchLinkNode.setRemoved(true);
                logger.info("Node: {} filtered as {} discontain '{}'", fetchLinkNode, subject, searchProcessor.getKeyword());
            }
        }
    }

}
