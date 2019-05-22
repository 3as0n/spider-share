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

package com.treefinance.crawler.framework.context.function;

import org.apache.commons.collections4.MapUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Jerry
 * @since 01:13 2018/7/25
 */
public class GlobalScope {

    public static final GlobalScope DEFAULT = new GlobalScope(null, null, null);
    private final Map<String, Object> localScope;
    private final Map<String, Object> requestScope;
    private final Map<String, Object> contextScope;

    public GlobalScope(Map<String, Object> localScope, Map<String, Object> requestScope, Map<String, Object> contextScope) {
        this.localScope = localScope;
        this.requestScope = requestScope;
        this.contextScope = contextScope;
    }

    public Map<String, Object> getRequestScope() {
        return requestScope;
    }

    public Map<String, Object> getContextScope() {
        return contextScope;
    }

    public Map<String, Object> asMap() {
        Map<String, Object> scope = null;
        if (MapUtils.isNotEmpty(contextScope)) {
            scope = new HashMap<>(contextScope);
        }

        if (MapUtils.isNotEmpty(requestScope)) {
            if (scope == null) {
                scope = new HashMap<>(requestScope);
            } else {
                scope.putAll(requestScope);
            }
        }

        if (MapUtils.isNotEmpty(localScope)) {
            if (scope == null) {
                scope = new HashMap<>(localScope);
            } else {
                scope.putAll(localScope);
            }
        }

        return scope == null ? Collections.emptyMap() : Collections.unmodifiableMap(scope);
    }

    public Object getValue(String name) {
        Object value = null;

        if (MapUtils.isNotEmpty(localScope)) {
            value = localScope.get(name);
        }

        if (value == null && MapUtils.isNotEmpty(requestScope)) {
            value = requestScope.get(name);
        }

        if (value == null && MapUtils.isNotEmpty(contextScope)) {
            value = contextScope.get(name);
        }

        return value;
    }
}
