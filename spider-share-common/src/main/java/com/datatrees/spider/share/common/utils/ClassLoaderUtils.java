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

package com.datatrees.spider.share.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * Created by zhouxinghai on 2017/7/14.
 */
public class ClassLoaderUtils {

    private static final Logger logger = LoggerFactory.getLogger(ClassLoaderUtils.class);

    public static Class loadClass(File jarFile, String className) {
        try {
            URL url = new URL("file", null, jarFile.getAbsolutePath());
            URLClassLoader loader = new URLClassLoader(new URL[] {url}, Thread.currentThread().getContextClassLoader());
            return loader.loadClass(className);
        } catch (Throwable e) {
            logger.error("loadClass error jarFile={},className={}", jarFile.getAbsolutePath(), className);
            throw new RuntimeException("loadClass error jarFile=" + jarFile.getAbsolutePath() + ",className=" + className);
        }
    }

    public static ClassLoader createClassLoader(File jarFile) {
        try {
            if (!jarFile.exists()) {
                logger.error("jar file not found,fileName={}", jarFile.getAbsolutePath());
            }
            URLClassLoader loader = URLClassLoader.newInstance(new URL[] {jarFile.toURI().toURL()}, Thread.currentThread().getContextClassLoader());
            logger.info("createClassLoader success jarFile={}", jarFile.getAbsolutePath());
            return loader;
        } catch (Throwable e) {
            logger.error("createClassLoader error jarFile={}", jarFile.getAbsolutePath());
            throw new RuntimeException("createClassLoader error jarFile=" + jarFile.getAbsolutePath());
        }

    }
}
