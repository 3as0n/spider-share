/*
 * Copyright © 2015 - 2018 杭州大树网络技术有限公司. All Rights Reserved
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.treefinance.crawler.framework.process.search;

import java.util.regex.Pattern;

import com.treefinance.crawler.framework.config.enums.url.FilterType;
import com.treefinance.crawler.framework.config.xml.filter.UrlFilter;
import com.treefinance.toolkit.util.RegExp;

/**
 * @author Jerry
 * @since 17:33 2018/7/31
 */
class RegexUrlFilterDecider implements UrlFilterDecider {

    private Pattern pattern;
    private boolean deny;

    public RegexUrlFilterDecider(UrlFilter urlFilter) {
        this.pattern = RegExp.compile(urlFilter.getFilter());
        this.deny = FilterType.BLACK_LIST.equals(urlFilter.getType());
    }

    @Override
    public boolean deny(String url) {
        if (pattern.matcher(url).find()) {
            return deny;
        }
        return false;
    }
}