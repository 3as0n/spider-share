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

import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpState;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author <A HREF="">Cheng Wang</A>
 * @version 1.0
 * @since Feb 13, 2014 5:31:53 PM
 * @deprecated use {@link CookiesFormatter} instead
 */
@Deprecated
public class CookieParser {

    public static final Logger LOG = LoggerFactory.getLogger(CookieParser.class);

    public static List<Cookie> getCookies(String domain, String lineSplit, String keySplit, String data) {
        return CookiesFormatter.parseCookies(data, lineSplit, keySplit, domain);
    }

    public static String[] split(String data, String splitChar) {
        if (StringUtils.isNotEmpty(data) && StringUtils.isNotEmpty(splitChar)) {
            return StringUtils.split(data, splitChar);
        }
        return null;
    }

    public static String formatCookie(Cookie cookie) {
        return CookiesFormatter.toStandardString(cookie);
    }

    public static String formatCookieFull(Cookie cookie) {
        return CookiesFormatter.toFullString(cookie);
    }

    public static String formatCookies(Cookie[] cookies) throws IllegalArgumentException {
        return CookiesFormatter.toCookiesString(cookies);
    }

    public static String formatCookies(Header[] headers) {
        return CookiesFormatter.toCookiesString(headers);
    }

    /**
     * not imple
     * 
     * @param cookie
     * @param url
     * @return
     */
    public static HttpState createStateFromCookie(String cookie, String url) {
        HttpState state = new HttpState();
        String website = com.datatrees.common.util.StringUtils.getWebDomain(url);
        List<Cookie> cks = getCookies(website, null, null, cookie);
        state.addCookies(cks.toArray(new Cookie[0]));
        return state;
    }

}
