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
import com.treefinance.crawler.framework.config.xml.operation.ReturnMatchOperation;
import com.treefinance.crawler.framework.context.function.SpiderRequest;
import com.treefinance.crawler.framework.context.function.SpiderResponse;
import com.treefinance.crawler.framework.expression.StandardExpression;
import com.treefinance.crawler.framework.process.operation.Operation;
import org.apache.commons.lang.StringUtils;

import javax.annotation.Nonnull;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * @author <A HREF="mailto:zhangjiachen@datatrees.com.cn">zhangjiachen</A>
 * @version 1.0
 * @since 2016年5月30日 下午8:33:11
 */
public class ReturnMatchOperationImpl extends Operation<ReturnMatchOperation> {

    public ReturnMatchOperationImpl(@Nonnull ReturnMatchOperation operation, @Nonnull FieldExtractor extractor) {
        super(operation, extractor);
    }

    @Override
    protected Object doOperation(@Nonnull ReturnMatchOperation operation, @Nonnull Object operatingData, @Nonnull SpiderRequest request, @Nonnull SpiderResponse response)
        throws Exception {
        String input = (String)operatingData;

        String value = StandardExpression.eval(operation.getValue(), request, response);

        logger.debug("return match keys: {}", value);

        if (StringUtils.isBlank(value)) {
            return null;
        }

        String[] matchedKeys = value.split(",");

        String result = Arrays.stream(matchedKeys).map(String::trim).filter(key -> !key.isEmpty() && input.contains(key)).collect(Collectors.joining(","));

        return result.isEmpty() ? null : result;
    }

}
