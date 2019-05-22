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

public enum TopicTag {

    LOGIN_INFO("login_info", "登陆准备"),
    OPERATOR_CRAWLER_START("operator_crawler_start", "运营商爬虫"),
    OPERATOR_LOGIN_POST("operator_login_post", "运营商登陆后"),
    TASK_INIT("task_init", "task初始化"),
    TASK_COMPLETE("task_complete", "task完成"),
    METHOD_USE_TIME("method_monitor", "接口耗时"),
    CALLBACK_INFO("callback_info", "回调信息"),
    TASK_CHECK_RESULT("task_check_result", "检查数据"),
    WEBSITE_STATISTICS("website_statistics", "站点统计"),
    PUSH_MAIL("push_mail", "邮件推送统计"),
    TASK_LOG("task_log", "task日志"),;

    private String tag;

    private String remark;

    TopicTag(String tag, String remark) {
        this.tag = tag;
        this.remark = remark;
    }

    public String getTag() {
        return tag;
    }

    public String getRemark() {
        return remark;
    }
}
