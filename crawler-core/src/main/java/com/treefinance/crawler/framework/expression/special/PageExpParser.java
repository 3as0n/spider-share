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

package com.treefinance.crawler.framework.expression.special;

import com.treefinance.crawler.framework.expression.spring.SpelExpParser;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Jerry
 * @since 01:29 2018/5/31
 */
public class PageExpParser {

    private static final Logger LOGGER = LoggerFactory.getLogger(PageExpParser.class);

    private static final String REGEX = "#\\{\\s*page\\s*,([\\d\\s.*/+-]+),([\\d\\s.*/+-]+),\\s*([\\d.]+)\\s*([+-]?)\\s*}";

    private static final Pattern PATTERN = Pattern.compile(REGEX);

    private PageExpParser() {}

    public static String eval(String text, int page) {
        LOGGER.debug("Eval page expression. input: {}, page: {}", text, page);
        if (StringUtils.isEmpty(text))
            return text;

        Matcher matcher = PATTERN.matcher(text);
        if (matcher.find()) {
            int pageNum = page;
            if (pageNum <= 0) {
                pageNum = 1;
            }

            StringBuffer buffer = new StringBuffer();

            do {
                int val = calculate(matcher, pageNum);
                matcher.appendReplacement(buffer, Integer.toString(val));
            } while (matcher.find());

            matcher.appendTail(buffer);

            String result = buffer.toString();
            LOGGER.debug("Page expression's eval-result: {}", result);
            return result;
        }

        return text;
    }

    private static int calculate(Matcher matcher, int page) {
        int start = Math.max(SpelExpParser.parse(matcher.group(1), Integer.TYPE), 0);

        if (page <= 0) {
            return start;
        }

        int end = SpelExpParser.parse(matcher.group(2), Integer.TYPE);
        if (end <= 0) {
            return start;
        }

        int offset = SpelExpParser.parse(matcher.group(3), Integer.TYPE);
        String offsetSign = matcher.group(4);
        if ("-".equals(offsetSign)) {
            offset = 0 - offset;
        }

        int pageNum = page <= end ? page : end;

        return start + offset * (pageNum - 1);
    }

}
