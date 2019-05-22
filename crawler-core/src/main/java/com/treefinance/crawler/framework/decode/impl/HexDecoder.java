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

import com.treefinance.crawler.framework.decode.Decoder;
import com.treefinance.toolkit.util.RegExp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;
import java.util.regex.Matcher;

/**
 * @author <A HREF="">Cheng Wang</A>
 * @version 1.0
 * @since Mar 14, 2014 11:30:55 AM
 */
public class HexDecoder implements Decoder {

    public static final HexDecoder DEFAULT = new HexDecoder();

    protected static final Logger log = LoggerFactory.getLogger(HexDecoder.class);

    protected static final String pattern = "((\\\\x([\\w\\d]{2}))+)";

    @Override
    public String decode(String content, Charset charset) {
        if (charset == null) {
            charset = Charset.defaultCharset();
            log.warn("using default charset! " + charset);
        }

        return replaceByRegex(content, charset, pattern);
    }

    /**
     * @param content
     * @param charset
     * @param regex
     * @return
     */
    private String replaceByRegex(String content, Charset charset, String regex) {
        Matcher matcher = RegExp.getMatcher(regex, content);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            String orginal = matcher.group(1);
            String rel = hexToString(orginal, charset);
            log.debug("orginal:" + orginal + " rel:\t" + rel);
            rel = rel.replaceAll("\\\\", "\\\\\\\\").replaceAll("\\$", "\\\\\\$");
            matcher.appendReplacement(sb, rel);
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    private String hexToString(String content, Charset charSet) {
        String result = "";
        try {
            String sourceArr[] = content.split("\\\\");
            byte[] byteArr = new byte[sourceArr.length - 1];
            for (int i = 1; i < sourceArr.length; i++) {
                Integer hexInt = Integer.decode("0" + sourceArr[i]);
                byteArr[i - 1] = hexInt.byteValue();
            }
            result = (new String(byteArr, charSet));
        } catch (Exception e) {
            log.error("hex to String error", e);
        }
        return result;
    }

}
