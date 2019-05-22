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

package com.treefinance.crawler.framework.decode.impl;

import com.datatrees.common.util.StringUtils;
import com.treefinance.crawler.framework.decode.Decoder;
import org.apache.commons.lang.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;

/**
 * @author <A HREF="">Cheng Wang</A>
 * @version 1.0
 * @since 2015年10月21日 下午5:48:19
 */
public class StandardDecoder implements Decoder {

    public static final StandardDecoder DEFAULT = new StandardDecoder();
    private static final Logger log = LoggerFactory.getLogger(HexDecoder.class);

    @Override
    public String decode(String content, Charset charset) {
        String result = content;
        if (charset == null) {
            charset = Charset.defaultCharset();
            log.warn("using default charset! " + charset);
        }
        result = result.replace("\\x", "\\u00").trim();
        try {
            result = StringEscapeUtils.unescapeJava(result);
        } catch (Exception e) {
            StringBuilder sb = new StringBuilder();
            String[] hex = result.split("\\\\u");
            for (String aHex : hex) {
                if (StringUtils.isNotBlank(aHex)) {
                    int data = Integer.parseInt(aHex, 16);
                    sb.append((char)data);
                }
            }
            result = sb.toString();
        }
        result = StringEscapeUtils.unescapeHtml(result);
        return result;
    }
}
