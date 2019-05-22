/*
 * Copyright © 2015 - 2018 杭州大树网络技术有限公司. All Rights Reserved
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.treefinance.crawler.framework.context;

import com.treefinance.crawler.framework.util.CookiesFormatter;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author Jerry
 * @since 00:50 2018/6/19
 */
public class DefaultCookieStore implements CookieStore {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultCookieStore.class);

    private String cookies;

    private Map<String, String> store;

    private boolean retainQuote = false;

    public DefaultCookieStore() {
    }

    public DefaultCookieStore(CookieStore cookieStore) {
        this.cookies = cookieStore.getCookiesAsString();
        this.store = new HashMap<>(cookieStore.getCookiesAsMap());
    }

    @Override
    public String getCookiesAsString() {
        if (this.cookies == null && this.store != null) {
            this.cookies = CookiesFormatter.toCookiesString(this.store);
        }

        return this.cookies;
    }

    @Override
    public Map<String, String> getCookiesAsMap() {
        if (this.store == null && StringUtils.isNotEmpty(this.cookies)) {
            this.store = CookiesFormatter.parseAsMap(this.cookies, this.retainQuote);
        }

        return this.store == null ? Collections.emptyMap() : Collections.unmodifiableMap(this.store);
    }

    @Override
    public void setCookies(String cookies) {
        this.cookies = StringUtils.trimToEmpty(cookies);
        this.store = null;
    }

    @Override
    public void setCookies(@Nullable String cookies, boolean retainQuote) {
        this.cookies = StringUtils.trimToEmpty(cookies);
        this.store = null;
        this.retainQuote = retainQuote;
    }

    @Override
    public void setCookies(Map<String, String> cookies) {
        this.store = cookies;
        this.cookies = null;
    }

    @Override
    public void addCookies(Map<String, String> cookies) {
        if (MapUtils.isEmpty(cookies)) {
            LOGGER.warn("There was no cookies to append into process context!");
            return;
        }

        if (this.store == null) {
            this.store = new HashMap<>(cookies);
        } else {
            this.store.putAll(cookies);
        }

        this.cookies = CookiesFormatter.toCookiesString(this.store);
    }

    @Override
    public void addCookies(@Nullable String[] cookies, boolean retainQuote) {
        Map<String, String> map = CookiesFormatter.parseAsMap(cookies, retainQuote);

        addCookies(map);
    }

    @Override
    public void clearCookies() {
        this.cookies = null;
        this.store = null;
    }

    @Override
    public void copyCookies(@Nonnull CookieStore cookieStore) {
        Objects.requireNonNull(cookieStore);

        this.cookies = cookieStore.getCookiesAsString();
        this.store = new HashMap<>(cookieStore.getCookiesAsMap());
        this.retainQuote = cookieStore.isRetainQuote();
    }

    @Override
    public boolean isRetainQuote() {
        return retainQuote;
    }
}
