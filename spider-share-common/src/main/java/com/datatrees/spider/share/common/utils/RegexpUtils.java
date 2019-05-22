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

package com.datatrees.spider.share.common.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexpUtils {

    public static String select(String source, String regex, int groupIndex) {
        return selectFirst(source, regex, groupIndex);
    }

    public static String selectFirst(String source, String regex, int groupIndex) {
        Matcher matcher = Pattern.compile(regex).matcher(source);
        if (matcher.find()) {
            return matcher.group(groupIndex);
        }
        return null;
    }

    public static List<String> selectList(String source, String regex) {
        Matcher matcher = Pattern.compile(regex).matcher(source);
        List<String> list = new ArrayList<>();
        while (matcher.find()) {
            list.add(matcher.group());
        }
        return list;
    }

    public static String selectLast(String source, String regex, int groupIndex) {
        Matcher matcher = Pattern.compile(regex).matcher(source);
        String selectText = null;
        while (matcher.find()) {
            selectText = matcher.group();
        }
        matcher = Pattern.compile(regex).matcher(selectText);
        if (matcher.find()) {
            return matcher.group(groupIndex);
        }
        return null;
    }
}
