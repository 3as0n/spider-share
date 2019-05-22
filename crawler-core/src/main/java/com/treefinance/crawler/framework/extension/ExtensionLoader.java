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

package com.treefinance.crawler.framework.extension;

import com.treefinance.crawler.classfile.ClassLoaderUtils;
import com.treefinance.crawler.framework.exception.ExtensionException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.io.File;

/**
 * @author Jerry
 * @since 15:15 15/01/2018
 */
public final class ExtensionLoader {

    private ExtensionLoader() {}

    public static <T> T load(@Nonnull final File file, @Nonnull final String mainClass, final boolean forceReload, @Nullable final ClassLoader parent,
        @Nonnull final Class<T> resultType) {
        ClassLoader parentLoader = parent;
        if (parentLoader == null) {
            parentLoader = ExtensionLoader.class.getClassLoader();
        }

        try {
            return ClassLoaderUtils.loadAndInstantiate(file, parentLoader, forceReload, mainClass, resultType);
        } catch (Exception e) {
            throw new ExtensionException("Error loading and initial custom extensional spider.", e);
        }
    }
}
