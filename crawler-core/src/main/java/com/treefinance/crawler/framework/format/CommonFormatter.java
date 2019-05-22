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

package com.treefinance.crawler.framework.format;

import com.treefinance.crawler.framework.exception.FormatException;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nonnull;

/**
 * @author Jerry
 * @since 16:36 2018/5/14
 */
public abstract class CommonFormatter<R> extends AbstractFormatter<R> {

    @Override
    public final R format(String value, @Nonnull FormatConfig config) throws FormatException {
        logger.debug("Formatting value: {}, config: {}", value, config);
        if (StringUtils.isEmpty(value)) {
            logger.warn("The input value is empty. Skip formatting...");
            return null;
        }

        try {
            return toFormat(value, config);
        } catch (FormatException e) {
            throw e;
        } catch (Exception e) {
            throw new FormatException("Error formatting field value. - value: " + value, e);
        }
    }

    protected abstract R toFormat(@Nonnull String value, @Nonnull FormatConfig config) throws Exception;
}
