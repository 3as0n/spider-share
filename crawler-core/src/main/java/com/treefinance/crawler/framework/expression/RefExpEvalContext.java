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

import java.util.Map;

/**
 * @author Jerry
 * @since 14:01 2018/8/30
 */
public class RefExpEvalContext extends ExpEvalContext {

    public static final RefExpEvalContext DEFAULT = new RefExpEvalContext(null);

    private boolean nullableIfMatch = false;

    private boolean format;

    public RefExpEvalContext(Map<String, Object> placeholderMapping) {
        this(placeholderMapping, false);
    }

    public RefExpEvalContext(Map<String, Object> placeholderMapping, boolean format) {
        super(placeholderMapping);
        this.format = format;
    }

    public RefExpEvalContext(Map<String, Object> placeholderMapping, boolean failOnUnknown, boolean allowNull) {
        this(placeholderMapping, failOnUnknown, allowNull, false);
    }

    public RefExpEvalContext(Map<String, Object> placeholderMapping, boolean failOnUnknown, boolean allowNull, boolean format) {
        super(placeholderMapping, failOnUnknown, allowNull);
        this.format = format;
    }

    public RefExpEvalContext(Map<String, Object> placeholderMapping, boolean failOnUnknown, boolean allowNull, boolean nullableIfMatch, boolean format) {
        super(placeholderMapping, failOnUnknown, allowNull);
        this.nullableIfMatch = nullableIfMatch;
        this.format = format;
    }

    public boolean isNullableIfMatch() {
        return nullableIfMatch;
    }

    public void setNullableIfMatch(boolean nullableIfMatch) {
        this.nullableIfMatch = nullableIfMatch;
    }

    public boolean isFormat() {
        return format;
    }

    public void setFormat(boolean format) {
        this.format = format;
    }
}
