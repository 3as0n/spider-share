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

package com.datatrees.spider.share.domain;

/**
 * 二维码状态
 * 
 * @author zhouxinghai
 * @date 2018/1/17
 */
public final class QRStatus {

    /**
     * 等待用户扫描
     */
    public static final String WAITING = "WAITING";
    /**
     * 用户已扫描,等待确认
     */
    public static final String SCANNED = "SCANNED";
    /**
     * 用户已确认,等待认证
     */
    public static final String CONFIRMED = "CONFIRMED";
    /**
     * 二维码失效
     */
    public static final String EXPIRE = "EXPIRE";
    /**
     * 验证失败
     */
    public static final String FAILED = "FAILED";
    /**
     * 验证成功
     */
    public static final String SUCCESS = "SUCCESS";
    /**
     * 任务结束,爬取失败
     */
    public static final String CRAWLER_FAILED = "CRAWLER_FAILED";
    /**
     * 任务结束,爬取成功
     */
    public static final String CRAWLER_SUCCESS = "CRAWLER_SUCCESS";
    /**
     * 请求qq邮箱独立密码
     */
    public static final String REQUIRE_SECOND_PASSWORD = "REQUIRE_SECOND_PASSWORD";
    /**
     * 校验qq邮箱独立密码失败
     */
    public static final String VALIDATE_SECOND_PASSWORD_FAIL = "VALIDATE_SECOND_PASSWORD_FAIL";
    /**
     * 请求短信验证码
     */
    public static final String REQUIRE_SMS = "REQUIRE_SMS";
    /**
     * 已接收短信验证码
     */
    public static final String RECEIVED_SMS = "RECEIVED_SMS";
    /**
     * 校验短信验证码失败
     */
    public static final String VALIDATE_SMS_FAIL = "VALIDATE_SMS_FAIL";
    /**
     * 请求图片验证码
     */
    public static final String REQUIRE_PIC = "REQUIRE_PIC";
    /**
     * 已接收图片验证码
     */
    public static final String RECEIVED_PIC = "RECEIVED_PIC";
    /**
     * 校验图片验证码失败
     */
    public static final String VALIDATE_PIC_FAIL = "VALIDATE_PIC_FAIL";

    private QRStatus() {}
}
