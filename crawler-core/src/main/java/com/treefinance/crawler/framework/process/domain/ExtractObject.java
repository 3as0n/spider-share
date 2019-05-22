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

package com.treefinance.crawler.framework.process.domain;

import com.treefinance.crawler.framework.process.fields.FieldExtractResult;

import java.util.Collection;
import java.util.Map;
import java.util.function.Consumer;

/**
 * @author Jerry
 * @since 14:00 2018/8/2
 */
public interface ExtractObject extends Map<String, Object> {

    String getName();

    String getResultClass();

    ExtractObject withFlatField(Object value);

    /**
     * @return true if the extract object is flat, otherwise false.
     */
    boolean isFlatField();

    Object getFlatFieldValue();

    /**
     * return true if the field value associated with <code>name</code> is valid, otherwise false.
     */
    boolean isValid(String name);

    void setFieldExtractValue(String fieldName, Object fieldValue);

    void setFieldExtractResult(FieldExtractResult fieldExtractResult);

    void merge(ExtractObject extractObject);

    default Collection<String> fieldNames() {
        return keySet();
    }

    default boolean isNotEmpty() {
        return !isEmpty();
    }

    Collection<ExtractObject> flatObjects();

    void consumeWithFlatObjects(Consumer<ExtractObject> consumer);
}
