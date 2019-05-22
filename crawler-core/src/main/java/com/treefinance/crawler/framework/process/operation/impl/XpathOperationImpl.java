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
import com.treefinance.crawler.framework.config.xml.operation.XpathOperation;
import com.treefinance.crawler.framework.context.function.SpiderRequest;
import com.treefinance.crawler.framework.context.function.SpiderResponse;
import com.treefinance.crawler.framework.exception.InvalidOperationException;
import com.treefinance.crawler.framework.expression.StandardExpression;
import com.treefinance.crawler.framework.process.operation.Operation;
import com.treefinance.crawler.framework.util.xpath.XPathUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nonnull;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author <A HREF="">Cheng Wang</A>
 * @version 1.0
 * @since Feb 18, 2014 2:58:48 PM
 */
public class XpathOperationImpl extends Operation<XpathOperation> {

    public XpathOperationImpl(@Nonnull XpathOperation operation, @Nonnull FieldExtractor extractor) {
        super(operation, extractor);
    }

    @Override
    protected void validate(@Nonnull XpathOperation operation, @Nonnull SpiderRequest request, @Nonnull SpiderResponse response) throws Exception {
        super.validate(operation, request, response);

        if (StringUtils.isEmpty(operation.getXpath())) {
            throw new InvalidOperationException("Invalid xpath operation! - 'xpath/text()' must not be empty.");
        }
    }

    @Override
    protected Object doOperation(@Nonnull XpathOperation operation, @Nonnull Object operatingData, @Nonnull SpiderRequest request, @Nonnull SpiderResponse response)
        throws Exception {
        String xpath = operation.getXpath();

        xpath = StandardExpression.eval(xpath, request, response);

        logger.debug("Actual xpath: {}", xpath);

        if (StringUtils.isBlank(xpath)) {
            throw new InvalidOperationException("Incorrect xpath! \nOriginal Xpath: " + operation.getXpath() + "\nActual Xpath: " + xpath);
        }

        String result;
        List<String> segments = XPathUtil.getXpath(xpath, (String)operatingData);
        if (CollectionUtils.isNotEmpty(segments)) {
            result = segments.stream().collect(Collectors.joining());
        } else {
            logger.warn("xpath extract empty content! - {}", xpath);
            result = StringUtils.EMPTY;
        }

        if (result.isEmpty() && BooleanUtils.isTrue(operation.getEmptyToNull())) {
            result = null;
        }

        return result;
    }
}
