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

import com.treefinance.crawler.framework.context.SearchProcessorContext;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

/**
 * @author <A HREF="mailto:wangcheng@datatrees.com.cn">Cheng Wang</A>
 * @version 1.0
 * @since 2015年7月21日 下午1:23:40
 */
public enum FieldVisibleType {
    /**
     * request scope
     */
    REQUEST("request"),
    /**
     * context scope, like that some fields only was stored in search context or extract context.
     * <p>
     * notice: the context scope contains request scope
     * </p>
     */
    CONTEXT("context"),
    /**
     * only stored in <code>processor_result</code> of task context.
     * 
     * @see SearchProcessorContext#processorResult
     */
    PROCESSOR_RESULT("processor_result");

    private final String value;

    FieldVisibleType(String value) {
        this.value = value;
    }

    public static FieldVisibleType getFieldVisibleType(String value) {
        if (StringUtils.isEmpty(value)) {
            return null;
        }

        return Arrays.stream(values()).filter(e -> e.getValue().equalsIgnoreCase(value)).findFirst().orElse(null);
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return this.getValue();
    }
}
