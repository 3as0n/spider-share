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

package com.treefinance.crawler.framework.util;

import org.apache.commons.lang.StringUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static com.treefinance.crawler.framework.context.UrlProtocolRegistry.PROTOCOL_SEPARATOR;
import static com.treefinance.crawler.framework.context.UrlProtocolRegistry.getProtocols;

/**
 * @author <A HREF="">Cheng Wang</A>
 * @version 1.0
 * @since 2014-12-18 下午7:31:52
 */
public final class URLSplitter {

    private URLSplitter() {}

    public static Collection<String> split(String multiUrls) {
        String urls = StringUtils.trim(multiUrls);
        if (StringUtils.isEmpty(urls)) {
            return Collections.emptyList();
        }

        Set<String> list = new HashSet<>();

        int end = urls.length();
        int start = urls.lastIndexOf(PROTOCOL_SEPARATOR);
        String body;
        while (start >= PROTOCOL_SEPARATOR.length()) {
            int pos = urls.lastIndexOf(PROTOCOL_SEPARATOR, start - 1);
            if (pos == -1) {
                pos = 0;
            }

            body = urls.substring(start, end);
            String head = urls.substring(pos, start);

            for (String protocol : getProtocols()) {
                if (head.endsWith(protocol)) {
                    list.add(protocol + StringUtils.stripEnd(body, null));
                    end = start - protocol.length();
                    break;
                }
            }
            start = pos;
        }

        if (end > 0) {
            list.add(urls.substring(0, end));
        }

        if (list.size() > 1) {
            list.add(urls);
        }

        return list;
    }
}
