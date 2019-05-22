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

package com.treefinance.crawler.shaded.java.net;

import org.junit.Test;

import java.util.List;

/**
 * @author Jerry
 * @date 2019-05-22 20:09
 */
public class HttpCookieTest {

    @Test
    public void parse() {

        final List<HttpCookie> cookies = HttpCookie.parse("cookieCheck=67438; Domain=login.taobao.com; Path=/", true);
        System.out.println(cookies);
    }
}