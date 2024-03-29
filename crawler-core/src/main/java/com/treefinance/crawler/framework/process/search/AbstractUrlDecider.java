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

import org.apache.commons.collections4.CollectionUtils;

import javax.annotation.Nonnull;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jerry
 * @date 2019-06-25 22:28
 */
abstract class AbstractUrlDecider implements UrlDecider {
    private final List<UrlMatcher> urlMatchers;

    public AbstractUrlDecider() {
        this.urlMatchers = new ArrayList<>();
    }

    public AbstractUrlDecider(List<UrlMatcher> urlMatchers) {
        this.urlMatchers = urlMatchers;
    }

    @Override
    public final void addUrlRule(@Nonnull UrlMatcher urlMatcher) {
        this.urlMatchers.add(urlMatcher);
    }

    @Override
    public final boolean match(String url) {
        if (CollectionUtils.isNotEmpty(urlMatchers)) {
            for (UrlMatcher urlMatcher : urlMatchers) {
                if (urlMatcher.match(url)) {
                    return true;
                }
            }
        }

        return false;
    }
}
