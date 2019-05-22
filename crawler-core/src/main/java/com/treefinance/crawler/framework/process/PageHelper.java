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

package com.treefinance.crawler.framework.process;

import com.treefinance.crawler.framework.config.xml.page.Regexp;
import com.treefinance.crawler.framework.config.xml.page.Replacement;
import com.treefinance.toolkit.util.RegExp;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.regex.Matcher;

/**
 * @author Jerry
 * @since 17:20 26/12/2017
 */
public final class PageHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(PageHelper.class);

    private PageHelper() {}

    public static String replaceText(String content, List<Replacement> replacements) {
        String result = content;

        if (CollectionUtils.isNotEmpty(replacements)) {
            for (Replacement rm : replacements) {
                result = result.replaceAll(rm.getFrom(), Matcher.quoteReplacement(rm.getTo()));
            }
        }

        return result;
    }

    public static String getTextByRegexp(String content, Regexp regexp) {
        if (regexp == null || StringUtils.isEmpty(regexp.getRegex())) {
            return content;
        }

        LOGGER.debug("Extract content by regexp[pattern: {}, index: {}] <<< {}", regexp.getRegex(), regexp.getIndex(), content);

        String result = RegExp.group(content, regexp.getRegex(), regexp.getIndex());

        LOGGER.debug("Extracted result >>> {}", result);

        return result;
    }
}
