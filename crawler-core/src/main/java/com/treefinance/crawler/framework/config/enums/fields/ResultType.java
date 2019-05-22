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

package com.treefinance.crawler.framework.config.enums.fields;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

/**
 * @author <A HREF="mailto:wangcheng@datatrees.com.cn">Cheng Wang</A>
 * @version 1.0
 * @since Feb 7, 2014 4:29:24 PM
 */
public enum ResultType {
    String("string"),
    NUMBER("number"),
    DATE("date"),
    PAYMENT("payment"),
    RESOURCE_STRING("resource_string"),
    FILE("file"),
    CURRENCY("currency"),
    CURRENCY_PAYMENT("currency_payment"),
    RMB("rmb"),
    BOOLEAN("boolean"),
    INT("int"),
    LONG("long");

    private final String value;

    ResultType(String value) {
        this.value = value;
    }

    public static ResultType getResultType(String value) {
        if (StringUtils.isEmpty(value)) {
            return null;
        }

        return Arrays.stream(values()).filter(item -> item.getValue().equalsIgnoreCase(value)).findFirst().orElse(null);
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return this.getValue();
    }
}
