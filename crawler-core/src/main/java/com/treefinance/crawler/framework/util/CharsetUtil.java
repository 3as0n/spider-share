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

package com.treefinance.crawler.framework.util;

import org.apache.commons.lang.StringUtils;
import org.apache.http.Consts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;

/**
 * @author <A HREF="">Cheng Wang</A>
 * @version 1.0
 * @since Feb 20, 2014 9:43:26 AM
 */
public final class CharsetUtil {

    public static final Charset ASCII = Consts.ASCII;

    public static final Charset ISO_8859_1 = Consts.ISO_8859_1;

    public static final Charset UTF_8 = Consts.UTF_8;

    public static final String UTF_8_NAME = UTF_8.name();

    public static final String DEFAULT = UTF_8_NAME;

    private static final Logger LOGGER = LoggerFactory.getLogger(CharsetUtil.class);

    private CharsetUtil() {}

    public static Charset getCharset(String charsetName, String defaultCharset) {
        Charset charset = null;

        if (StringUtils.isNotEmpty(charsetName)) {
            try {
                charset = Charset.forName(charsetName);
            } catch (UnsupportedCharsetException e) {
                LOGGER.warn("Charset '{}' is not supported. And use default charset '{}' instead.", charsetName, defaultCharset);
            }
        }

        if (charset == null && StringUtils.isNotEmpty(defaultCharset)) {
            try {
                charset = Charset.forName(defaultCharset);
            } catch (UnsupportedCharsetException e) {
                LOGGER.warn("Default charset '{}' is not supported. ", defaultCharset);
            }
        }

        return charset;
    }

    private static Charset getCharset(String charsetName, Charset defaultCharset) {
        Charset charset = null;

        if (StringUtils.isNotEmpty(charsetName)) {
            try {
                charset = Charset.forName(charsetName);
            } catch (UnsupportedCharsetException e) {
                LOGGER.warn("Charset '{}' is not supported. And use default charset '{}' instead.", charsetName, defaultCharset);
            }
        }

        if (charset == null) {
            LOGGER.warn("Can not find charset '{}',use default '{}'.", charsetName, defaultCharset);
            charset = defaultCharset;
        }

        return charset;
    }

    public static Charset getCharset(String charsetName) {
        return getCharset(charsetName, UTF_8);
    }

    public static String getDefaultCharsetName() {
        return Charset.defaultCharset().name();
    }

    public static Charset getDefaultCharset() {
        return Charset.defaultCharset();
    }

    public static boolean exist(String charsetName) {
        return Charset.isSupported(charsetName);
    }

}
