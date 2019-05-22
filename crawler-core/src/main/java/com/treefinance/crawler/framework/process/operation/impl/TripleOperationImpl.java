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

import com.treefinance.crawler.framework.config.enums.operation.triple.TripleType;
import com.treefinance.crawler.framework.config.xml.extractor.FieldExtractor;
import com.treefinance.crawler.framework.config.xml.operation.TripleOperation;
import com.treefinance.crawler.framework.context.function.SpiderRequest;
import com.treefinance.crawler.framework.context.function.SpiderResponse;
import com.treefinance.crawler.framework.exception.InvalidOperationException;
import com.treefinance.crawler.framework.expression.StandardExpression;
import com.treefinance.crawler.framework.process.operation.Operation;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nonnull;

public class TripleOperationImpl extends Operation<TripleOperation> {

    public TripleOperationImpl(@Nonnull TripleOperation operation, @Nonnull FieldExtractor extractor) {
        super(operation, extractor);
    }

    @Override
    protected boolean isSkipped(@Nonnull TripleOperation operation, @Nonnull SpiderRequest request, @Nonnull SpiderResponse response) {
        // invalid xpath operation and skip
        boolean flag = StringUtils.isBlank(operation.getValue());
        if (flag) {
            logger.warn("Empty expression of triple operation and skip.");
        }
        return flag;
    }

    @Override
    protected Object doOperation(@Nonnull TripleOperation operation, @Nonnull Object operatingData, @Nonnull SpiderRequest request, @Nonnull SpiderResponse response)
        throws Exception {
        String expression = operation.getValue();

        // ${this}=${a}?${b}:${c}
        TripleType type = operation.getTripleType();
        if (type == null) {
            type = TripleType.EQ;
        }

        String exp = type.getExpression();
        int i = expression.indexOf(exp);
        if (i == -1) {
            throw new InvalidOperationException("Invalid triple operation! - Triple expression was incorrect.");
        }

        String param1 = expression.substring(0, i);

        i = i + exp.length();
        int j = expression.indexOf("?", i);
        if (i == -1) {
            throw new InvalidOperationException("Invalid triple operation! - Triple expression was incorrect.");
        }

        String param2 = expression.substring(i, j);

        i = j + 1;
        j = expression.indexOf(":", i);
        if (j == -1) {
            throw new InvalidOperationException("Invalid triple operation! - Triple expression was incorrect.");
        }

        String result1 = expression.substring(i, j);
        String result2 = expression.substring(j + 1);

        String input = (String)operatingData;
        param1 = evalExp(param1, input, request, response);
        param2 = evalExp(param2, input, request, response);
        result1 = evalExp(result1, input, request, response);
        result2 = evalExp(result2, input, request, response);

        return type.calculate(param1, param2, result1, result2);
    }

    private String evalExp(String value, String operatingData, SpiderRequest request, SpiderResponse response) {
        String val = StringUtils.replace(value, "${this}", operatingData);

        return StandardExpression.eval(val, request, response);
    }
}
