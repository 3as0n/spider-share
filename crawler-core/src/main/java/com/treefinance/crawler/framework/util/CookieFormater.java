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

import org.apache.commons.collections4.MapUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author <A HREF="">Cheng Wang</A>
 * @version 1.0
 * @since Jul 29, 2014 1:51:32 PM
 * @deprecated use {@link CookiesFormatter} instead
 */
@Deprecated
public enum CookieFormater {
    /**
     * 单例
     */
    INSTANCE;

    public String listToString(Map<String, String> cookies) {
        return CookiesFormatter.toCookiesString(cookies);
    }

    public String listToString(String[] cookies) {
        return CookiesFormatter.join(cookies);
    }

    public String parserCookie(String[] cookieVals) {
        return parserCookie(cookieVals, false);
    }

    public String parserCookie(String[] cookieVals, boolean retainQuote) {
        return CookiesFormatter.toCookiesString(cookieVals, retainQuote);
    }

    public Map<String, String> parserCookietToMap(String[] cookieVals) {
        return parserCookietToMap(cookieVals, false);
    }

    public Map<String, String> parserCookietToMap(String[] cookieVals, boolean retainQuote) {
        final Map<String, String> map = CookiesFormatter.parseAsMap(cookieVals, retainQuote);
        return MapUtils.isEmpty(map) ? new HashMap<>() : map;
    }

    public String parserCookie(String cookieVals, boolean retainQuote) {
        return CookiesFormatter.toStandardString(cookieVals, retainQuote);
    }

    public Map<String, String> parserCookieToMap(String cookieVals) {
        return parserCookieToMap(cookieVals, false);
    }

    public Map<String, String> parserCookieToMap(String cookieVals, boolean retainQuote) {
        final Map<String, String> map = CookiesFormatter.parseAsMap(cookieVals, retainQuote);
        return MapUtils.isEmpty(map) ? new HashMap<>() : map;
    }

    public String[] parserCookieToArray(String cookieVals, boolean retainQuote) {
        return CookiesFormatter.parseAsArray(cookieVals, retainQuote);
    }

    public String[] parserCookieToArray(String[] cookieVals, boolean retainQuote) {
        return CookiesFormatter.parseAsArray(cookieVals, retainQuote);
    }

}
