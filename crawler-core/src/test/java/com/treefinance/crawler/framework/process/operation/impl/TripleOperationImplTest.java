package com.treefinance.crawler.framework.process.operation.impl;

import com.treefinance.crawler.framework.context.pipeline.InvokeException;
import com.datatrees.common.pipeline.Request;
import com.datatrees.common.pipeline.Response;
import com.datatrees.crawler.core.domain.config.extractor.FieldExtractor;
import com.datatrees.crawler.core.domain.config.operation.impl.TripleOperation;
import com.datatrees.crawler.core.processor.common.ResponseUtil;
import com.datatrees.crawler.core.processor.common.exception.ResultEmptyException;
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
        Request request = new Request();
        request.setInput("01");

        FieldExtractResultSet resultMap = new FieldExtractResultSet();
        resultMap.put("totalNum", new FieldExtractResult("06"));
        Response response = new Response();
        ResponseUtil.setFieldExtractResultSet(response, resultMap);

        TripleOperationImpl operation = new TripleOperationImpl(tripleOperation, new FieldExtractor());
        operation.invoke(request, response);
    }
}