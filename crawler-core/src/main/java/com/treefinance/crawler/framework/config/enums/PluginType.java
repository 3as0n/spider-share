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

package com.treefinance.crawler.framework.config.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * @author <A HREF="mailto:wangcheng@datatrees.com.cn">Cheng Wang</A>
 * @version 1.0
 * @since Feb 7, 2014 11:17:43 AM
 */
public enum PluginType {
    JAVA("jar"),
    SHELL("sh"),
    PYTHON("py");

    private static Map<String, PluginType> PluginTypeMap = new HashMap<String, PluginType>();

    static {
        for (PluginType obj : values()) {
            PluginTypeMap.put(obj.getValue(), obj);
        }
    }

    private final String value;

    PluginType(String value) {
        this.value = value;
    }

    public static PluginType getPluginType(String value) {
        return PluginTypeMap.get(value);
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return this.getValue();
    }
}
