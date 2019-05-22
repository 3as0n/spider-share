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

package com.treefinance.crawler.framework.process.operation.impl;

import com.datatrees.common.conf.PropertiesConfiguration;
import com.datatrees.common.util.GsonUtils;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.gson.reflect.TypeToken;
import com.treefinance.crawler.framework.config.xml.extractor.FieldExtractor;
import com.treefinance.crawler.framework.config.xml.operation.MappingOperation;
import com.treefinance.crawler.framework.context.function.SpiderRequest;
import com.treefinance.crawler.framework.context.function.SpiderResponse;
import com.treefinance.crawler.framework.exception.InvalidOperationException;
import com.treefinance.crawler.framework.process.operation.Operation;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nonnull;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author Jerry
 * @datetime 2015-07-17 20:02
 */
public class MappingOperationImpl extends Operation<MappingOperation> {

    private static final LoadingCache<String, Map<String, String>> CACHE =
        CacheBuilder.newBuilder().expireAfterAccess(30, TimeUnit.MINUTES).softValues().initialCapacity(2).build(new CacheLoader<String, Map<String, String>>() {
            @Override
            public Map<String, String> load(String groupName) throws Exception {
                String groupMapJson = PropertiesConfiguration.getInstance().get("mapping.group." + groupName + ".json");
                if (StringUtils.isNotBlank(groupMapJson)) {
                    return GsonUtils.fromJson(groupMapJson, new TypeToken<Map<String, String>>() {}.getType());
                }
                return null;
            }
        });

    public MappingOperationImpl(@Nonnull MappingOperation operation, @Nonnull FieldExtractor extractor) {
        super(operation, extractor);
    }

    @Override
    protected void validate(@Nonnull MappingOperation operation, @Nonnull SpiderRequest request, @Nonnull SpiderResponse response) throws Exception {
        super.validate(operation, request, response);

        if (StringUtils.isEmpty(operation.getGroupName())) {
            throw new InvalidOperationException("Invalid mapping operation! - Attribute 'group-name' must not be empty!");
        }
    }

    @Override
    protected Object doOperation(@Nonnull MappingOperation operation, @Nonnull Object operatingData, @Nonnull SpiderRequest request, @Nonnull SpiderResponse response)
        throws Exception {
        String group = operation.getGroupName();
        Map<String, String> mapping = CACHE.getUnchecked(group);
        if (mapping != null) {
            String input = (String)operatingData;
            return mapping.get(input);
        }

        return null;
    }
}
