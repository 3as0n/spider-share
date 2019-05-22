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