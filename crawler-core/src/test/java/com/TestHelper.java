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

package com;

import com.treefinance.crawler.exception.UnexpectedException;
import com.treefinance.crawler.framework.config.SpiderConfig;
import com.treefinance.crawler.framework.config.factory.SpiderConfigFactory;
import com.treefinance.crawler.framework.download.WrappedFile;
import org.springframework.util.StreamUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Objects;

/**
 * @author Jerry
 * @since 20:25 06/12/2017
 */
public final class TestHelper {

    private TestHelper() {}

    public static <T extends SpiderConfig> T getConfig(String filepath, Class<T> configClass) {
        String content = getFileContent(filepath);

        return SpiderConfigFactory.build(content, configClass);
    }

    public static String getFileContent(String filePath) {
        try (InputStream inputStream = getInputStream(filePath)) {
            return StreamUtils.copyToString(inputStream, Charset.defaultCharset());
        } catch (IOException e) {
            throw new UnexpectedException("Unexpected exception", e);
        }
    }

    public static URL getResource(String filepath) {
        return TestHelper.class.getClassLoader().getResource(filepath);
    }

    public static InputStream getInputStream(String filepath) {
        return TestHelper.class.getClassLoader().getResourceAsStream(filepath);
    }

    public static File getFile(String path) throws URISyntaxException {
        URL resource = getResource(path);

        Objects.requireNonNull(resource);

        return new File(resource.toURI());
    }

    public static WrappedFile getPageContent(String path) {
        return getPageContent(new File(path));
    }

    public static WrappedFile getPageContent(File page) {
        WrappedFile file = new WrappedFile(page);
        file.setMimeType("text/html");
        return file;
    }
}
