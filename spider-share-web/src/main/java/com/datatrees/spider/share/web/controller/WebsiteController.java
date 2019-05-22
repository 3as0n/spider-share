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

import com.datatrees.spider.share.domain.http.HttpResult;
import com.datatrees.spider.share.service.WebsiteInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * Created by zhouxinghai on 2017/7/5.
 */
@RestController
@RequestMapping("/website")
public class WebsiteController {

    private static final Logger logger = LoggerFactory.getLogger(WebsiteController.class);

    @Resource
    private WebsiteInfoService websiteInfoService;

    @RequestMapping(value = "/updateWebsiteConf", method = RequestMethod.POST)
    public HttpResult<Boolean> updateWebsiteConf(String websiteName, String searchConfig, String extractConfig) {
        HttpResult<Boolean> result = new HttpResult<>();
        try {
            websiteInfoService.updateWebsiteConf(websiteName, searchConfig, extractConfig);
            logger.info("updateWebsiteConf success websiteName={}", websiteName);
            return result.success(true);
        } catch (Exception e) {
            logger.error("updateWebsiteConf error websiteName={}", websiteName, e);
            return result.failure();
        }
    }

}
