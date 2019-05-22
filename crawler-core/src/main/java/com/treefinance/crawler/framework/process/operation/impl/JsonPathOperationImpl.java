/*
 * Copyright © 2015 - 2018 杭州大树网络技术有限公司. All Rights Reserved
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

import com.jayway.jsonpath.InvalidJsonException;
import com.jayway.jsonpath.InvalidPathException;
import com.jayway.jsonpath.PathNotFoundException;
import com.treefinance.crawler.framework.config.xml.extractor.FieldExtractor;
import com.treefinance.crawler.framework.config.xml.operation.JsonPathOperation;
import com.treefinance.crawler.framework.context.function.SpiderRequest;
import com.treefinance.crawler.framework.context.function.SpiderResponse;
import com.treefinance.crawler.framework.exception.InvalidDataException;
import com.treefinance.crawler.framework.exception.InvalidOperationException;
import com.treefinance.crawler.framework.expression.StandardExpression;
import com.treefinance.crawler.framework.process.operation.Operation;
import com.treefinance.crawler.framework.util.json.JsonPathUtil;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nonnull;

/**
 * @author Jerry
 * @datetime 2015-07-17 20:02
 */
public class JsonPathOperationImpl extends Operation<JsonPathOperation> {

    public JsonPathOperationImpl(@Nonnull JsonPathOperation operation, @Nonnull FieldExtractor extractor) {
        super(operation, extractor);
    }

    @Override
    protected void validate(@Nonnull JsonPathOperation operation, @Nonnull SpiderRequest request, @Nonnull SpiderResponse response) throws Exception {
        super.validate(operation, request, response);

        if (StringUtils.isEmpty(operation.getJsonpath())) {
            throw new InvalidOperationException("Invalid jsonpath operation! - 'jsonpath/text()' must not be empty.");
        }
    }

    @Override
    protected Object doOperation(@Nonnull JsonPathOperation operation, @Nonnull Object operatingData, @Nonnull SpiderRequest request, @Nonnull SpiderResponse response)
        throws Exception {
        String jsonpath = operation.getJsonpath();

        jsonpath = StandardExpression.eval(jsonpath, request, response);

        logger.debug("Actual jsonpath: {}", jsonpath);

        if (StringUtils.isBlank(jsonpath)) {
            throw new InvalidOperationException("Incorrect jsonpath! \nOriginal Jsonpath: " + operation.getJsonpath() + "\nActual Jsonpath: " + jsonpath);
        }

        String input = (String)operatingData;

        String result;
        try {
            result = JsonPathUtil.readAsString(input, jsonpath);
        } catch (InvalidJsonException | PathNotFoundException e) {
            throw new InvalidDataException(
                "Incorrect json data! >> " + e.getMessage() + "\nOriginal Jsonpath: " + operation.getJsonpath() + "\nActual Jsonpath: " + jsonpath + "\nInput:\n" + input);
        } catch (InvalidPathException e) {
            throw new InvalidOperationException("Incorrect jsonpath! >> " + e.getMessage() + "\nOriginal Jsonpath: " + operation.getJsonpath() + "\nActual Jsonpath: " + jsonpath);
        } catch (Exception e) {
            throw new InvalidOperationException(
                "Error parsing with jsonpath! >> " + e.getMessage() + "\nOriginal Jsonpath: " + operation.getJsonpath() + "\nActual Jsonpath: " + jsonpath + "\nInput:\n" + input);
        }

        if (result.isEmpty() && BooleanUtils.isTrue(operation.getEmptyToNull())) {
            result = null;
        }

        return result;
    }
}
