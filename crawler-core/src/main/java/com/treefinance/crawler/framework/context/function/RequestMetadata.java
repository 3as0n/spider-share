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

package com.treefinance.crawler.framework.context.function;

import javax.annotation.Nonnull;

import java.util.Map;
import java.util.function.Function;

/**
 * @author Jerry
 * @since 10:46 2018/7/31
 */
public interface RequestMetadata {

    Map<String, Object> getExtra();

    void setExtra(Map<String, Object> extra);

    void addExtra(String name, Object value);

    void addExtra(Map<String, Object> extra);

    Object computeExtraIfAbsent(@Nonnull String name, @Nonnull Function<String, Object> mappingFunction);

    default <T> T computeExtraIfAbsent(@Nonnull String name, @Nonnull Function<String, Object> mappingFunction, @Nonnull Class<T> type) {
        Object value = computeExtraIfAbsent(name, mappingFunction);

        return value == null ? null : type.cast(value);
    }
}
