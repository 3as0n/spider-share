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

package com.datatrees.spider.share.domain.http;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * @author zhouxinghai
 * @date 2017/7/19.
 */
public class Cookie implements Serializable {

    @JSONField(ordinal = 1)
    private String name;

    @JSONField(ordinal = 2)
    private String value;

    @JSONField(ordinal = 3)
    private String domain;

    @JSONField(ordinal = 4)
    private int version;

    @JSONField(ordinal = 5)
    private String path;

    @JSONField(ordinal = 6)
    private boolean secure;

    @JSONField(ordinal = 7)
    private Date expiryDate;

    @JSONField(ordinal = 8)
    private Map<String, String> attribs = new HashMap<>();

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isSecure() {
        return secure;
    }

    public void setSecure(boolean secure) {
        this.secure = secure;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Map<String, String> getAttribs() {
        return attribs;
    }

    public void setAttribs(Map<String, String> attribs) {
        this.attribs = attribs;
    }

    public void putAttr(String key, String value) {
        if (attribs == null) {
            attribs = new HashMap<>(8);
        }
        attribs.put(key, value);
    }

    public void foreachAttribs(BiConsumer<String, String> consumer) {
        if (attribs != null) {
            attribs.forEach(consumer);
        }
    }
}
