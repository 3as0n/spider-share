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

package com.datatrees.spider.share.domain.http;

/**
 * Created by zhouxinghai on 2017/7/17.
 */
public class HttpHeadKey {

    public static final String REFERER = "Referer"; // 引用页

    public static final String CONTENT_TYPE = "Content-Type"; // 数据类型

    public static final String CONNECTION = "Connection"; // 数据类型close,Keep-Alive

    public static final String USER_AGENT = "User-Agent"; // 数据类型close,Keep-Alive

    public static final String X_REQUESTED_WITH = "X-Requested-With";// ajax异步请求

    public static final String SET_COOKIE = "Set-Cookie";// 设置cookie

    public static final String LOCATION = "Location";// 重定向
}
