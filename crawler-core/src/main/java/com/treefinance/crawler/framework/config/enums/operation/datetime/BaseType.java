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

package com.treefinance.crawler.framework.config.enums.operation.datetime;

import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author likun
 * @version $Id: BaseType.java, v 0.1 Jul 22, 2015 12:37:13 PM likun Exp $
 */
public enum BaseType {
    NOW("now"),
    FIRST_DAY_OF_THIS_WEEK("firstdayofthisweek"),
    LAST_DAY_OF_THIS_WEEK("lastdayofthisweek"),
    FIRST_DAY_OF_THIS_MONTH("firstdayofthismonth"),
    LAST_DAY_OF_THIS_MONTH("lastdayofthismonth"),
    FIRST_DAY_OF_THIS_YEAR("firstdayofthisyear"),
    LAST_DAY_OF_THIS_YEAR("lastdayofthisyear"),
    CUSTOM("custom");

    private static Map<String, BaseType> baseTypeMap = new HashMap<String, BaseType>();

    static {
        for (BaseType obj : values()) {
            baseTypeMap.put(obj.getValue(), obj);
        }
    }

    private final String value;

    BaseType(String value) {
        this.value = value;
    }

    public static BaseType getBaseType(String value) {
        if (StringUtils.isEmpty(value)) {
            return null;
        }
        return baseTypeMap.get(value);
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return this.getValue();
    }
}
