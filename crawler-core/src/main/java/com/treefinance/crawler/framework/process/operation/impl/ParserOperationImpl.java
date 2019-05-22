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
import com.treefinance.crawler.framework.config.xml.operation.ParserOperation;
import com.treefinance.crawler.framework.config.xml.parser.Parser;
import com.treefinance.crawler.framework.context.function.SpiderRequest;
import com.treefinance.crawler.framework.context.function.SpiderResponse;
import com.treefinance.crawler.framework.exception.InvalidOperationException;
import com.treefinance.crawler.framework.process.operation.Operation;

import javax.annotation.Nonnull;

/**
 * @author <A HREF="">Cheng Wang</A>
 * @version 1.0
 * @since Feb 18, 2014 2:43:36 PM
 */
public class ParserOperationImpl extends Operation<ParserOperation> {

    private static final String URL_FIELD = "url";

    public ParserOperationImpl(@Nonnull ParserOperation operation, @Nonnull FieldExtractor extractor) {
        super(operation, extractor);
    }

    @Override
    protected void validate(@Nonnull ParserOperation operation, @Nonnull SpiderRequest request, @Nonnull SpiderResponse response) throws Exception {
        super.validate(operation, request, response);

        if (operation.getParser() == null) {
            throw new InvalidOperationException("Invalid parser operation! - Reference 'parser' must not be null.");
        }
    }

    @Override
    protected Object doOperation(@Nonnull ParserOperation operation, @Nonnull Object operatingData, @Nonnull SpiderRequest request, @Nonnull SpiderResponse response)
        throws Exception {
        Parser parser = operation.getParser();

        FieldExtractor field = getExtractor();
        logger.debug("field name: {}", field);

        String fieldName = field.getField();
        boolean needRequest = !fieldName.toLowerCase().endsWith(URL_FIELD);
        boolean needReturnUrlList = !needRequest && fieldName.length() == URL_FIELD.length();

        logger.debug("invoke parser process: {}", field);
        try {
            ParserHandler parserHandler = new ParserHandler(parser, needRequest, needReturnUrlList);
            return parserHandler.parse((String)operatingData, request, response);
        } finally {
            logger.debug("success invoke parser process: {}", field);
        }
    }

}
