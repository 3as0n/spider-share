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

import com.treefinance.crawler.exception.UnexpectedException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * @author Jerry
 * @since 17:25 2018/5/25
 */
public final class UrlUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(UrlUtils.class);

    private UrlUtils() {}

    public static String urlEncode(String value, String charset, boolean strict) {
        if (StringUtils.isEmpty(value)) {
            return value;
        }

        String encoding = StringUtils.trim(charset);

        if (StringUtils.isEmpty(encoding)) {
            LOGGER.warn("The given charset is empty. use default charset: {}", charset);
            encoding = CharsetUtil.UTF_8_NAME;
        }

        LOGGER.info("Url encoding with charset: {}, value: {}", encoding, value);

        try {
            return URLEncoder.encode(value, encoding);
        } catch (UnsupportedEncodingException e) {
            if (strict) {
                throw new UnexpectedException("Error url encoding with charset: " + encoding + ", value: " + value, e);
            }

            LOGGER.warn("Error url encoding with charset: {}, value: {}", encoding, value);

            if (!CharsetUtil.UTF_8_NAME.equals(encoding)) {
                LOGGER.info("Url encoding with default charset: {}, value: {}", CharsetUtil.UTF_8_NAME, value);
                try {
                    return URLEncoder.encode(value, CharsetUtil.UTF_8_NAME);
                } catch (UnsupportedEncodingException e1) {
                    LOGGER.warn("Error url encoding with default charset: {}, value: {}", CharsetUtil.UTF_8_NAME, value);
                }
            }
        }

        return value;
    }
}
