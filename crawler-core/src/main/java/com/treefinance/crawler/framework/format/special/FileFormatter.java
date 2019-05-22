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

package com.treefinance.crawler.framework.format.special;

import com.treefinance.crawler.framework.context.AbstractProcessorContext;
import com.treefinance.crawler.framework.download.WrappedFile;
import com.treefinance.crawler.framework.format.CommonFormatter;
import com.treefinance.crawler.framework.format.FormatConfig;
import com.treefinance.crawler.framework.protocol.ProtocolInput;
import com.treefinance.crawler.framework.protocol.util.UrlUtils;
import com.treefinance.crawler.framework.util.CharsetUtil;
import com.treefinance.crawler.framework.util.FileUtils;

import javax.annotation.Nonnull;

import java.io.File;

/**
 * @author Jerry
 * @since 00:43 2018/6/2
 */
public class FileFormatter extends CommonFormatter<WrappedFile> {

    @Override
    protected WrappedFile toFormat(@Nonnull String value, @Nonnull FormatConfig config) throws Exception {
        AbstractProcessorContext processorContext = config.getProcessorContext();
        File file = new File(FileUtils.getFileRandomPath(processorContext.getWebsiteName()));
        WrappedFile wrappedFile = new WrappedFile(file);
        Object result = config.getSourceFieldValue("fileName");
        wrappedFile.setName(result != null ? result.toString() : file.getName());

        if (UrlUtils.isUrl(value)) {
            String cookie = processorContext.getCookiesAsString();
            ProtocolInput input = new ProtocolInput().setUrl(value).setFollowRedirect(true).setCookie(cookie);
            wrappedFile.setInput(input);
            // set input async to get file while needed
        } else {
            wrappedFile.write(value.getBytes(CharsetUtil.UTF_8_NAME));
            wrappedFile.setMimeType("text/html");
            wrappedFile.setCharSet("UTF-8");
            wrappedFile.setSourceURL(config.getCurrentUrl());
        }

        return wrappedFile;
    }

}
