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

package com.treefinance.crawler.framework.decode.impl;

import com.treefinance.crawler.framework.decode.Decoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;

/**
 * @author <A HREF="">Cheng Wang</A>
 * @version 1.0
 * @since Mar 15, 2014 11:30:55 AM
 */
public class SpecialDecoder implements Decoder {

    public static final SpecialDecoder DEFAULT = new SpecialDecoder();

    private static final Logger log = LoggerFactory.getLogger(SpecialDecoder.class);

    @Override
    public String decode(String content, Charset charset) {
        if (charset == null) {
            charset = Charset.defaultCharset();
            log.warn("using default charset! " + charset);
        }
        return decodeUnicode(content, charset);
    }

    private String decodeUnicode(String content, Charset charset) {
        StringBuilder sb = new StringBuilder();
        int i = -1;
        int pos = 0;

        while ((i = content.indexOf("\\u", pos)) != -1) {
            sb.append(content, pos, i);
            if (i + 5 < content.length()) {
                pos = i + 6;
                sb.append((char)Integer.parseInt(content.substring(i + 2, i + 6), 16));
            }
        }
        if (sb.length() == 0) {
            sb.append(content);
        }
        return new String(sb.toString().getBytes(), charset);
    }

}
