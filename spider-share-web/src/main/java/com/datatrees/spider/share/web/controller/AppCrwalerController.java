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

package com.datatrees.spider.share.web.controller;

import com.datatrees.spider.share.domain.param.AppCrawlerConfigParam;
import com.datatrees.spider.share.service.AppCrawlerConfigService;
import com.treefinance.saas.knife.common.CommonStateCode;
import com.treefinance.saas.knife.result.Results;
import com.treefinance.saas.knife.result.SaasResult;
import com.treefinance.saas.merchant.center.facade.result.console.MerchantAppLicenseResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import java.util.List;

/**
 * User: yand Date: 2018/4/16
 */
@RestController
@RequestMapping("/app/crawler")
public class AppCrwalerController {

    private static final Logger logger = LoggerFactory.getLogger(AppCrwalerController.class);

    @Resource
    private AppCrawlerConfigService appCrawlerConfigService;

    @RequestMapping("/getList")
    public SaasResult<List<AppCrawlerConfigParam>> getAppCrawlerConfigList(@RequestBody List<MerchantAppLicenseResult> appIds) {
        return Results.newSuccessResult(appCrawlerConfigService.getAppCrawlerConfigList(appIds));

    }

    @RequestMapping("/update")
    public SaasResult<String> updateAppCrawlerConfig(@RequestBody AppCrawlerConfigParam params) {
        logger.info("AppCrawlerConfigParam is {}", params);
        appCrawlerConfigService.updateAppConfig(params.getAppId(), params.getProjectConfigInfos());
        return Results.newResult(CommonStateCode.SUCCESS);
    }

}
