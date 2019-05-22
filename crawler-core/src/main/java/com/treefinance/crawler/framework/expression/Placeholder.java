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

package com.treefinance.crawler.framework.expression;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @author Jerry
 * @since 17:31 2018/8/27
 */
class Placeholder {

    private final String name;

    private final String subname;

    private Placeholder(String name, String subname) {
        this.name = name;
        this.subname = subname;
    }

    public static Placeholder parse(String placeholder) {
        if (StringUtils.isEmpty(placeholder)) {
            throw new IllegalArgumentException("Invalid expression placeholder : " + placeholder);
        }

        if (placeholder.charAt(0) == '.') {
            throw new IllegalArgumentException("Expression placeholder must not be start with '.'! - placeholder : " + placeholder);
        }

        int cursor = 0;
        int length = placeholder.length();
        StringBuilder name = new StringBuilder(length);
        while (cursor < length) {
            char nextChar = placeholder.charAt(cursor);
            if (nextChar == '\\') {
                cursor++;
                if (cursor == length) {
                    name.append(nextChar);
                    break;
                }
                nextChar = placeholder.charAt(cursor);
                name.append(nextChar);
                cursor++;
            } else if (nextChar == '.') {
                cursor++;
                break;
            } else {
                name.append(nextChar);
                cursor++;
            }
        }

        String subname = null;
        if (cursor < length) {
            subname = placeholder.substring(cursor);
        }

        return new Placeholder(name.toString(), subname);
    }

    public String getName() {
        return name;
    }

    public String getSubname() {
        return subname;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE).append("name", name).append("subname", subname).toString();
    }
}
