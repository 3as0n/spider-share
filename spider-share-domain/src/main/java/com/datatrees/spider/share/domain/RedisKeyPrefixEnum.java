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

import java.util.concurrent.TimeUnit;

/**
 * Created by zhouxinghai on 2017/7/5.
 */
public enum RedisKeyPrefixEnum {

    WEBSITENAME_TRANSFORM_MAP("websitename.transform.map", 60, TimeUnit.MINUTES, "websitename中文转英文名称"),
    LOCK("lock", 5, TimeUnit.SECONDS, "共享锁"),
    TASK_COOKIE("task.cookie", 30, TimeUnit.MINUTES, "根据taskId共享cookie"),
    TASK_WEBSITE("task.website", 30, TimeUnit.MINUTES, "根据taskId保存website"),
    TASK_RUN_COUNT("task.run.count", 30, TimeUnit.MINUTES, "根据taskId运行次数"),
    TASK_SHARE("task.spider.share", 30, TimeUnit.MINUTES, "根据taskId共享中间属性"),
    TASK_IP_LOCALE("task.ip.locale", 30, TimeUnit.MINUTES, "用户手机号归属地信息"),
    TASK_PROXY("task.proxy", 30, TimeUnit.MINUTES, "根据taskId共享代理"),
    TASK_PROXY_ENABLE("task.proxy.enable", 30, TimeUnit.MINUTES, "根据taskId共享代理"),
    TASK_SMS_CODE("task.sms.code", 30, TimeUnit.MINUTES, "根据taskId共享短信验证码"),
    TASK_PIC_CODE("task.pic.code", 30, TimeUnit.MINUTES, "根据taskId共享短信验证码"),
    TASK_RESULT("task.result", 10, TimeUnit.MINUTES, "task爬取结果"),
    TASK_CONTEXT("task.context", 10, TimeUnit.MINUTES, "task context"),
    TASK_REQUEST("task.request", 10, TimeUnit.MINUTES, "根据taskId共享代理"),
    TASK_PAGE_CONTENT("task.page.content", 10, TimeUnit.MINUTES, "请求页面"),
    TASK_METHOD_USE_TIME("task.method.use.time", 15, TimeUnit.MINUTES, "任务接口耗时"),
    MAX_WEIGHT_OPERATOR("max.weight.operator", 30, TimeUnit.DAYS, "最大权重运营商"),
    WEBSITE_LAST_INFO("website.last.info", 365, TimeUnit.DAYS, "站点最近记录"),
    WEBSITE_PLUGIN_FILE_NAME("website.plugin.file.name", 60, TimeUnit.MINUTES, "为website制定jar"),
    WEBSITE_PROXY("website.proxy", 1, TimeUnit.HOURS, "运营商指定代理"),
    WEBSITE_MONITOR_ID("website.monitor.id", 3, TimeUnit.DAYS, "website_monitor主键"),
    WEBSITE_DAY_LIST("website.day.list", 3, TimeUnit.DAYS, "站点信息统计"),
    START_TIMESTAMP("start.timestamp", 1, TimeUnit.HOURS, " 开始时间"),
    FINISH_TIMESTAMP("finish.timestamp", 1, TimeUnit.HOURS, " 完成时间"),
    SUBMIT_RESULT("submit.result", 15, TimeUnit.MINUTES, "校验结果"),
    PLUGIN_VERSION("plugin.version", 365, TimeUnit.DAYS, "插件版本"),
    GROUP_LAST_INFO("group.last.info", 365, TimeUnit.DAYS, "分组信息"),
    LOGIN_RESULT("login.result", 1, TimeUnit.DAYS, "登陆结果"),
    LOGIN_INIT("login.init", 1, TimeUnit.DAYS, "登陆初始化"),
    PLUGIN_DATA("plugin.data", 365, TimeUnit.DAYS, "插件版本"),
    PROCESS_START_TIME("process.start.time", 1, TimeUnit.HOURS, "处理开始时间"),
    PROCESS_EXPIRE("process.expire", 1, TimeUnit.HOURS, "process超时时间"),
    PROCESS_END_TIME("process.end.time", 1, TimeUnit.HOURS, "处理开始时间"),
    NICK_GROUP_LAST_INFO("nick.group.last.info", 365, TimeUnit.DAYS, "归属地信息"),
    WEBSITE_GROUP_LAST_INFO("website.group.last.info", 365, TimeUnit.DAYS, "站点归属地信息"),
    NICK_GROUP_DAY_LIST("nick.group.day.list", 3, TimeUnit.DAYS, "归属地信息统计"),
    WEBSITE_GROUP_DAY_LIST("website.group.day.list", 3, TimeUnit.DAYS, "站点归属地信息统计"),
    WEBSITE_GROUP_MONITOR_ID("website.group.monitor.id", 3, TimeUnit.DAYS, "website_group_monitor主键"),
    NICK_GROUP_MONITOR_ID("nick.group.monitor.id", 3, TimeUnit.DAYS, "group_monitor主键"),
    TASK_INIT_NICK_GROUP_CODE("task.init.nick.group.code", 30, TimeUnit.MINUTES, "任务初始化时的groupCode"),
    TASK_INFO_ACCOUNT_NO("task.info.account.no", 30, TimeUnit.MINUTES, "任务对应的accountNo"),
    APP_CRAWLER_CONFIG("app.crawler.config", 1, TimeUnit.MILLISECONDS, "用户爬取模块配置");

    /**
     * 备注
     */
    private final String remark;

    /**
     * 分隔符
     */
    private final String separator = ".";

    /**
     * 前缀
     */
    private String prefix;

    /**
     * 超时时间
     */
    private int timeout;

    /**
     * 时间单位
     */
    private TimeUnit timeUnit;

    RedisKeyPrefixEnum(String prefix, int timeout, TimeUnit timeUnit, String remark) {
        this.prefix = prefix;
        this.remark = remark;
        this.timeout = timeout;
        this.timeUnit = timeUnit;
    }

    public static void main(String[] args) {
        System.out.println(RedisKeyPrefixEnum.TASK_COOKIE.getRedisKey(1, 2, 3, 4, 5));
    }

    public int getTimeout() {
        return timeout;
    }

    public String getRedisKey() {
        return prefix;
    }

    public String getRedisKey(Object postfix) {
        return prefix + separator + postfix.toString();
    }

    public String getRedisKey(Object... postfix) {
        StringBuffer sb = new StringBuffer(prefix);
        for (Object o : postfix) {
            sb.append(separator).append(o.toString());
        }
        return sb.toString();
    }

    public String getPrefix() {
        return prefix;
    }

    public String getRemark() {
        return remark;
    }

    public TimeUnit getTimeUnit() {
        return timeUnit;
    }

    public String parsePostfix(String key) {
        return key.substring(prefix.length() + separator.length());
    }

    public int toSeconds() {
        long l = timeUnit.toSeconds(timeout);
        return l == 0 ? 1 : (int)l;
    }
}
