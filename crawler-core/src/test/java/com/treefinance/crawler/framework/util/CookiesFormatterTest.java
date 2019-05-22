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

package com.treefinance.crawler.framework.util;

import com.treefinance.crawler.shaded.java.net.HttpCookie;
import com.treefinance.toolkit.util.Strings;
import org.junit.Test;

import java.util.Map;

/**
 * @author Jerry
 * @date 2019-05-22 19:22
 */
public class CookiesFormatterTest {

    @Test
    public void parse() {
        final HttpCookie cookie = CookiesFormatter.parse("cookieCheck=67438; Domain=login.taobao.com; Path=/", true);
        System.out.println(cookie);
    }

    @Test
    public void parseAsMap() {
        String cookies = "JSESSIONID=F3C3D4171369AC00D47452388811BFF3; Secure=; _ga=GA1.3.279016222.1555298836; _ga=GA1.4.279016222.1555298836; __utmz=39553075.1556597495.3.3.utmcsr=my.chsi.com.cn|utmccn=(referral)|utmcmd=referral|utmcct=/archive/index.jsp; acw_tc=2760822115584227030634571e99dfeda1d0d9a5401829931572409dd194ec; _gid=GA1.4.886904566.1558422708; aliyungf_tc=AQAAAHCRhjOtnQQA0mPgeuB0Rwfj+1QD; __utmc=39553075; _gid=GA1.3.2006206494.1558508867; __utma=39553075.279016222.1555298836.1558511541.1558516470.14; Secure=;HttpOnly;";

        Map<String, String> cookieMap = CookiesFormatter.parseAsMap(cookies);

        System.out.println(cookieMap);

        String[] cookieArray = CookiesFormatter.toCookieArray(cookieMap, false);
        System.out.println(Strings.join(cookieArray, ";"));

        cookieMap = CookiesFormatter.parseAsMap(cookieArray, false);

        System.out.println(cookieMap);

        cookieArray = CookiesFormatter.parseAsArray(cookieArray, false);

        System.out.println(Strings.join(cookieArray, ";"));
    }
}