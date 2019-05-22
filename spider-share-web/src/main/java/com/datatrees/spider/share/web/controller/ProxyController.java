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

package com.datatrees.spider.share.web.controller;

import com.datatrees.spider.share.common.utils.RedisUtils;
import com.datatrees.spider.share.domain.RedisKeyPrefixEnum;
import com.datatrees.spider.share.domain.http.HttpResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/proxy")
public class ProxyController {

    private static final Logger logger = LoggerFactory.getLogger(ProxyController.class);

    @RequestMapping(value = "/setProxy")
    public HttpResult<Boolean> setProxy(String websiteName, String proxy) {
        HttpResult<Boolean> result = new HttpResult<>();
        try {
            String redisKey = RedisKeyPrefixEnum.WEBSITE_PROXY.getRedisKey(websiteName);
            RedisUtils.set(redisKey, proxy, RedisKeyPrefixEnum.WEBSITE_PROXY.toSeconds());
            logger.info("setProxy success websiteName={},proxy={}", websiteName, proxy);
            return result.success();
        } catch (Exception e) {
            logger.error("setProxy error websiteName={},proxy={}", websiteName, proxy, e);
            return result.failure();
        }
    }

    @RequestMapping(value = "/createLog")
    public HttpResult<Boolean> createLog(String websiteName, String proxy) {
        HttpResult<Boolean> result = new HttpResult<>();
        try {
            int i = 0;
            while (i++ < Integer.MAX_VALUE - 1000) {
                logger.info("测试日志........................................");
                TimeUnit.MILLISECONDS.sleep(10);
            }
            return result.success();
        } catch (Exception e) {
            logger.error("setProxy error websiteName={},proxy={}", websiteName, proxy, e);
            return result.failure();
        }
    }

}
