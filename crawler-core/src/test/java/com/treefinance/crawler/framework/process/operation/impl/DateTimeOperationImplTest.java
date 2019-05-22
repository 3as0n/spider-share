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
import com.treefinance.crawler.framework.config.xml.operation.DateTimeOperation;
import com.treefinance.crawler.framework.context.function.SpiderRequest;
import com.treefinance.crawler.framework.context.function.SpiderRequestFactory;
import com.treefinance.crawler.framework.context.function.SpiderResponse;
import com.treefinance.crawler.framework.context.function.SpiderResponseFactory;
import com.treefinance.crawler.framework.context.pipeline.InvokeException;
import com.treefinance.crawler.framework.exception.ResultEmptyException;
import org.junit.Test;

/**
 * @author Jerry
 * @since 11:30 2018/7/16
 */
public class DateTimeOperationImplTest {

    @Test
    public void invoke() throws InvokeException, ResultEmptyException {
        FieldExtractor extractor = new FieldExtractor();
        DateTimeOperation operation = new DateTimeOperation();
        operation.setBaseType("now");
        operation.setDateTimeFieldType("month");
        operation.setOffset("-6");
        operation.setFormat("yyyy-MM-dd");

        DateTimeOperationImpl impl = new DateTimeOperationImpl(operation, extractor);

        SpiderRequest request = SpiderRequestFactory.make();
        request.setInput("1967-01-01");
        SpiderResponse response = SpiderResponseFactory.make();
        impl.invoke(request, response);

        operation = new DateTimeOperation();
        operation.setBaseType("custom");
        operation.setSourceFormat("yyyy-MM-dd");
        operation.setFormat("timestamp");
        impl = new DateTimeOperationImpl(operation, extractor);
        impl.invoke(request, response);
        Object outPut = response.getOutPut();
        System.out.println(outPut);
    }
}