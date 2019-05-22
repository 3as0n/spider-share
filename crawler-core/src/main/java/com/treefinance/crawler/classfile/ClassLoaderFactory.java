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

package com.treefinance.crawler.classfile;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Jerry
 * @since 09:59 11/08/2017
 */
public final class ClassLoaderFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClassLoaderFactory.class);

    private ClassLoaderFactory() {}

    public static ClassLoader create(final File file, final ClassLoader parent) throws MalformedURLException {
        File[] files = file != null ? new File[] {file} : new File[0];
        return create(files, parent);
    }

    public static ClassLoader create(final File[] files, final ClassLoader parent) throws MalformedURLException {
        List<URL> urls = toURL(files);

        LOGGER.debug("Instantiate URLClassLoader. Url size: {}, parent: {}", files.length, parent);

        if (CollectionUtils.isEmpty(urls)) {
            LOGGER.warn("Can not instantiate URLClassLoader because files is empty!");
            throw new IllegalArgumentException("Can not find the resource of jar files to create class loader.");
        }

        URL[] array = urls.toArray(new URL[0]);
        if (parent != null) {
            return URLClassLoader.newInstance(array, parent);
        }
        return URLClassLoader.newInstance(array);
    }

    private static List<URL> toURL(File[] files) throws MalformedURLException {
        List<URL> list = new ArrayList<>();
        if (ArrayUtils.isNotEmpty(files)) {
            for (File file : files) {
                if (file == null || !file.exists() || !file.canRead()) {
                    continue;
                }

                if (file.isFile()) {
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("Loading jar file: {}", file.getAbsolutePath());
                    }
                    list.add(file.toURI().toURL());
                } else if (file.isDirectory()) {
                    File[] children = file.listFiles();
                    list.addAll(toURL(children));
                }
            }
        }

        return list;
    }

}
