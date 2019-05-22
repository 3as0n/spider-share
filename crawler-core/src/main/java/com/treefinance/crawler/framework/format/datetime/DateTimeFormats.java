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

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Jerry
 * @since 21:19 2018/7/16
 */
public class DateTimeFormats {

    public static final int BASE_YEAR = 1970;
    private Map<String, DateTimeFormatter> formatterMap = new HashMap<>();

    public DateTimeFormatter getFormatter(String pattern) {
        return formatterMap.computeIfAbsent(pattern, p -> DateTimeFormat.forPattern(p).withDefaultYear(BASE_YEAR));
    }

}
