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

package com.datatrees.spider.share.service;

import com.datatrees.spider.share.domain.param.AppCrawlerConfigParam;
import com.datatrees.spider.share.domain.param.CrawlerProjectParam;
import com.treefinance.saas.merchant.center.facade.result.console.MerchantAppLicenseResult;

import java.util.List;

/**
 * User: yand Date: 2018/4/10
 */
public interface AppCrawlerConfigService {

    /**
     * 从redis中获取
     * 
     * @return
     */
    String getFromRedis(String appId, String project);

    /**
     * 获取所有商户信息
     * 
     * @return
     */
    List<AppCrawlerConfigParam> getAppCrawlerConfigList(List<MerchantAppLicenseResult> appIds);

    /**
     * 修改商户配置信息
     * 
     * @param appId
     * @param projectConfigInfos
     */
    void updateAppConfig(String appId, List<CrawlerProjectParam> projectConfigInfos);

}
