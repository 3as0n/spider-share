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

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhouxinghai on 2017/7/20.
 */
public class FormType {

    public static final String LOGIN = "LOGIN";// 登陆表单

    public static final String LOGIN_POST = "LOGIN_POST";// 登陆后处理

    public static final String VALIDATE_BILL_DETAIL = "VALIDATE_BILL_DETAIL";// 验证详单

    public static final String VALIDATE_USER_INFO = "VALIDATE_USER_INFO";// 验证个人信息

    public static final String REGISTER = "REGISTER";// 注册

    private static final Map<String, String> allTypes = new HashMap<>();

    static {
        allTypes.put(LOGIN, "登录");
        allTypes.put(VALIDATE_BILL_DETAIL, "详单");
        allTypes.put(VALIDATE_USER_INFO, "个人信息");
        allTypes.put(REGISTER, "注册");
    }

    public static String getName(String type) {
        if (allTypes.containsKey(type)) {
            return allTypes.get(type);
        }
        return "自定义插件" + type;
    }

    public boolean validate(String type) {
        return allTypes.containsKey(type);
    }

}
