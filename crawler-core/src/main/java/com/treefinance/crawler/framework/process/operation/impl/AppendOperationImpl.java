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

package com.treefinance.crawler.framework.process.operation.impl;

import com.treefinance.crawler.framework.config.xml.extractor.FieldExtractor;
import com.treefinance.crawler.framework.config.xml.operation.AppendOperation;
import com.treefinance.crawler.framework.context.function.SpiderRequest;
import com.treefinance.crawler.framework.context.function.SpiderResponse;
import com.treefinance.crawler.framework.expression.StandardExpression;
import com.treefinance.crawler.framework.process.operation.Operation;
import org.apache.commons.lang.StringUtils;

import javax.annotation.Nonnull;

public class AppendOperationImpl extends Operation<AppendOperation> {

    public AppendOperationImpl(@Nonnull AppendOperation operation, @Nonnull FieldExtractor extractor) {
        super(operation, extractor);
    }

    @Override
    protected Object doOperation(@Nonnull AppendOperation operation, @Nonnull Object operatingData, @Nonnull SpiderRequest request, @Nonnull SpiderResponse response)
        throws Exception {
        String value = operation.getValue();

        value = StandardExpression.eval(value, request, response);

        logger.debug("Actual append text: {}", value);

        if (StringUtils.isEmpty(value)) {
            return operatingData;
        }

        String input = (String)operatingData;

        int index = operation.getIndex();
        if (index < 0 || index >= input.length()) {
            return input + value;
        } else if (index == 0) {
            return value + input;
        } else {
            String prefix = input.substring(0, index);
            String suffix = input.substring(index, input.length());

            return prefix + value + suffix;
        }
    }

}
