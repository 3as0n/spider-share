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
 * 消息topic Created by zhouxinghai on 2017/4/25.
 */
public enum TopicEnum {

    SPIDER_OPERATOR("spider_operator", "爬虫运营商"),
    SPIDER_ECOMMERCE("spider_ecommerce", "爬虫电商"),
    SPIDER_BANK("spider_bank", "爬虫账单"),
    SPIDER_EXTRA("spider_extra", "爬虫其他"),
    TASK_NEXT_DIRECTIVE("task_next_directive", "交互指令"),
    CRAWLER_MONITOR("crawler_monitor", " 爬虫监控"),
    TASK_LOG("task_log", "任务状态日志"),
    // RAWDATA_INPUT("rawData_input", "登录成功消息"),
    ;

    /**
     * topic代码
     */
    private String code;

    /**
     * 描述
     */
    private String name;

    TopicEnum(String topic, String remark) {
        this.code = topic;
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
        return "TopicEnum{" + "code='" + code + '\'' + ", name='" + name + '\'' + '}';
    }
}
