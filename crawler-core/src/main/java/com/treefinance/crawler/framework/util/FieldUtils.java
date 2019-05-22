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

package com.treefinance.crawler.framework.util;

import com.treefinance.crawler.exception.UncheckedInterruptedException;
import com.treefinance.crawler.framework.context.FieldScopes;
import com.treefinance.crawler.framework.context.function.SpiderRequest;
import com.treefinance.crawler.framework.context.function.SpiderResponse;
import com.treefinance.crawler.framework.download.WrappedFile;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author <A HREF="">Cheng Wang</A>
 * @version 1.0
 * @since 2015年7月14日 下午4:03:20
 */
public final class FieldUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(FieldUtils.class);

    private static final String DEFAULT_SEPARATOR = "  \r\n";

    private FieldUtils() {}

    public static String getFieldValueAsString(Object target, String field) {
        return getFormattedFieldValue(target, field, DEFAULT_SEPARATOR);
    }

    public static String getFieldValueAsString(Object target, String field, String separator) {
        return getFormattedFieldValue(target, field, separator == null ? DEFAULT_SEPARATOR : separator);
    }

    public static String getFormattedFieldValue(Object target, String field, String separator) {
        Object value = getFieldValue(target, field);

        return formatValue(value, separator);
    }

    public static Object getFieldValue(Object target, String field) {
        if (target instanceof Map) {
            return ((Map)target).get(field);
        }

        try {
            return BeanUtils.getFieldValue(target, field);
        } catch (Exception e) {
            LOGGER.warn("Unexpected exception when getting bean field value. - " + target, e);
        }

        return null;
    }

    @SuppressWarnings("unchecked")
    private static String formatValue(Object value, String separator) {
        if (value == null) {
            return StringUtils.EMPTY;
        } else if (value instanceof String) {
            return (String)value;
        } else if (value instanceof WrappedFile) {
            try {
                return ((WrappedFile)value).readToString();
            } catch (InterruptedException e) {
                throw new UncheckedInterruptedException("unexpected interrupted exception!", e);
            } catch (IOException e) {
                LOGGER.error("Error reading file content. - " + value, e);
            }

            return StringUtils.EMPTY;
        } else if (value instanceof Collection) {
            String delimiter = StringUtils.defaultString(separator);

            return ((Collection<Object>)value).stream().map(sub -> formatValue(sub, delimiter)).collect(Collectors.joining(delimiter));
        } else {
            return value.toString();
        }
    }

    public static Object getSourceFieldValue(String sourceId, SpiderRequest request, SpiderResponse response) {
        Object result = FieldScopes.getVisibleField(sourceId, request, response);

        LOGGER.debug("Field value from sourceId: {}, result: {}", sourceId, result);

        return result;
    }
}
