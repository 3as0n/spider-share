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

package com.datatrees.spider.share.service.plugin.login;

import com.treefinance.crawler.framework.context.function.LinkNode;
import org.apache.commons.lang.StringUtils;

import java.util.Map;

public abstract class Abstract189LoginPlugin extends AbstractLoginPlugin {

    // check phone region
    protected String validPhoneRegion(String mobile) {
        String validPhoneUrl = "http://login.189.cn/login/ajax\"m=checkphone&phone=" + mobile;
        LinkNode validPhoneNode = new LinkNode(validPhoneUrl);
        validPhoneNode.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        return (String)getResponseByWebRequest(validPhoneNode, ContentType.Content);
    }

    // set 189 website login message
    protected void set189ErrorMessage(String resultCode, Map<String, Object> resultMap) {
        if (resultCode != null) {
            if (resultCode.equals("9103") || resultCode.equals("9999")) {
                resultMap.put("errorCode", "您的密码错误");
            } else if (resultCode.equals("8105")) {
                resultMap.put("errorCode", "密码过于简单,请重置");
            } else if (resultCode.equals("9111")) {
                resultMap.put("errorCode", "登录失败过多，帐号已被锁定");
            } else if (resultCode.equals("9100")) {
                resultMap.put("errorCode", "该账户不存在");
            } else if (resultCode.equals("6113")) {
                resultMap.put("errorCode", "系统繁忙，稍后重试");
            } else if (StringUtils.isNotBlank(resultCode)) {
                resultMap.put("errorCode", ErrorMessage.DEFAULT_ERROR);
            }
        }

    }

    // 189 login
    protected String login189Website(String username, String password, String uType, String provinceId) {
        String requestUrl = "http://login.189.cn/login\"Account=" + username + "&UType=" + uType + "&ProvinceID=" + provinceId + "&AreaCode=&CityNo=&RandomFlag=0&Password="
            + password + "&Captcha=";
        LinkNode loginPage = new LinkNode(requestUrl);
        loginPage.setReferer("http://login.189.cn/login");
        return (String)getResponseByWebRequest(loginPage, ContentType.Content);
    }
}
