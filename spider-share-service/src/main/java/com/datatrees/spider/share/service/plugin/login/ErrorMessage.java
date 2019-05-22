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

package com.datatrees.spider.share.service.plugin.login;

public interface ErrorMessage {

    String DEFAULT_ERROR = "运营商正在升级维护，请稍后重试。";
    String INFO_NOT_ENOUGTH_ERROR = "信息不完整，请按要求完成输入。";
    String NOT_CONFORM_ERROR = "您的账号存在异常，请登录运营商官网进行验证确认。";
    String SERVER_INTERNAL_ERROR = "系统内部错误，请稍后重试。";
    String VAILD_CODE_ERROR = "验证码错误，请输入正确的验证码。";
    String REGION_CHECK_ERROR = "手机号码与运营商归属地不符，请重新输入。";
    String UNKNOWN_ERROR = "未知异常，请稍后重试。";
    String LOGIN_DEFAULT_ERROR = "登录失败，请重试";
    String USER_PASSWORD_ERROR = "用户名或密码错误，请重新输入。";
    String MAIL_DEFAULT_ERROR = "邮箱正在升级维护，请稍后重试。";
}
