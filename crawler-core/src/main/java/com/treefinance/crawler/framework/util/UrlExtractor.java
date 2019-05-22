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

import com.treefinance.crawler.framework.consts.Constants;
import com.treefinance.toolkit.util.RegExp;
import org.apache.commons.lang.StringUtils;

import java.util.Collections;
import java.util.List;

/**
 * @author <A HREF="">Cheng Wang</A>
 * @version 1.0
 * @since Feb 25, 2014 10:52:48 AM
 */
public class UrlExtractor {

    private UrlExtractor() {}

    public static List<String> extract(String data) {
        if (StringUtils.isNotEmpty(data)) {
            return RegExp.findAll(data, Constants.URL_REGEX, 1);
        }
        return Collections.emptyList();
    }
}
