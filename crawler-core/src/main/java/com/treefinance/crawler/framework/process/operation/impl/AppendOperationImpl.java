package com.treefinance.crawler.framework.process.operation.impl;

import javax.annotation.Nonnull;

import com.datatrees.crawler.core.domain.config.extractor.FieldExtractor;
import com.datatrees.crawler.core.domain.config.operation.impl.AppendOperation;
import com.treefinance.crawler.framework.context.function.SpiderRequest;
import com.treefinance.crawler.framework.context.function.SpiderResponse;
import com.treefinance.crawler.framework.expression.StandardExpression;
import com.treefinance.crawler.framework.process.operation.Operation;
import org.apache.commons.lang.StringUtils;

public class AppendOperationImpl extends Operation<AppendOperation> {

    public AppendOperationImpl(@Nonnull AppendOperation operation, @Nonnull FieldExtractor extractor) {
        super(operation, extractor);
    }

    @Override
    protected Object doOperation(@Nonnull AppendOperation operation, @Nonnull Object operatingData, @Nonnull SpiderRequest request, @Nonnull SpiderResponse response) throws Exception {
        String value = operation.getValue();

        value = StandardExpression.eval(value, request, response);

        logger.debug("Actual append text: {}", value);

        if (StringUtils.isEmpty(value)) {
            return operatingData;
        }

        String input = (String) operatingData;

        int index = operation.getIndex();
        if (index < 0 || index >= input.length()) {
            return input + value;
        } else if(index == 0){
            return value + input;
        } else {
            String prefix = input.substring(0, index);
            String suffix = input.substring( index, input.length());

            return prefix + value + suffix;
        }
    }

}
