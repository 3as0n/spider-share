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

package com.datatrees.spider.share.domain.directive;

/**
 * redis 交互指令任务状态 Created by zhouxinghai on 2017/5/19.
 */
public class DirectiveRedisCode {

    public static final String WAIT_APP_DATA = "WAIT_APP_DATA"; // 指令已发出,等待前端处理返回数据

    public static final String WAIT_SERVER_PROCESS = "WAIT_SERVER_PROCESS"; // 前端处理完成,数据已经保存到Redis,等待后端处理

    public static final String SERVER_FAIL = "SERVER_FAIL"; // 后端数据处理失败

    public static final String SERVER_SUCCESS = "SERVER_SUCCESS"; // 后端数据处理成功

    public static final String CANCEL = "CANCEL"; // 取消

    public static final String SKIP = "SKIP"; // 跳过二维码

    public static final String REFRESH_LOGIN_RANDOMPASSWORD = "REFRESH_LOGIN_RANDOMPASSWORD";// 登陆时,发送短信验证码到手机

    public static final String REFRESH_LOGIN_CODE = "REFRESH_LOGIN_CODE"; // 登陆时,刷新图片验证码

    public static final String REFRESH_LOGIN_QR_CODE = "REFRESH_LOGIN_QR_CODE"; // 登陆时,刷新二维码

    public static final String WAITTING = "WAITTING"; // 等待

    public static final String SCANNED = "SCANNED"; // 已经扫码二维码,等待用户确认

    public static final String FAILED = "FAILED"; // 失败

    public static final String SUCCESS = "SUCCESS"; // 成功

    public static final String START_LOGIN = "START_LOGIN"; // 开始登录
}