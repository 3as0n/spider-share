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

import com.treefinance.crawler.lang.Copyable;
import com.treefinance.toolkit.util.kryo.KryoUtils;

import java.io.Serializable;
import java.util.Collections;
import java.util.Map;

/**
 * @author Jerry
 * @since 13:34 2018/5/30
 */
public class ExpEvalContext implements Serializable, Copyable<ExpEvalContext> {

    public static final ExpEvalContext DEFAULT = new ExpEvalContext(null);

    private final Map<String, Object> placeholderMapping;

    private boolean failOnUnknown = true; // fail on unknown placeholder

    private boolean allowNull = false; // placeholder is nullable

    private boolean nullToEmpty = false; // convert the null value of placeholder to empty string

    public ExpEvalContext(Map<String, Object> placeholderMapping) {
        this(placeholderMapping, true);
    }

    public ExpEvalContext(Map<String, Object> placeholderMapping, boolean failOnUnknown) {
        this(placeholderMapping, failOnUnknown, false);
    }

    public ExpEvalContext(Map<String, Object> placeholderMapping, boolean failOnUnknown, boolean allowNull) {
        this(placeholderMapping, failOnUnknown, allowNull, false);
    }

    public ExpEvalContext(Map<String, Object> placeholderMapping, boolean failOnUnknown, boolean allowNull, boolean nullToEmpty) {
        this.placeholderMapping = placeholderMapping == null ? Collections.emptyMap() : placeholderMapping;
        this.failOnUnknown = failOnUnknown;
        this.allowNull = allowNull;
        this.nullToEmpty = nullToEmpty;
    }

    public Map<String, Object> getPlaceholderMapping() {
        return Collections.unmodifiableMap(placeholderMapping);
    }

    public boolean isFailOnUnknown() {
        return failOnUnknown;
    }

    public void setFailOnUnknown(boolean failOnUnknown) {
        this.failOnUnknown = failOnUnknown;
    }

    public boolean isAllowNull() {
        return allowNull;
    }

    public void setAllowNull(boolean allowNull) {
        this.allowNull = allowNull;
    }

    public boolean isNullToEmpty() {
        return nullToEmpty;
    }

    public void setNullToEmpty(boolean nullToEmpty) {
        this.nullToEmpty = nullToEmpty;
    }

    @Override
    public ExpEvalContext copy() {
        return KryoUtils.copy(this);
    }
}
