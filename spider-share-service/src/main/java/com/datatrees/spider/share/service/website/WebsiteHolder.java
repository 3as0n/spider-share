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

package com.datatrees.spider.share.service.website;

import com.datatrees.spider.share.domain.website.WebsiteConfig;
import com.treefinance.crawler.framework.context.Website;

/**
 * 运营商,电商,website表不一样
 * 
 * @author zhouxinghai
 * @date 2018/7/23
 */
public interface WebsiteHolder {

    /**
     * 是否支持
     * 
     * @param websiteName
     * @return
     */
    boolean support(String websiteName);

    /**
     * 获取Website
     * 
     * @param websiteName
     * @return
     */
    Website getWebsite(String websiteName);

    WebsiteConfig getWebsiteConfig(String websiteName);

}
