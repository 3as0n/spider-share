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
import com.treefinance.crawler.framework.config.xml.operation.CalculateOperation;
import com.treefinance.crawler.framework.context.function.SpiderRequest;
import com.treefinance.crawler.framework.context.function.SpiderResponse;
import com.treefinance.crawler.framework.exception.InvalidOperationException;
import com.treefinance.crawler.framework.process.operation.Operation;
import com.treefinance.crawler.framework.util.CalculateUtils;
import org.apache.commons.lang.StringUtils;

import javax.annotation.Nonnull;

/**
 * @author <A HREF="">Cheng Wang</A>
 * @version 1.0
 * @since 2015年10月21日 上午10:29:59
 */
public class CalculateOperationImpl extends Operation<CalculateOperation> {

    public CalculateOperationImpl(@Nonnull CalculateOperation operation, @Nonnull FieldExtractor extractor) {
        super(operation, extractor);
    }

    @Override
    protected void validate(@Nonnull CalculateOperation operation, @Nonnull SpiderRequest request, @Nonnull SpiderResponse response) throws Exception {
        super.validate(operation, request, response);

        if (StringUtils.isEmpty(operation.getValue())) {
            throw new InvalidOperationException("Invalid calculate operation! - 'calculate/text()' must not be empty.");
        }
    }

    @Override
    protected Object doOperation(@Nonnull CalculateOperation operation, @Nonnull Object operatingData, @Nonnull SpiderRequest request, @Nonnull SpiderResponse response)
        throws Exception {
        String expression = operation.getValue();

        Object result = CalculateUtils.calculate(expression, request, response, null, null);

        return result == null ? null : result.toString();
    }

}
