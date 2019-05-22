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

package com.treefinance.crawler.framework.process.fields;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Jerry
 * @since 15:40 2018/5/15
 */
public class FieldExtractResultSet extends HashMap<String, FieldExtractResult> {

    public boolean isNotEmptyResult(String name) {
        FieldExtractResult obj = get(name);

        if (obj == null) {
            return false;
        }

        return obj.isNotEmpty();
    }

    public Map<String, Object> resultMap() {
        if (this.isEmpty()) {
            return Collections.emptyMap();
        }

        Map<String, Object> resultMap = new HashMap<>();
        forEach((id, fieldExtractResult) -> {
            Object result = fieldExtractResult.getResult();
            if (result != null) {
                resultMap.put(id, result);
            }
        });

        return Collections.unmodifiableMap(resultMap);
    }
}
