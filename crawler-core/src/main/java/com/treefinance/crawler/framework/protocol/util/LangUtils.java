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

package com.treefinance.crawler.framework.protocol.util;

/**
 * @author <A HREF="">Cheng Wang</A>
 * @version 1.0
 * @since Feb 24, 2014 1:51:43 PM
 */
public final class LangUtils {

    public static final int HASH_SEED = 17;

    public static final int HASH_OFFSET = 37;

    private LangUtils() {
        super();
    }

    public static int hashCode(final int seed, final int hashcode) {
        return seed * HASH_OFFSET + hashcode;
    }

    public static int hashCode(final int seed, final Object obj) {
        return hashCode(seed, obj != null ? obj.hashCode() : 0);
    }

    public static int hashCode(final int seed, final boolean b) {
        return hashCode(seed, b ? 1 : 0);
    }

    public static boolean equals(final Object obj1, final Object obj2) {
        return obj1 == null ? obj2 == null : obj1.equals(obj2);
    }
}
