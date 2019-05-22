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

package com.treefinance.crawler.framework.util;

import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nonnull;

import java.lang.reflect.Field;
import java.security.AccessController;
import java.security.PrivilegedAction;

/**
 * @author Jerry
 * @since 20:32 26/12/2017
 */
public final class BeanUtils {

    private BeanUtils() {}

    public static Object getFieldValue(Object bean, String fieldName) throws NoSuchFieldException, IllegalAccessException {
        if (bean == null || StringUtils.isEmpty(fieldName)) {
            return null;
        }

        Field f = getField(bean, fieldName);
        return f.get(bean);
    }

    public static Field getField(@Nonnull Object bean, String fieldName) throws NoSuchFieldException {
        Class clazz = bean.getClass();
        return getField(clazz, fieldName);
    }

    public static Field getField(@Nonnull Class clazz, String fieldName) throws NoSuchFieldException {
        Field field = clazz.getDeclaredField(fieldName);
        AccessController.doPrivileged((PrivilegedAction<Object>)() -> {
            field.setAccessible(true);
            return null;
        });
        return field;
    }

}
