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

package com.datatrees.spider.share.domain;

/**
 * 任务状态 Created by zhouxinghai on 2017/4/25.
 */
public enum TaskStatusEnum {

    PROCESSING("PROCESSING", "处理中"),
    CRAWLER("CRAWLER", "进入爬取阶段"),
    PREPARE_FOR_CRAWL("PREPARE_FOR_CRAWL", "初始化完成"),
    REFRESH_LOGIN_RANDOMPASSWORD("REFRESH_LOGIN_RANDOMPASSWORD", "刷新登录短信验证码"),
    REFRESH_LOGIN_CODE("REFRESH_LOGIN_CODE", "刷新登录图片验证码"),
    REFRESH_LOGIN_QR_CODE("REFRESH_LOGIN_QR_CODE", "刷新登录二维码"),
    START_LOGIN("START_LOGIN", "开始登录"),
    WAITING_FOR_SMS_VERIFY("WAITING_FOR_SMS_VERIFY", "等待短信验证码"),
    WAITING_FOR_PICTURE_VERIFY("WAITING_FOR_PICTURE_VERIFY", "等待图片验证码"),
    WAITING_FOR_QR_VERIFY("WAITING_FOR_QR_VERIFY", "等待二维码"),
    WAITING_FOR_URL_VERIFY("WAITING_FOR_URL_VERIFY", "等待前端页面返回"),
    VERIFY_QR_SUCCESS("VERIFY_QR_SUCCESS", "二维码验证成功"),
    VERIFY_QR_FAILED("VERIFY_QR_FAILED", "二维码验证失败"),
    LOGIN_SUCCESS("LOGIN_SUCCESS", "登录成功"),
    LOGIN_FAILED("LOGIN_FAILED", "登录失败"),
    LOGIN_PROCESSING("LOGIN_PROCESSING", "登陆中"),
    CRAWLER_FAILED("CRAWLER_FAILED", "爬取失败"),
    CRAWLER_SUCCESS("CRAWLER_SUCCESS", "爬取成功"),
    TASK_TIMEOUT("TASK_TIMEOUT", "任务超时"),
    TASK_SUCCESS("TASK_SUCCESS", "任务成功"),
    TASK_FAIL("TASK_FAIL", "任务失败"),;

    /**
     * 状态码
     */
    private String code;

    /**
     * 描述
     */
    private String name;

    TaskStatusEnum(String statusCode, String remark) {
        this.code = statusCode;
        this.name = remark;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "TaskStatusEnum{" + "code='" + code + '\'' + ", name='" + name + '\'' + '}';
    }
}
