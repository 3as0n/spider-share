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

import java.util.Collection;

/**
 * @author Jerry
 * @since 18:03 2018/9/10
 */
public final class ObjectUtils {

    private ObjectUtils() {}

    public static boolean isNullOrEmpty(Object object) {
        return object == null || (object instanceof String && ((String)object).isEmpty()) || (object instanceof Collection && ((Collection)object).isEmpty());
    }

    public static boolean isNullOrEmptyString(Object object) {
        return object == null || (object instanceof String && ((String)object).isEmpty());
    }

    public static boolean isNullOrEmptyCollection(Object object) {
        return object == null || (object instanceof Collection && ((Collection)object).isEmpty());
    }
}
