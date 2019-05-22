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

package com.treefinance.crawler.framework.format.datetime;

import com.treefinance.crawler.framework.consts.Constants;
import com.treefinance.crawler.framework.exception.FormatException;
import com.treefinance.crawler.framework.format.ConfigurableFormatter;
import com.treefinance.crawler.framework.format.FormatConfig;
import com.treefinance.toolkit.util.RegExp;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeParserBucket;

import javax.annotation.Nonnull;

import java.util.Arrays;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Jerry
 * @since 00:43 2018/6/2
 */
public class DateFormatter extends ConfigurableFormatter<Date> {

    private static final Pattern HOUR_PATTERN = Pattern.compile("(\\b|[^0-9a-zA-Z])hh(\\b|[^0-9a-zA-Z])");

    /**
     * 适配日期。如果时间缺少日期，那么按一定规则补上年月日。
     */
    private static DateTime adaptDate(DateTime dateTime, String pattern) {
        if (pattern.toLowerCase().contains("yy") || dateTime.getYear() != DateTimeFormats.BASE_YEAR) {
            return dateTime;
        }

        DateTime now = DateTime.now();
        if (!dateTime.isAfter(now.withYear(DateTimeFormats.BASE_YEAR))) {
            if (!pattern.contains("MM") && dateTime.getMonthOfYear() == 1 && dateTime.getDayOfMonth() == 1) {
                return dateTime.withDate(now.toLocalDate());
            } else {
                return dateTime.withYear(now.getYear());
            }
        } else {
            return dateTime.withYear(now.minusYears(1).getYear());
        }
    }

    @Override
    protected Date toFormat(@Nonnull String value, @Nonnull FormatConfig config) throws Exception {
        String input = value.trim();
        String actualPattern = config.trimmedPattern();
        if (StringUtils.isEmpty(actualPattern)) {
            if (RegExp.matches(input, "\\d+")) {
                return new Date(Long.parseLong(input));
            }

            actualPattern = StringUtils.trim(getConf().get("DEFAULT_DATE_PATTERN", Constants.DEFAULT_DATE_PATTERN));
        }

        if (StringUtils.isEmpty(actualPattern)) {
            throw new FormatException("Empty date pattern used to parse datetime.");
        }

        String separator = getConf().get("DEFAULT_DATE_PATTERN_SEPARATOR", ";");
        String[] patterns = actualPattern.split(separator);
        if (patterns.length > 1) {
            patterns = Arrays.stream(patterns).sorted((o1, o2) -> Integer.compare(o2.length(), o1.length())).toArray(String[]::new);
        }

        DateTimeFormats dateTimeFormats = config.getDateTimeFormats();
        DateTime dateTime;
        for (String pattern : patterns) {
            if (pattern.isEmpty()) {
                continue;
            }

            DateTimeFormatter dateFormat = dateTimeFormats.getFormatter(pattern);
            try {
                dateTime = dateFormat.parseDateTime(input);
            } catch (IllegalArgumentException e) {
                logger.warn("Incorrect datetime pattern: {}, input: {}", pattern, input);
                dateTime = adaptPatternFormat(pattern, input, dateTimeFormats);
            } catch (Exception e) {
                logger.warn("Unexpected exception when parsing datetime with pattern: {}, input: {}", pattern, input);
                dateTime = null;
            }

            if (dateTime != null) {
                return adaptDate(dateTime, pattern).toDate();
            }
        }

        throw new FormatException("There was no matched date pattern to parse datetime. input: " + value + ", patterns: " + actualPattern);
    }

    /**
     * 由于一些历史遗留的问题，对格式进行一定程度的适配。 1. hh 等同于 HH。比如：yyyy-MM-dd hh:mm 等同 yyyy-MM-dd HH:mm 2. 局部匹配。比如：输入：2018-07-16
     * 23:38，格式：yyyy-MM-dd，输出：2018-07-16
     */
    private DateTime adaptPatternFormat(String pattern, String input, @Nonnull DateTimeFormats dateTimeFormats) {
        String newPattern = pattern;
        Matcher matcher = HOUR_PATTERN.matcher(newPattern);
        if (matcher.find()) {
            StringBuffer buffer = new StringBuffer();
            do {
                matcher.appendReplacement(buffer, matcher.group().toUpperCase());
            } while (matcher.find());
            matcher.appendTail(buffer);
            newPattern = buffer.toString();

            logger.warn("Adapted datetime parsing. pattern: {}, datetime: {}", newPattern, input);
            DateTimeFormatter formatter = dateTimeFormats.getFormatter(newPattern);
            try {
                return formatter.parseDateTime(input);
            } catch (IllegalArgumentException e) {
                logger.warn("Incorrect adapted datetime pattern: {}, input: {}", newPattern, input);
            } catch (Exception e) {
                logger.warn("Error parsing datetime with possible adapted-pattern: {}, input: {}", newPattern, input);
                return null;
            }
        }

        DateTimeFormatter formatter = dateTimeFormats.getFormatter(newPattern);
        DateTimeParserBucket bucket = new DateTimeParserBucket(0, null, formatter.getLocale(), formatter.getPivotYear(), formatter.getDefaultYear());
        int errorPos = formatter.getParser().parseInto(bucket, input, 0);
        if (errorPos > 0 && errorPos < input.length()) {
            String subText = input.substring(0, errorPos);

            logger.warn("Adapted datetime parsing. pattern: {}, datetime: {}, origin-input: {}", newPattern, subText, input);
            return formatter.parseDateTime(subText);
        }

        return null;
    }
}
