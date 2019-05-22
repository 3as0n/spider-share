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

package com.datatrees.spider.share.service.util;

import com.datatrees.spider.share.service.domain.SubmitFile;
import com.treefinance.toolkit.util.io.Streams;
import org.apache.commons.lang.StringUtils;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;

public class ZipCompressUtils {

    private static final Logger logger = LoggerFactory.getLogger(ZipCompressUtils.class);

    public static void compress(String zipPathName, String srcPathName) {
        logger.debug("start compress file: {}", srcPathName);
        File file = new File(srcPathName);
        if (!file.exists()) {
            throw new RuntimeException(srcPathName + "not exist！");
        }

        File zipFile = new File(zipPathName);

        try (ZipOutputStream out = new ZipOutputStream(new CheckedOutputStream(new FileOutputStream(zipFile), new CRC32()))) {
            compress(file, out, StringUtils.EMPTY);
        } catch (IOException e) {
            throw new UncheckedIOException("Error compressing file with zip.", e);
        }
    }

    public static void compress(String zipPathName, Map<String, SubmitFile> compressFileMap) {
        File zipFile = new File(zipPathName);

        try (FileOutputStream fileOutputStream = new FileOutputStream(zipFile)) {
            compress(fileOutputStream, compressFileMap);
        } catch (IOException e) {
            throw new UncheckedIOException("Error compressing file with zip.", e);
        }
    }

    private static void compress(File file, ZipOutputStream out, String basedir) throws IOException {
        boolean directory = file.isDirectory();
        logger.debug("compress file: {}, directory: {}", basedir + file.getName(), directory);
        if (directory) {
            File[] files = file.listFiles();
            if (files != null) {
                for (File item : files) {
                    compress(item, out, basedir + file.getName() + "/");
                }
            }
        } else if (file.exists()) {
            try (FileInputStream fis = new FileInputStream(file)) {
                compressInputStream(fis, out, basedir, file.getName());
            }
        }
    }

    public static byte[] compress(Map<String, SubmitFile> compressFileMap) {
        logger.debug("start compressing file: {}", compressFileMap.keySet());
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        compress(output, compressFileMap);

        return output.toByteArray();
    }

    public static void compress(OutputStream output, Map<String, SubmitFile> compressFileMap) {
        logger.debug("start compressing file: {}", compressFileMap.keySet());

        try (ZipOutputStream out = new ZipOutputStream(new CheckedOutputStream(output, new CRC32()))) {
            for (Entry<String, SubmitFile> entry : compressFileMap.entrySet()) {
                compress(out, entry);
            }
        } catch (IOException e) {
            throw new UncheckedIOException("Error compressing file with zip.", e);
        }
    }

    private static void compress(ZipOutputStream out, Entry<String, SubmitFile> entry) throws IOException {
        SubmitFile value = entry.getValue();
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(value.getValue())) {
            String fileName = StringUtils.defaultIfBlank(value.getFileName(), entry.getKey() + ".html");
            compressInputStream(inputStream, out, StringUtils.EMPTY, fileName);
        }
    }

    private static void compressInputStream(InputStream input, ZipOutputStream out, String basedir, String name) throws IOException {
        try (BufferedInputStream bis = new BufferedInputStream(input)) {
            ZipEntry entry = new ZipEntry(basedir + name);
            out.putNextEntry(entry);

            Streams.write(bis, out);
        }
    }

}
