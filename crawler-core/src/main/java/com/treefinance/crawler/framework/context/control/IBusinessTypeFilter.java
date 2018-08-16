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

package com.treefinance.crawler.framework.context.control;

import javax.annotation.Nonnull;

import com.treefinance.crawler.framework.config.enums.BusinessType;
import com.datatrees.crawler.core.processor.AbstractProcessorContext;

/**
 * the filter for crawling business.
 * @author Jerry
 * @see BusinessType
 * @since 20:37 2018/5/9
 */
public interface IBusinessTypeFilter {

    boolean isFilter(String businessType, @Nonnull AbstractProcessorContext context);

    default boolean isFilter(BusinessType businessType, @Nonnull AbstractProcessorContext context) {
        return businessType != null && isFilter(businessType.getCode(), context);
    }
}
