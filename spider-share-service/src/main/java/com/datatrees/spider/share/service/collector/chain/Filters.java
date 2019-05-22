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

import com.datatrees.spider.share.service.collector.chain.search.AddPagingUrlLinkFilter;
import com.datatrees.spider.share.service.collector.chain.search.RecordNetworkTrafficFilter;
import com.datatrees.spider.share.service.collector.chain.search.ResponseStatusFilter;
import com.datatrees.spider.share.service.collector.chain.search.RetryRequestFilter;
import com.datatrees.spider.share.service.collector.chain.search.SleepModeFilter;
import com.datatrees.spider.share.service.collector.chain.urlHandler.AddParserTemplateUrlFilter;
import com.datatrees.spider.share.service.collector.chain.urlHandler.DuplicateRemoveFilter;
import com.datatrees.spider.share.service.collector.chain.urlHandler.EcommerceTitleFilter;
import com.datatrees.spider.share.service.collector.chain.urlHandler.MailBillIllegalSubjectFilter;
import com.datatrees.spider.share.service.collector.chain.urlHandler.MailBillOutboxFilter;
import com.datatrees.spider.share.service.collector.chain.urlHandler.MailBillReceiveTimeFilter;
import com.datatrees.spider.share.service.collector.chain.urlHandler.MailBillSenderFilter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jerry
 * @since 21:01 2018/4/17
 */
public enum Filters {
    LINKNODE {
        @Override
        List<Filter> getFilters() {
            List<Filter> filterList = new ArrayList<>();
            filterList.add(new MailBillReceiveTimeFilter());
            filterList.add(new MailBillIllegalSubjectFilter());
            filterList.add(new MailBillOutboxFilter());
            filterList.add(new MailBillSenderFilter());

            filterList.add(new DuplicateRemoveFilter());
            filterList.add(new AddParserTemplateUrlFilter());
            filterList.add(new EcommerceTitleFilter());

            return filterList;
        }
    },
    SEARCH {
        @Override
        List<Filter> getFilters() {
            List<Filter> filterList = new ArrayList<>();

            filterList.add(new RecordNetworkTrafficFilter());
            filterList.add(new ResponseStatusFilter());
            filterList.add(new AddPagingUrlLinkFilter());
            filterList.add(new RetryRequestFilter());
            filterList.add(new SleepModeFilter());

            return filterList;
        }
    };

    private List<Filter> filters;

    Filters() {
        this.filters = getFilters();
    }

    public void doFilter(Context context) {
        new FilterChain(filters).doFilter(context);
    }

    abstract List<Filter> getFilters();

}
