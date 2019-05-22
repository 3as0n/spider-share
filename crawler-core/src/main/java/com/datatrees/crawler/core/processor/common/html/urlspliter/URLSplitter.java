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

package com.datatrees.crawler.core.processor.common.html.urlspliter;

import com.treefinance.crawler.framework.context.UrlProtocolRegistry;
import org.apache.commons.lang.StringUtils;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * @author <A HREF="mailto:wangcheng@datatrees.com.cn">Cheng Wang</A>
 * @version 1.0
 * @since 2014-12-18 下午7:31:52
 */
@Deprecated
public class URLSplitter {

    public static Collection<String> split(String originalURL) {
        StringBuffer tmpOriginalURL = new StringBuffer(StringUtils.trim(originalURL));

        Set<String> urlList = new HashSet<String>();
        urlList.add(StringUtils.trim(originalURL));

        int round = 2; // Records of protocol separator what the times of occurrence
        int index = 0; // Records of each protocol separator subscript position
        loop:
        for (;;) {
            outer:
            if ((index = StringUtils.ordinalIndexOf(tmpOriginalURL.toString(), UrlProtocolRegistry.PROTOCOL_SEPARATOR, round)) != -1) {

                String tempUrl = tmpOriginalURL.substring(0, index);
                String firstUrl = "";
                for (String protocol : UrlProtocolRegistry.getProtocols()) {
                    if (tempUrl.endsWith(protocol)) {
                        int end = (index - protocol.length()) < 0 ? 0 : (index - protocol.length());
                        firstUrl = tmpOriginalURL.substring(0, end);
                        if (StringUtils.isNotBlank(firstUrl) && !urlList.contains(firstUrl)) {
                            urlList.add(StringUtils.trim(firstUrl));
                        }
                        tmpOriginalURL = new StringBuffer(tmpOriginalURL.substring(firstUrl.length()));
                        // To avoid special situation,termination outer loop
                        if (urlList.contains(tmpOriginalURL))
                            break loop;

                        // reset round value
                        round = 2;
                        // Out of the inner loop
                        break outer;
                    }
                }
                round++;
            } else {
                if (!urlList.contains(tmpOriginalURL)) {
                    urlList.add(StringUtils.trim(tmpOriginalURL.toString()));
                }
                // Jump out of a dead circulation
                break loop;
            }
        }
        return urlList;
    }
}
