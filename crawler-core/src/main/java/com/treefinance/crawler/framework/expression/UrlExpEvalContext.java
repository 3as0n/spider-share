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

package com.treefinance.crawler.framework.expression;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author Jerry
 * @since 14:29 2018/5/30
 */
public class UrlExpEvalContext extends ExpEvalContext {

    private final List<String> urlEncodedKeys;
    private final String charset;

    public UrlExpEvalContext(Map<String, Object> placeholderMapping, List<String> urlEncodedKeys, String charset) {
        this(placeholderMapping, true, false, urlEncodedKeys, charset);
    }

    public UrlExpEvalContext(Map<String, Object> placeholderMapping, boolean failOnUnknown, boolean allowNull, List<String> urlEncodedKeys, String charset) {
        super(placeholderMapping, failOnUnknown, allowNull);
        this.urlEncodedKeys = urlEncodedKeys == null ? Collections.emptyList() : urlEncodedKeys;
        this.charset = charset;
    }

    public List<String> getUrlEncodedKeys() {
        return Collections.unmodifiableList(urlEncodedKeys);
    }

    public String getCharset() {
        return charset;
    }

    public boolean needUrlEncode(String key) {
        return urlEncodedKeys.contains(key);
    }
}
