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

package com.treefinance.crawler.framework.context;

import com.treefinance.crawler.framework.context.function.SpiderRequest;
import com.treefinance.crawler.framework.context.function.SpiderResponse;
import com.treefinance.crawler.framework.process.fields.FieldExtractResult;
import com.treefinance.crawler.framework.process.fields.FieldExtractResultSet;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Jerry
 * @since 14:41 2018/5/16
 */
public final class FieldScopes {

    private static final Logger LOGGER = LoggerFactory.getLogger(FieldScopes.class);

    private FieldScopes() {}

    public static Map<String, Object> getSharedFields(SpiderRequest request) {
        return request.getGlobalScopeAsMap();
    }

    public static Map<String, Object> getExtractFields(SpiderResponse response) {
        return ResponseUtil.getFieldExtractResultMap(response);
    }

    public static Object getVisibleField(String name, SpiderRequest request, SpiderResponse response) {
        Object result = null;
        FieldExtractResultSet fieldExtractResultSet = ResponseUtil.getFieldExtractResultSet(response);
        if (fieldExtractResultSet != null) {
            FieldExtractResult fieldExtractResult = fieldExtractResultSet.get(name);
            if (fieldExtractResult != null) {
                result = fieldExtractResult.getResult();
            }
        }
        if (result == null) {
            result = request.getGlobalFieldValue(name);
        }
        LOGGER.debug("Search field in extracted fields or global fields. - name: {}, value: {}", name, result);

        return result;
    }

    /**
     * 获取当前上下文可见的值栈，包含解析出的字段值，共享的值。其中解析值优先于共享值。
     */
    public static Map<String, Object> getVisibleFields(SpiderRequest request, SpiderResponse response) {
        List<Map<String, Object>> fieldScopes = new ArrayList<>(2);

        if (response != null) {
            Map<String, Object> extractFields = getExtractFields(response);
            if (MapUtils.isNotEmpty(extractFields)) {
                fieldScopes.add(extractFields);
            }
        }

        if (request != null) {
            Map<String, Object> sharedFields = getSharedFields(request);
            if (MapUtils.isNotEmpty(sharedFields)) {
                fieldScopes.add(sharedFields);
            }
        }

        return merge(fieldScopes);
    }

    /**
     * 合并值栈，优先级按列表由前往后降序。
     */
    @Nonnull
    public static Map<String, Object> merge(@Nullable List<Map<String, Object>> fieldScopes) {
        if (CollectionUtils.isEmpty(fieldScopes)) {
            return Collections.emptyMap();
        }

        Map<String, Object> map;
        if (fieldScopes.size() == 1) {
            map = fieldScopes.get(0);
        } else {
            map = new HashMap<>();
            for (Map<String, Object> fieldScope : fieldScopes) {
                if (fieldScope == null) {
                    continue;
                }

                fieldScope.forEach((key, val) -> {
                    if (val != null) {
                        map.putIfAbsent(key, val);
                    }
                });
            }
        }

        return Collections.unmodifiableMap(map);
    }
}
