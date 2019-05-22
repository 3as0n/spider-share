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

package com.treefinance.crawler.classfile;

import java.io.File;
import java.util.Objects;

/**
 * @author Jerry
 * @since 10:38 14/08/2017
 */
public final class ClassLoaderUtils {

    private ClassLoaderUtils() {}

    public static <T> T loadAndInstantiate(File file, ClassLoader parent, boolean forceReload, String className, Class<T> resultType)
        throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        Objects.requireNonNull(className);
        Objects.requireNonNull(resultType);
        ClassLoader classLoader = ClassLoaderManager.findClassLoader(file, parent, forceReload);

        Class<?> clazz = classLoader.loadClass(className);

        if (resultType.isAssignableFrom(clazz)) {
            return resultType.cast(clazz.newInstance());
        }

        throw new IllegalArgumentException("The loaded class[" + className + "] is not assignable form '" + resultType.getSimpleName() + "'.");
    }

}
