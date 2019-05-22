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

package com.treefinance.crawler.framework.process.operation.impl;

import com.treefinance.crawler.framework.consts.Constants;
import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <A HREF="">Cheng Wang</A>
 * @version 1.0
 * @since Mar 7, 2014 11:03:02 AM
 */
public class ParserURLCombiner {

    public static String encodeUrl(String url, String... args) {
        if (ArrayUtils.isEmpty(args)) {
            return url;
        }

        StringBuilder sb = new StringBuilder(url);
        for (String arg : args) {
            sb.append(Constants.PARSER_SPLIT).append(arg);
        }

        return sb.toString();
    }

    public static String[] decodeParserUrl(String url) {
        List<String> splits = new ArrayList<String>();
        String split = Constants.PARSER_SPLIT;
        int start = 0;
        int next = -1;
        int length = url.length();
        while ((next = url.indexOf(split, start)) > -1) {
            String tmp = url.substring(start, next);
            start = next + split.length();
            splits.add(tmp);
        }
        if (start <= length) {
            splits.add(url.substring(start, length));
        }
        return splits.toArray(new String[] {});
    }
}
