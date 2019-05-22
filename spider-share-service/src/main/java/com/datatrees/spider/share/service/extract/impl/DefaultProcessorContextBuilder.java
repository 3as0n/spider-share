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

package com.datatrees.spider.share.service.extract.impl;

import com.alibaba.fastjson.JSON;
import com.datatrees.common.conf.PropertiesConfiguration;
import com.datatrees.spider.share.domain.ResultType;
import com.datatrees.spider.share.service.WebsiteConfigService;
import com.datatrees.spider.share.service.domain.ExtractMessage;
import com.datatrees.spider.share.service.domain.SpiderTask;
import com.treefinance.crawler.framework.context.ExtractorProcessorContext;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author <A HREF="">Cheng Wang</A>
 * @version 1.0
 * @since 2015年7月30日 下午8:32:23
 */
@Component
public class DefaultProcessorContextBuilder {

    private final static Logger logger = LoggerFactory.getLogger(DefaultProcessorContextBuilder.class);

    private final List<String> extractorUseDefaultWebsiteIds;

    @Resource
    private WebsiteConfigService websiteConfigService;

    public DefaultProcessorContextBuilder() {
        // FIXME: 2018/11/29 强制过滤，有风险，功夫贷162是卡牛
        String extractorUseDefaultWebsiteIds = PropertiesConfiguration.getInstance().get("extractor.use.default.websiteIds", "162");
        if (StringUtils.isNotEmpty(extractorUseDefaultWebsiteIds)) {
            this.extractorUseDefaultWebsiteIds =
                Arrays.stream(extractorUseDefaultWebsiteIds.split(",")).map(String::trim).filter(s -> !s.isEmpty()).distinct().collect(Collectors.toList());
        } else {
            this.extractorUseDefaultWebsiteIds = Collections.emptyList();
        }
    }

    public ExtractorProcessorContext buildExtractorProcessorContext(ExtractMessage extractMessage) {
        ResultType resultType = extractMessage.getResultType();
        ExtractorProcessorContext context;

        switch (resultType) {
            case MAILBILL:
                String websiteIdStr = Integer.toString(extractMessage.getWebsiteId());
                if (extractorUseDefaultWebsiteIds.contains(websiteIdStr)) {
                    context = websiteConfigService.getExtractorProcessorContext(extractMessage.getTaskId(), extractMessage.getWebsiteName());
                } else {
                    if (extractMessage.getTypeId() == 0) {
                        logger.error("invalid bankId,extractMessage={}", JSON.toJSONString(extractMessage));
                    }
                    context = websiteConfigService.getExtractorProcessorContextWithBankId(extractMessage.getTypeId(), extractMessage.getTaskId());
                }
                break;
            default:
                // use the same website config to extract
                context = websiteConfigService.getExtractorProcessorContext(extractMessage.getTaskId(), extractMessage.getWebsiteName());
                break;
        }

        if (context != null) {
            SpiderTask task = extractMessage.getTask();
            context.copyCookies(task.getProcessorContext());
            logger.debug("Copy cookies into extract context from search context: {}", task.getCookies());
        }

        return context;
    }
}
