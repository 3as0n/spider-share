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

package com.treefinance.crawler.framework.download;

import com.datatrees.common.conf.PropertiesConfiguration;
import com.treefinance.crawler.framework.protocol.Content;
import com.treefinance.crawler.framework.protocol.ProtocolInput;
import com.treefinance.crawler.framework.protocol.ProtocolOutput;
import com.treefinance.crawler.framework.protocol.WebClientUtil;
import com.treefinance.crawler.framework.util.CharsetUtil;
import com.treefinance.toolkit.util.RegExp;
import com.treefinance.toolkit.util.io.Streams;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.impl.io.EmptyInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;

public class WrappedFile {

    private static final Logger logger = LoggerFactory.getLogger(WrappedFile.class);
    private static final byte[] EMPTY_BYTES = new byte[0];
    private static final int sleepSecond = PropertiesConfiguration.getInstance().getInt("default.sleep.seconds", 2000);
    private static final int retryCount = PropertiesConfiguration.getInstance().getInt("default.file.download.retry.count", 3);
    private static final String textFileNameSuffixPattern = PropertiesConfiguration.getInstance().get("text.filename.suffix.pattern", "htm$|html$|txt$");
    private static final String textMimeTypePattern = PropertiesConfiguration.getInstance().get("text.mimeType.pattern", "^text/");
    private final File file;
    private String name;
    private String mimeType;
    private String charSet;
    private long size;
    private String sourceURL;
    private ProtocolInput input;

    public WrappedFile(@Nonnull File file) {
        this.file = Objects.requireNonNull(file);
        this.size = file.length();
    }

    public File getFile() {
        return file;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getSourceURL() {
        return sourceURL;
    }

    public void setSourceURL(String sourceURL) {
        this.sourceURL = sourceURL;
    }

    public void setCharSet(String charSet) {
        this.charSet = charSet;
    }

    public ProtocolInput getInput() {
        return input;
    }

    public void setInput(ProtocolInput input) {
        this.input = input;
    }

    public boolean isEmpty() {
        return size <= 0 && (!file.exists() || file.length() == 0);
    }

    public void write(byte[] data) throws IOException {
        try (OutputStream stream = new FileOutputStream(file)) {
            Streams.write(data, stream);
        }
        this.setSize(file.length());
    }

    public byte[] readFull() throws IOException, InterruptedException {
        try (InputStream stream = getInputStream()) {
            if (stream instanceof EmptyInputStream) {
                return EMPTY_BYTES;
            }
            return Streams.readToByteArray(stream);
        }
    }

    public String readToString() throws IOException, InterruptedException {
        byte[] bytes = readFull();
        if (bytes.length == 0) {
            return StringUtils.EMPTY;
        }

        return new String(bytes, CharsetUtil.DEFAULT);
    }

    public InputStream getInputStream() throws FileNotFoundException, InterruptedException {
        download();

        if (file.exists()) {
            return new FileInputStream(file);
        }

        return EmptyInputStream.INSTANCE;
    }

    public void download() throws InterruptedException {
        if (isEmpty() && input != null) {
            for (int i = 0; i < retryCount; i++) {
                try {
                    writeToFile(input);
                    break;
                } catch (Exception e) {
                    logger.error("Error downloading with url: {}, error: {}", input.getUrl(), e.getMessage());
                    long sleepMillis = sleepSecond + (int)(Math.random() * sleepSecond) * (i + 1);
                    Thread.sleep(sleepMillis);
                }
            }
        }
    }

    public boolean needDetectContent() {
        return (this.mimeType != null && RegExp.find(this.mimeType, textMimeTypePattern)) || (name != null && RegExp.find(name, textFileNameSuffixPattern));
    }

    public String getAbsolutePath() {
        return file.getAbsolutePath();
    }

    public void remove() {
        FileUtils.deleteQuietly(file);
    }

    @Override
    public String toString() {
        return "WrappedFile [name=" + name + ", mimeType=" + mimeType + ", charSet=" + charSet + ", size=" + size + ", file=" + file + ", sourceURL=" + sourceURL + "]";
    }

    private void writeToFile(ProtocolInput input) throws IOException {
        String url = input.getUrl();
        logger.info("downloading file with url: {}", url);
        this.setSourceURL(url);
        ProtocolOutput out = WebClientUtil.getFileClient().getProtocolOutput(input);
        Content content = out.getContent();
        this.setMimeType(content.getMimeType());

        if (this.needDetectContent()) {
            write(content.detectContentAsString().getBytes(CharsetUtil.UTF_8_NAME));
        } else {
            write(content.getContent());
        }
    }

}
