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

package com.treefinance.crawler.framework.format;

import com.treefinance.crawler.framework.context.function.SpiderRequest;
import com.treefinance.crawler.framework.context.function.SpiderResponse;
import com.treefinance.crawler.framework.exception.FormatException;

import javax.annotation.Nonnull;

/**
 * @author Jerry
 * @since 15:49 2018/5/14
 */
public interface Formatter<R> {

    boolean supportResultType(Object value);

    default R format(String value, String pattern, @Nonnull SpiderRequest request, @Nonnull SpiderResponse response) throws FormatException {
        return format(value, new FormatConfig(request, response, pattern));
    }

    R format(String value, @Nonnull FormatConfig config) throws FormatException;
}
