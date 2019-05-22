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

package com.datatrees.spider.share.domain;

import java.util.HashMap;
import java.util.Map;

/**
 * @author <A HREF="">Cheng Wang</A>
 * @version 1.0
 * @since 2015年7月28日 下午11:32:45
 */
public enum ResultType {
    EBANKBILL("EBANKBILL"),
    MAILBILL("MAILBILL"),
    OPERATOR("OPERATOR"),
    ECOMMERCE("ECOMMERCE"),
    DEFAULT("DEFAULT");

    private static Map<String, ResultType> ResultTypeMap = new HashMap<String, ResultType>();

    static {
        for (ResultType obj : values()) {
            ResultTypeMap.put(obj.getValue(), obj);
        }
    }

    private final String value;

    ResultType(String value) {
        this.value = value;
    }

    public static ResultType getResultType(String value) {
        return ResultTypeMap.get(value);
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return this.getValue();
    }
}
