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

package com.treefinance.crawler.framework.process.search;

import com.treefinance.crawler.framework.config.enums.url.FilterType;
import com.treefinance.crawler.framework.config.xml.filter.UrlFilter;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Jerry
 * @since 17:28 2018/7/31
 */
public class DefaultUrlFilterHandler implements UrlFilterHandler {

    private UrlWhitelist whitelist;
    private UrlBlacklist blacklist;

    public DefaultUrlFilterHandler(List<UrlFilter> filterList) {
        if (CollectionUtils.isNotEmpty(filterList)) {
            Map<FilterType, List<UrlMatcher>> map =
                filterList.stream().collect(Collectors.groupingBy(UrlFilter::getType, Collectors.mapping(filter -> new RegexUrlMatcher(filter.getFilter()), Collectors.toList())));

            List<UrlMatcher> urlMatchers = map.get(FilterType.WHITE_LIST);
            if (urlMatchers != null) {
                this.whitelist = new RegexUrlWhitelist(urlMatchers);
            }

            urlMatchers = map.get(FilterType.BLACK_LIST);
            if (urlMatchers != null) {
                blacklist = new RegexUrlBlacklist(urlMatchers);
            }
        }
    }

    @Override
    public boolean filter(String url) {
        // 没有设置白名单和黑名单规则，全部允许
        if (whitelist == null && blacklist == null) {
            return false;
        }

        // 没有设置黑名单规则，按白名单规则执行
        if (blacklist == null) {
            return !whitelist.match(url);
        }

        // 没有设置白名单规则，按黑名单规则执行
        if (whitelist == null) {
            return blacklist.match(url);
        }

        // 判断是否同时中了白名单和黑名单，以黑名单优先
        if (whitelist.match(url)) {
            return blacklist.match(url);
        }

        return true;
    }
}
