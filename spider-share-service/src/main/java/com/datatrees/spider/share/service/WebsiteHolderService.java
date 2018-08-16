/*
 * Copyright © 2015 - 2018 杭州大树网络技术有限公司. All Rights Reserved
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.datatrees.spider.share.service;

import com.datatrees.crawler.core.domain.Website;
import com.datatrees.spider.share.domain.model.WebsiteConf;
import com.datatrees.spider.share.domain.website.WebsiteConfig;

public interface WebsiteHolderService {

    /**
     * 获取Website
     * @param websiteName
     * @return
     */
    Website getWebsite(String websiteName);

    WebsiteConfig getWebsiteConfig(String websiteName);

    /**
     * 获取WebsiteConf
     * @param websiteName
     * @return
     */
    WebsiteConf getWebsiteConf(String websiteName);

}
