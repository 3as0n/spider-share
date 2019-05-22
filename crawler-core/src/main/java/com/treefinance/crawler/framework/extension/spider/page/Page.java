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

package com.treefinance.crawler.framework.extension.spider.page;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Jerry
 * @since 14:41 27/11/2017
 */
public class Page implements Serializable {

    private final String url;

    private final String content;

    private final Map<String, Object> attributes = new HashMap<>();

    private Map<String, Object> extra;

    public Page(String url, String content) {
        this(url, content, null);
    }

    public Page(String url, String content, Map<String, Object> extra) {
        this.url = StringUtils.defaultString(url);
        this.content = StringUtils.defaultString(content);
        this.extra = extra;
    }

    public String getUrl() {
        return url;
    }

    public String getContent() {
        return content;
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public void addAttributes(Map<String, Object> fields) {
        getAttributes().putAll(fields);
    }

    public void addAttribute(String key, Object value) {
        getAttributes().put(key, value);
    }

    public Object removeAttribute(String key) {
        return getAttributes().remove(key);
    }

    public void clearAttributes() {
        getAttributes().clear();
    }

    public Map<String, Object> getExtra() {
        if (extra == null) {
            extra = new HashMap<>();
        }
        return extra;
    }

    public void setExtra(Map<String, Object> extra) {
        this.extra = extra;
    }

    public void addExtra(String key, Object value) {
        getExtra().put(key, value);
    }

    public void clearExtra() {
        getExtra().clear();
    }
}
