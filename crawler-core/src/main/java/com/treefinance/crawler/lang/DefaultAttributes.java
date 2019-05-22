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

package com.treefinance.crawler.lang;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * @author Jerry
 * @since 15:01 2018/5/8
 */
public class DefaultAttributes implements Attributes {

    private final Map<String, Object> map = new HashMap<>();

    @Override
    public void setAttribute(@Nonnull String name, @Nullable Object attribute) {
        if (attribute == null) {
            removeAttribute(name);
        } else {
            map.put(name, attribute);
        }
    }

    @Override
    public boolean containsAttribute(@Nonnull String name) {
        return map.containsKey(name);
    }

    @Override
    public Object getAttribute(@Nonnull String name) {
        return map.get(name);
    }

    @Override
    public Object computeAttribute(@Nonnull String name, @Nonnull BiFunction<String, Object, Object> mappingFunction) {
        return map.compute(name, mappingFunction);
    }

    @Override
    public Object computeAttributeIfAbsent(@Nonnull String name, @Nonnull Function<String, Object> mappingFunction) {
        return map.computeIfAbsent(name, mappingFunction);
    }

    @Override
    public Object removeAttribute(@Nonnull String name) {
        return map.remove(name);
    }

    @Nonnull
    @Override
    public Map<String, Object> getAttributes() {
        return Collections.unmodifiableMap(map);
    }

    @Nonnull
    @Override
    public Enumeration<String> getAttributeNames() {
        return Collections.enumeration(Collections.unmodifiableSet(map.keySet()));
    }

    @Override
    public void clearAttributes() {
        map.clear();
    }

    @Override
    public void addAttributes(@Nullable Map<String, Object> attributes) {
        if (attributes != null) {
            map.putAll(attributes);
        }
    }

    @Nonnull
    @Override
    public String toString() {
        return map.toString();
    }

    protected Map<String, Object> attributes() {
        return map;
    }
}
