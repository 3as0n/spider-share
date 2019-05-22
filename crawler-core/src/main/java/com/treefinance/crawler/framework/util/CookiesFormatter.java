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
import com.treefinance.toolkit.util.http.HttpHeaders;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.Header;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Jerry
 * @date 2019-05-22 11:47
 */
public final class CookiesFormatter {
    private static final Logger LOGGER = LoggerFactory.getLogger(CookiesFormatter.class);
    private static final String SEPARATOR = "=";
    private static final String DELIMITER = ";";
    private static final String PATH = "/";
    private static final int MAX_AGE = 1577808000;
    private static final boolean SECURE = false;

    private CookiesFormatter() {}

    public static List<HttpCookie> parseList(String header, boolean retainQuote) {
        final String head = StringUtils.trim(header);
        if (StringUtils.isEmpty(head)) {
            return Collections.emptyList();
        }
        return HttpCookie.parse(head, retainQuote);
    }

    public static HttpCookie parse(String cookie, boolean retainQuote) {
        final List<HttpCookie> httpCookies = parseList(cookie, retainQuote);
        if (httpCookies.isEmpty()) {
            return null;
        }
        return httpCookies.get(0);
    }

    /**
     * parse cookies string as a map like {"key1":"value1","key2":"value2"} and removed the expired cookie;
     */
    public static Map<String, String> parseAsMap(String cookies, boolean retainQuote) {
        if (StringUtils.isEmpty(cookies)) {
            return Collections.emptyMap();
        }

        final String[] cookieArray = cookies.split(DELIMITER);

        return parseAsMap(cookieArray, retainQuote);
    }

    public static Map<String, String> parseAsMap(String cookies) {
        return parseAsMap(cookies, false);
    }

    /**
     * parse cookies array as a map like {"key1":"value1","key2":"value2"} and removed the expired cookie;
     */
    public static Map<String, String> parseAsMap(String[] cookies, boolean retainQuote) {
        if (ArrayUtils.isEmpty(cookies)) {
            return Collections.emptyMap();
        }

        Map<String, String> cookieMap = new HashMap<>(cookies.length);
        for (String cookie : cookies) {
            try {
                HttpCookie httpCookie = parse(cookie, retainQuote);
                LOGGER.debug("parse cookie string: {} >> {}", cookie, httpCookie);

                if (httpCookie == null) {
                    continue;
                }

                if (!httpCookie.hasExpired()) {
                    cookieMap.put(httpCookie.getName(), httpCookie.getValue());
                } else {
                    LOGGER.debug("cookie has expired! >> {}", httpCookie);
                }
            } catch (Exception e) {
                LOGGER.error("Error parsing cookie string! >> {}", cookie, e);
            }
        }

        return cookieMap;
    }

    /**
     * parse cookies string as a normal cookie array like ["key1"="value1","key2"="value2"] and removed the expired
     * cookie;
     */
    public static String[] parseAsArray(String cookies, boolean retainQuote) {
        if (StringUtils.isEmpty(cookies)) {
            return ArrayUtils.EMPTY_STRING_ARRAY;
        }

        final String[] cookieArray = cookies.split(DELIMITER);

        return parseAsArray(cookieArray, retainQuote);
    }

    /**
     * parse cookies array as a normal cookie array like ["key1"="value1","key2"="value2"] and removed the expired
     * cookie;
     */
    public static String[] parseAsArray(String[] cookies, boolean retainQuote) {
        if (ArrayUtils.isEmpty(cookies)) {
            return ArrayUtils.EMPTY_STRING_ARRAY;
        }

        List<String> cookieList = new ArrayList<>(cookies.length);
        for (String cookie : cookies) {
            try {
                HttpCookie httpCookie = parse(cookie, retainQuote);
                LOGGER.debug("parse cookie string: {} >> {}", cookie, httpCookie);
                if (httpCookie == null) {
                    continue;
                }

                if (!httpCookie.hasExpired()) {
                    cookieList.add(httpCookie.getName() + SEPARATOR + httpCookie.getValue());
                } else {
                    LOGGER.debug("cookie has expired! >> {}", httpCookie);
                }
            } catch (Exception e) {
                LOGGER.error("Error parsing cookie string! >> {}", cookie, e);
            }
        }

        return cookieList.toArray(new String[0]);
    }

    /**
     * convert cookies string to a normal cookies string like "key1=value1;key2=value2" and removed the expired cookie;
     */
    public static String toStandardString(String cookies, boolean retainQuote) {
        final Map<String, String> map = parseAsMap(cookies, retainQuote);

        return toCookiesString(map);
    }

