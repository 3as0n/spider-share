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