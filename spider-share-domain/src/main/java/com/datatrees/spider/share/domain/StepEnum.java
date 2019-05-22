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

public enum StepEnum {

    REC_INIT_MSG(100, "收到消息"),
    INIT(110, "初始化"),
    INIT_SUCCESS(120, "初始化成功"),
    LOGIN_SUCCESS(300, "登陆成功"),
    VALIDATE_BILL_DETAIL_SUCCESS(400, "详单校验成功"),
    DEFINE_PROCESS_SUCCESS(600, "自定义处理成功"),
    CRAWLER_SUCCESS(700, "抓取成功"),
    PROCESS_SUCCESS(800, "洗数成功"),
    CHECK_SUCCESS(900, "数据校验成功"),
    CALLBACK_SUCCESS(1000, "回调成功"),
    CALLBACK_FAIL(4000, "回调失败"),
    PROCESS_FAIL(4100, "洗数失败"),
    CHECK_FAIL(4200, "数据校验失败"),
    CRAWLER_FAIL(4300, "爬取失败"),
    USER_CANCEL(4430, "用户取消任务"),
    SYSTEM_CANCEL(4420, "网关取消任务"),
    CRAWLER_CANCEL(4410, "爬虫取消任务"),
    DEFINE_PROCESS_FAIL(4600, "自定义处理失败"),
    VALIDATE_BILL_DETAIL_FAIL(4700, "详单校验失败"),
    LOGIN_FAIL(4800, "登陆失败"),
    INIT_FAIL(4900, "初始化失败");

    /**
     * 步骤代码
     */
    private int stepCode;

    /**
     * 名称
     */
    private String stepName;

    StepEnum(int stepCode, String stepName) {
        this.stepCode = stepCode;
        this.stepName = stepName;
    }

    public int getStepCode() {
        return stepCode;
    }

    public String getStepName() {
        return stepName;
    }

}
