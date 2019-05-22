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

package com.treefinance.crawler.framework.process.operation.impl;

import com.treefinance.crawler.framework.config.xml.extractor.FieldExtractor;
import com.treefinance.crawler.framework.config.xml.operation.RegexOperation;
import com.treefinance.crawler.framework.context.function.SpiderRequest;
import com.treefinance.crawler.framework.context.function.SpiderResponse;
import com.treefinance.crawler.framework.exception.InvalidOperationException;
import com.treefinance.crawler.framework.expression.StandardExpression;
import com.treefinance.crawler.framework.process.operation.Operation;
import com.treefinance.toolkit.util.RegExp;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nonnull;

import java.util.regex.Matcher;

/**
 * @author <A HREF="">Cheng Wang</A>
 * @version 1.0
 * @since Feb 18, 2014 2:57:53 PM
 */
public class RegexOperationImpl extends Operation<RegexOperation> {

    public RegexOperationImpl(@Nonnull RegexOperation operation, @Nonnull FieldExtractor extractor) {
        super(operation, extractor);
    }

    @Override
    protected void validate(@Nonnull RegexOperation operation, @Nonnull SpiderRequest request, @Nonnull SpiderResponse response) throws Exception {
        super.validate(operation, request, response);

        if (StringUtils.isEmpty(operation.getRegex())) {
            throw new InvalidOperationException("Invalid regex operation! - 'regex/text()' must not be empty.");
        }
    }

    @Override
    protected Object doOperation(@Nonnull RegexOperation operation, @Nonnull Object operatingData, @Nonnull SpiderRequest request, @Nonnull SpiderResponse response)
        throws Exception {
        String regex = StandardExpression.eval(operation.getRegex(), request, response);

        logger.debug("Actual regexp: {}", regex);

        String input = (String)operatingData;

        Integer groupIndex = operation.getGroupIndex();
        if (groupIndex == null || groupIndex < 0) {
            Matcher result = RegExp.getMatcher(regex, input);
            if (result.find()) {
                return result;
            }

            return null;
        }

        logger.debug("regex: {}, index: {}", regex, groupIndex);

        return RegExp.group(input, regex, groupIndex, null);
    }
}
