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

import com.treefinance.crawler.framework.config.xml.extractor.FieldExtractor;
import com.treefinance.crawler.framework.config.xml.operation.ReplaceOperation;
import com.treefinance.crawler.framework.context.function.SpiderRequest;
import com.treefinance.crawler.framework.context.function.SpiderResponse;
import com.treefinance.crawler.framework.expression.ExpressionEngine;
import com.treefinance.crawler.framework.process.operation.Operation;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nonnull;

import java.util.regex.Matcher;

/**
 * @author <A HREF="">Cheng Wang</A>
 * @version 1.0
 * @since Feb 18, 2014 2:58:19 PM
 */
public class ReplaceOperationImpl extends Operation<ReplaceOperation> {

    public ReplaceOperationImpl(@Nonnull ReplaceOperation operation, @Nonnull FieldExtractor extractor) {
        super(operation, extractor);
    }

    @Override
    protected boolean isSkipped(@Nonnull ReplaceOperation operation, @Nonnull SpiderRequest request, @Nonnull SpiderResponse response) {
        boolean empty = StringUtils.isEmpty(operation.getFrom());
        if (empty) {
            logger.warn("empty 'from' value in replace operation and skip.");
        }
        return empty;
    }

    @Override
    protected Object doOperation(@Nonnull ReplaceOperation operation, @Nonnull Object operatingData, @Nonnull SpiderRequest request, @Nonnull SpiderResponse response)
        throws Exception {
        ExpressionEngine expressionEngine = null;

        String from = operation.getFrom();
        if (StringUtils.isNotBlank(from)) {
            expressionEngine = new ExpressionEngine(request, response);
            from = expressionEngine.eval(from);
        }

        logger.debug("Actual replace from: {}", from);

        String to = StringUtils.defaultString(operation.getTo());
        if (StringUtils.isNotBlank(to)) {
            if (expressionEngine == null) {
                expressionEngine = new ExpressionEngine(request, response);
            }
            to = expressionEngine.eval(to);
        }

        logger.debug("Actual replace to: {}", to);

        String input = (String)operatingData;

        return input.replaceAll(from, Matcher.quoteReplacement(to));
    }
}
