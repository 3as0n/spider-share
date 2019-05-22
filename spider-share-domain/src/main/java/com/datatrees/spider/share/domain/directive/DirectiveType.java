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

package com.datatrees.spider.share.domain.directive;

/**
 * 交互指令类型 Created by zhouxinghai on 2017/5/22.
 */
public class DirectiveType {

    /**
     * 将后端失败率高的页面交给APP抓取
     */
    public static final String GRAB_URL = "grab_url";

    /**
     * 模拟登陆
     */
    public static final String PLUGIN_LOGIN = "plugin_login";

    /**
     * 登录过程中,二次密码，例：QQ邮箱独立密码
     */
    public static final String LOGIN_SECOND_PASSWORD = "login_second_password";

    /**
     * 抓取过程中,图片验证码
     */
    public static final String CRAWL_CODE = "crawl_code";

    /**
     * 抓取过程中,短信验证码
     */
    public static final String CRAWL_SMS = "crawl_sms";

    /**
     * 抓取过程中,二维码
     */
    public static final String CRAWL_QR = "crawl_qr";

    /**
     * 取消任务
     */
    public static final String CANCEL = "cancel";
}
