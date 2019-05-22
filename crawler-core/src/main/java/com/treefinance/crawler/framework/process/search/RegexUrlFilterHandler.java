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

package com.treefinance.crawler.framework.process.search;

import com.treefinance.crawler.framework.config.xml.filter.UrlFilter;
import org.apache.commons.collections4.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Jerry
 * @since 17:28 2018/7/31
 */
public class RegexUrlFilterHandler implements UrlFilterHandler {

    private final List<UrlFilterDecider> deciders;

    public RegexUrlFilterHandler(List<UrlFilter> filterList) {
        if (CollectionUtils.isNotEmpty(filterList)) {
            deciders = filterList.stream().map(RegexUrlFilterDecider::new).collect(Collectors.toList());
        } else {
            deciders = Collections.emptyList();
        }
    }

    @Override
    public boolean filter(String url) {
        if (CollectionUtils.isNotEmpty(deciders)) {
            for (UrlFilterDecider decider : deciders) {
                if (decider.deny(url)) {
                    return true;
                }
            }
        }

        return false;
    }
}
