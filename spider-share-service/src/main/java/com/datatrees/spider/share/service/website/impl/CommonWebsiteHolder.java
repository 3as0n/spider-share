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

package com.datatrees.spider.share.service.website.impl;

import com.datatrees.spider.share.common.utils.WebsiteUtils;
import com.datatrees.spider.share.domain.model.WebsiteInfo;
import com.datatrees.spider.share.domain.website.WebsiteConfig;
import com.datatrees.spider.share.service.WebsiteConfigService;
import com.datatrees.spider.share.service.WebsiteInfoService;
import com.datatrees.spider.share.service.website.WebsiteHolder;
import com.treefinance.crawler.framework.context.Website;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class CommonWebsiteHolder implements WebsiteHolder {

    @Resource
    private WebsiteConfigService websiteConfigService;

    @Resource
    private WebsiteInfoService websiteInfoService;

    @Override
    public boolean support(String websiteName) {
        return !WebsiteUtils.isOperator(websiteName);
    }

    @Override
    public Website getWebsite(String websiteName) {
        WebsiteConfig websiteConfig = getWebsiteConfig(websiteName);
        return websiteConfigService.buildWebsite(websiteConfig);
    }

    @Override
    public WebsiteConfig getWebsiteConfig(String websiteName) {
        WebsiteInfo websiteInfo = websiteInfoService.getByWebsiteName(websiteName);
        return websiteInfoService.buildWebsiteConfig(websiteInfo);
    }
}
