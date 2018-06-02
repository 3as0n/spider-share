package com.datatrees.crawler.core.processor.operation.impl;

import java.util.regex.Pattern;

import com.datatrees.common.pipeline.Request;
import com.datatrees.common.pipeline.Response;
import com.datatrees.crawler.core.domain.config.operation.impl.TripleOperation;
import com.datatrees.crawler.core.domain.config.operation.impl.triple.TripleType;
import com.datatrees.crawler.core.processor.common.CalculateUtil;
import com.datatrees.crawler.core.processor.operation.Operation;
import com.datatrees.crawler.core.processor.operation.OperationHelper;
import com.treefinance.crawler.framework.expression.StandardExpression;
import com.treefinance.toolkit.util.RegExp;
import org.apache.commons.lang.StringUtils;

public class TripleOperationImpl extends Operation<TripleOperation> {

    @Override
    public void process(Request request, Response response) throws Exception {
        TripleOperation operation = getOperation();
        // ${this}=${a}?${b}:${c}
        String expression = operation.getValue();
        TripleType type = operation.getTripleType();
        if (type == null) {
            type = TripleType.EQ;
        }

        String orginal = OperationHelper.getStringInput(request, response);

        logger.debug("triple expression: {}", expression);

        String firstParams = StringUtils.substringBefore(expression, type.getExpression());
        String secondParams = StringUtils.substringBetween(expression, type.getExpression(), "?");
        String firstResult = StringUtils.substringBetween(expression, "?", ":");
        String secondResult = StringUtils.substringAfterLast(expression, ":");

        firstParams = replaceFromContext(firstParams, orginal, request, response);
        secondParams = replaceFromContext(secondParams, orginal, request, response);
        firstResult = replaceFromContext(firstResult, orginal, request, response);
        secondResult = replaceFromContext(secondResult, orginal, request, response);

        String result = this.doTriple(type, firstParams, secondParams, firstResult, secondResult);

        logger.debug("original: {}, dest: {}", orginal, result);

        response.setOutPut(result);
    }

    private String doTriple(TripleType type, String firstParams, String secondParams, String firstResult, String secondResult) {
        String result = secondResult;
        if (firstParams != null && secondParams != null) {
            switch (type) {
                case EQ:
                    if (firstParams.trim().equals(secondParams.trim())) {
                        result = firstResult;
                    }
                    break;
                case NE:
                    if (!firstParams.equals(secondParams)) {
                        result = firstResult;
                    }
                    break;
                case GT:
                    if (CalculateUtil.calculate(firstParams, 0d) > CalculateUtil.calculate(secondParams, 0d)) {
                        result = CalculateUtil.calculate(firstResult, 0) + "";
                    } else {
                        result = CalculateUtil.calculate(secondResult, 0) + "";
                    }
                    break;
                case LT:
                    if (CalculateUtil.calculate(firstParams, 0d) < CalculateUtil.calculate(secondParams, 0d)) {
                        result = CalculateUtil.calculate(firstResult, 0) + "";
                    } else {
                        result = CalculateUtil.calculate(secondResult, 0) + "";
                    }
                    break;
                case GE:
                    if (CalculateUtil.calculate(firstParams, 0d) >= CalculateUtil.calculate(secondParams, 0d)) {
                        result = CalculateUtil.calculate(firstResult, 0) + "";
                    } else {
                        result = CalculateUtil.calculate(secondResult, 0) + "";
                    }
                    break;
                case LE:
                    if (CalculateUtil.calculate(firstParams, 0d) <= CalculateUtil.calculate(secondParams, 0d)) {
                        result = CalculateUtil.calculate(firstResult, 0) + "";
                    } else {
                        result = CalculateUtil.calculate(secondResult, 0) + "";
                    }
                    break;
                case REGEX:
                    if (RegExp.find(firstParams, secondParams)) {
                        result = firstResult;
                    }
                    break;
                case CONTAINS:
                    if (RegExp.find(firstParams, secondParams, Pattern.CASE_INSENSITIVE)) {
                        result = firstResult;
                    }
                    break;
                default:
                    break;
            }
        }
        return result;
    }

    private String replaceFromContext(String params, String orginal, Request request, Response response) {
        String val = StringUtils.replace(params, "${this}", orginal);

        return StandardExpression.eval(val, request, response);
    }
}
