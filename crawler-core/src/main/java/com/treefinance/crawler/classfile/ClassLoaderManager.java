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

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.RemovalListener;
import com.treefinance.crawler.exception.UnexpectedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author Jerry
 * @since 09:55 11/08/2017
 */
public final class ClassLoaderManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClassLoaderManager.class);

    private static final Cache<Key, ClassLoader> LOADER_CACHE =
        CacheBuilder.newBuilder().expireAfterAccess(10, TimeUnit.MINUTES).softValues().removalListener((RemovalListener<Key, ClassLoader>)notification -> {
            ClassLoader classLoader = notification.getValue();
            if (classLoader instanceof URLClassLoader) {
                try {
                    ((URLClassLoader)classLoader).close();
                } catch (IOException e) {
                    String paths = Arrays.stream(((URLClassLoader)classLoader).getURLs()).map(URL::getPath).collect(Collectors.joining(","));
                    LOGGER.error("Error closing classLoader:" + paths, e);
                }
            }
        }).build();

    public static ClassLoader findClassLoader(final File file, final ClassLoader classLoader, boolean expired) {
        try {
            Key key = new Key(file, classLoader);
            if (expired) {
                LOADER_CACHE.invalidate(key);
            }

            return LOADER_CACHE.get(key, () -> ClassLoaderFactory.create(file, classLoader));
        } catch (ExecutionException e) {
            throw new UnexpectedException(e);
        }
    }

    private static class Key {

        // file path
        private final Path path;

        // last modified time of file
        private final long lastModified;

        private final ClassLoader classLoader;

        Key(File file, ClassLoader classLoader) {
            Objects.requireNonNull(file);
            this.path = file.toPath();
            this.lastModified = file.lastModified();
            this.classLoader = classLoader;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;

            Key key = (Key)o;

            try {
                return lastModified == key.lastModified && Files.isSameFile(path, key.path)
                    && (classLoader != null ? classLoader.equals(key.classLoader) : key.classLoader == null);
            } catch (IOException e) {
                throw new UnexpectedException(e);
            }
        }

        @Override
        public int hashCode() {
            int result = path.hashCode();
            result = 31 * result + (int)(lastModified ^ (lastModified >>> 32));
            result = 31 * result + (classLoader != null ? classLoader.hashCode() : 0);
            return result;
        }
    }
}
