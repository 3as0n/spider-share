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

package com.datatrees.spider.share.service.collector.chain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;

/**
 * @author <A HREF="">Cheng Wang</A>
 * @version 1.0
 * @since 2015年7月29日 上午2:35:30
 */
public class FilterChain {

    private static final Logger logger = LoggerFactory.getLogger(FilterChain.class);

    private List<Filter> filters;

    private int pos = 0;

    public FilterChain(List<Filter> filters) {
        if (filters != null) {
            this.filters = filters;
        } else {
            this.filters = Collections.emptyList();
        }
    }

    public void doFilter(Context context) {
        if (this.pos < this.filters.size()) {
            Filter nextFilter = this.filters.get(pos++);

            if (logger.isTraceEnabled()) {
                logger.trace("Call filter: {}", nextFilter.getClass().getName());
            }

            nextFilter.doFilter(context, this);
        }
    }
}
