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

package com.treefinance.crawler.framework.context.control;

import com.treefinance.crawler.framework.config.enums.BusinessType;
import com.treefinance.crawler.framework.context.AbstractProcessorContext;
import com.treefinance.crawler.framework.context.ExtractorProcessorContext;
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicReference;

/**
 * the decider to decide whether the given crawling business need to be call.
 * 
 * @author Jerry
 * @see IBusinessTypeFilter
 * @see BusinessType
 * @since 20:40 2018/5/9
 */
public final class BusinessTypeDecider {

    private static final AtomicReference<Queue<IBusinessTypeFilter>> REF = new AtomicReference<>();

    private BusinessTypeDecider() {}

    private static Queue<IBusinessTypeFilter> queue() {
        return REF.get();
    }

    private static Queue<IBusinessTypeFilter> ensureQueue() {
        while (true) {
            Queue<IBusinessTypeFilter> collection = queue();
            if (collection != null) {
                return collection;
            }
            collection = new ConcurrentLinkedQueue<>();
            if (REF.compareAndSet(null, collection)) {
                return collection;
            }
        }
    }

    public static void registerFilters(Collection<IBusinessTypeFilter> filters) {
        if (filters != null) {
            filters.forEach(BusinessTypeDecider::registerFilter);
        }
    }

    public static void registerFilter(IBusinessTypeFilter filter) {
        if (filter != null) {
            ensureQueue().add(filter);
        }
    }

    public static boolean support(BusinessType businessType, AbstractProcessorContext processorContext) {
        // 解析阶段不控制过滤
        if (businessType == null || processorContext instanceof ExtractorProcessorContext) {
            return true;
        }

        Queue<IBusinessTypeFilter> queue = queue();
        if (queue != null) {
            for (IBusinessTypeFilter filter : queue) {
                if (filter.isFilter(businessType, processorContext)) {
                    return false;
                }
            }
        }

        return true;
    }

    public static boolean support(String businessType, AbstractProcessorContext processorContext) {
        // 解析阶段不控制过滤
        if (StringUtils.isEmpty(businessType) || processorContext instanceof ExtractorProcessorContext) {
            return true;
        }

        Queue<IBusinessTypeFilter> queue = queue();
        if (queue != null) {
            for (IBusinessTypeFilter filter : queue) {
                if (filter.isFilter(businessType, processorContext)) {
                    return false;
                }
            }
        }

        return true;
    }
}
