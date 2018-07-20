/*
 * Copyright © 2015 - 2017 杭州大树网络技术有限公司. All Rights Reserved
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

package com.treefinance.crawler.framework.context;

import javax.annotation.Nonnull;

import com.treefinance.crawler.lang.Attributes;

/**
 * @author Jerry
 * @since 11:05 25/01/2018
 */
public interface Context extends Attributes {

    default void addAttribute(@Nonnull final String key, @Nonnull final Object value) {
        setAttribute(key, value);
    }

}