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

package com.datatrees.spider.share.service;

import com.datatrees.spider.share.domain.model.WebsiteConf;
import com.datatrees.spider.share.domain.website.WebsiteConfig;
import com.treefinance.crawler.framework.context.ExtractorProcessorContext;
import com.treefinance.crawler.framework.context.SearchProcessorContext;
import com.treefinance.crawler.framework.context.Website;

import java.util.List;
import java.util.Map;

/**
 * 站点配置 Created by zhouxinghai on 2017/6/30.
 */
public interface WebsiteConfigService {

    /**
     * 获取SearchProcessorContext,taskInit使用
     * 
     * @param websiteName
     * @return
     */
    SearchProcessorContext getSearchProcessorContext(Long taskId, String websiteName);

    ExtractorProcessorContext getExtractorProcessorContext(Long taskId, String websiteName);

    ExtractorProcessorContext getExtractorProcessorContextWithBankId(int bankId, Long taskId);

    /**
     * 将WebsiteConfig转化成Website
     * 
     * @param websiteConfig
     * @return
     */
    Website buildWebsite(WebsiteConfig websiteConfig);

    /**
     * key:bankId value:websiteName
     * 
     * @param map
     */
    void initBankCache(Map<Integer, String> map);

    WebsiteConf getWebsiteConf(String websiteName);

    List<WebsiteConf> getWebsiteConf(List<String> websiteNameList);

}
