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
import com.treefinance.crawler.framework.config.xml.operation.TripleOperation;
import com.treefinance.crawler.framework.context.ResponseUtil;
import com.treefinance.crawler.framework.context.function.SpiderRequest;
import com.treefinance.crawler.framework.context.function.SpiderRequestFactory;
import com.treefinance.crawler.framework.context.function.SpiderResponse;
import com.treefinance.crawler.framework.context.function.SpiderResponseFactory;
import com.treefinance.crawler.framework.context.pipeline.InvokeException;
import com.treefinance.crawler.framework.exception.ResultEmptyException;
import com.treefinance.crawler.framework.process.fields.FieldExtractResult;
import com.treefinance.crawler.framework.process.fields.FieldExtractResultSet;
import org.junit.Test;

/**
 * @author Jerry
 * @since 13:59 2018/7/6
 */
public class TripleOperationImplTest {

    @Test
    public void doOperation() throws InvokeException, ResultEmptyException {
        TripleOperation tripleOperation = new TripleOperation();
        tripleOperation.setTripleType("gt");
        tripleOperation.setValue("${totalNum}>9?9:${this}");
        SpiderRequest request = SpiderRequestFactory.make();
        request.setInput("01");

        FieldExtractResultSet resultMap = new FieldExtractResultSet();
        resultMap.put("totalNum", new FieldExtractResult("06"));
        SpiderResponse response = SpiderResponseFactory.make();
        ResponseUtil.setFieldExtractResultSet(response, resultMap);

        TripleOperationImpl operation = new TripleOperationImpl(tripleOperation, new FieldExtractor());
        operation.invoke(request, response);
    }
}