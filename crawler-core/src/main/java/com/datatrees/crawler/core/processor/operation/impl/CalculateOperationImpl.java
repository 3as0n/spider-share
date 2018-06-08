/**
 * This document and its contents are protected by copyright 2015 and owned by datatrees.com Inc.
 * The copying and reproduction of this document and/or its content (whether wholly or partly) or
 * any incorporation of the same into any other material in any media or format of any kind is
 * strictly prohibited. All rights are reserved.
 * Copyright (c) datatrees.com Inc. 2015
 */

package com.datatrees.crawler.core.processor.operation.impl;

import javax.annotation.Nonnull;

import com.datatrees.common.pipeline.Request;
import com.datatrees.common.pipeline.Response;
import com.datatrees.crawler.core.domain.config.extractor.FieldExtractor;
import com.datatrees.crawler.core.domain.config.operation.impl.CalculateOperation;
import com.treefinance.crawler.framework.util.CalculateUtils;
import com.datatrees.crawler.core.processor.operation.Operation;
import org.apache.commons.lang.StringUtils;

/**
 * @author <A HREF="mailto:wangcheng@datatrees.com.cn">Cheng Wang</A>
 * @version 1.0
 * @since 2015年10月21日 上午10:29:59
 */
public class CalculateOperationImpl extends Operation<CalculateOperation> {

    public CalculateOperationImpl(@Nonnull CalculateOperation operation, @Nonnull FieldExtractor extractor) {
        super(operation, extractor);
    }

    @Override
    public void process(Request request, Response response) throws Exception {
        CalculateOperation operation = getOperation();
        String expression = operation.getValue();

        Object result = null;
        // regex support get value from context
        if (StringUtils.isNotEmpty(expression)) {
            result = CalculateUtils.calculate(expression, request, response, null, null);
        }
        logger.debug("calculate input: {}, result: {}", expression, result);

        if (result != null) {
            response.setOutPut(result.toString());
        } else {
            response.setOutPut(null);
        }
    }

}
