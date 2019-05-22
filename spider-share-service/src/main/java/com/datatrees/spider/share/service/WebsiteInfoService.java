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

import com.datatrees.spider.share.domain.model.WebsiteInfo;
import com.datatrees.spider.share.domain.website.WebsiteConfig;

/**
 * Created by zhangyanjia on 2018/3/20.
 */
public interface WebsiteInfoService {

    /**
     * 根据环境和站点名获取运营商配置
     * 
     * @param websiteName
     * @return
     */
    WebsiteInfo getByWebsiteName(String websiteName);

    /**
     * 根据websiteName更新searchConfigSource,extractConfigSource
     * 
     * @param websiteName
     * @param searchConfig
     * @param extractConfig
     */
    void updateWebsiteConf(String websiteName, String searchConfig, String extractConfig);

    WebsiteConfig buildWebsiteConfig(WebsiteInfo websiteInfo);
}