    public static String toCookiesString(String[] cookies, boolean retainQuote) {
        final Map<String, String> cookiesMap = parseAsMap(cookies, retainQuote);

        return toCookiesString(cookiesMap);
    }

    public static String toCookiesString(Map<String, String> cookies) {
        if (MapUtils.isEmpty(cookies)) {
            return StringUtils.EMPTY;
        }

        return cookies.entrySet().stream().map(entry -> entry.getKey() + SEPARATOR + StringUtils.defaultString(entry.getValue())).collect(Collectors.joining(DELIMITER));
    }

    public static String[] toCookieArray(Map<String, String> cookiesMap, boolean retainQuote) {
        if (MapUtils.isEmpty(cookiesMap)) {
            return ArrayUtils.EMPTY_STRING_ARRAY;
        }

        return cookiesMap.entrySet().stream().map(entry -> {
            String cookie = entry.getKey() + SEPARATOR + StringUtils.defaultString(entry.getValue());
            try {
                HttpCookie httpCookie = parse(cookie, retainQuote);
                LOGGER.debug("parse cookie string: {} >> {}", cookie, httpCookie);
                if (httpCookie != null) {
                    if (!httpCookie.hasExpired()) {
                        return httpCookie.getName() + SEPARATOR + httpCookie.getValue();
                    } else {
                        LOGGER.debug("cookie has expired! >> {}", httpCookie);
                    }
                }
            } catch (Exception e) {
                LOGGER.error("Error parsing cookie string! >> {}", cookie, e);
            }
            return null;
        }).filter(Objects::nonNull).toArray(String[]::new);
    }

    public static String join(String... cookies) {
        if (ArrayUtils.isEmpty(cookies)) {
            return StringUtils.EMPTY;
        }

        return String.join(DELIMITER, cookies);
    }

    public static String toFullString(Cookie cookie) {
        if (cookie == null) {
            return StringUtils.EMPTY;
        }

        StringBuilder buf = new StringBuilder();
        buf.append(cookie.getName()).append(SEPARATOR).append(StringUtils.defaultString(cookie.getValue()));
        if (cookie.getDomain() != null) {
            buf.append("; domain=").append(cookie.getDomain());
        }
        if (cookie.getPath() != null) {
            buf.append("; path=").append(cookie.getPath());
        }
        return buf.toString();
    }

    public static String toStandardString(Cookie cookie) {
        if (cookie == null) {
            return StringUtils.EMPTY;
        }

        return cookie.getName() + SEPARATOR + StringUtils.defaultString(cookie.getValue());
    }

    public static String toCookiesString(Cookie[] cookies) {
        if (ArrayUtils.isEmpty(cookies)) {
            return StringUtils.EMPTY;
        }

        return Arrays.stream(cookies).map(CookiesFormatter::toStandardString).filter(str -> !str.isEmpty()).collect(Collectors.joining(DELIMITER));
    }

    public static String toCookiesString(Header[] headers) {
        if (ArrayUtils.isEmpty(headers)) {
            return StringUtils.EMPTY;
        }

        return Arrays.stream(headers).filter(header -> HttpHeaders.COOKIE.equals(header.getName())).map(Header::getValue).collect(Collectors.joining(DELIMITER));
    }

    public static List<Cookie> parseCookies(String cookies, String domain) {
        return parseCookies(cookies, null, null, domain);
    }

    public static List<Cookie> parseCookies(String cookies, String itemDelimiter, String pairSeparator, String domain) {
        if (StringUtils.isEmpty(cookies)) {
            return Collections.emptyList();
        }

        String delimiter = StringUtils.defaultIfEmpty(itemDelimiter, DELIMITER);
        String[] pairs = cookies.split(delimiter);
        if (ArrayUtils.isEmpty(pairs)) {
            return Collections.emptyList();
        }

        String separator = StringUtils.defaultIfEmpty(pairSeparator, SEPARATOR);
        List<Cookie> cookieList = new ArrayList<>();
        for (String pair : pairs) {
            final int i = pair.indexOf(separator);
            if (i != -1) {
                String name = pair.substring(0, i).trim();
                if (name.isEmpty()) {
                    continue;
                }

                String value = pair.substring(i + 1).trim();
                cookieList.add(new Cookie(domain, name, value, PATH, MAX_AGE, SECURE));
            }
        }
        return cookieList;
    }

}
