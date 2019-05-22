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
import com.treefinance.crawler.framework.config.xml.operation.MatchGroupOperation;
import com.treefinance.crawler.framework.context.function.SpiderRequest;
import com.treefinance.crawler.framework.context.function.SpiderResponse;
import com.treefinance.crawler.framework.exception.InvalidOperationException;
import com.treefinance.crawler.framework.process.operation.Operation;
import com.treefinance.crawler.framework.util.FieldUtils;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nonnull;

import java.util.regex.Matcher;

/**
 * @author <A HREF="">Cheng Wang</A>
 * @version 1.0
 * @since Feb 18, 2014 2:58:34 PM
 */
public class MatchGroupOperationImpl extends Operation<MatchGroupOperation> {

    public MatchGroupOperationImpl(@Nonnull MatchGroupOperation operation, @Nonnull FieldExtractor extractor) {
        super(operation, extractor);
    }

    @Override
    protected void validate(@Nonnull MatchGroupOperation operation, @Nonnull SpiderRequest request, @Nonnull SpiderResponse response) throws Exception {
        super.validate(operation, request, response);

        if (StringUtils.isEmpty(operation.getSourceId())) {
            throw new InvalidOperationException("Invalid match-group operation! - Attribute 'source' must not be empty.");
        }
    }

    @Override
    protected Object doOperation(@Nonnull MatchGroupOperation operation, @Nonnull Object operatingData, @Nonnull SpiderRequest request, @Nonnull SpiderResponse response)
        throws Exception {
        Matcher matcher = (Matcher)FieldUtils.getSourceFieldValue(operation.getSourceId(), request, response);

        String result = null;
        if (matcher != null) {
            Integer index = operation.getGroupIndex();
            if (index == null) {
                index = 0;
            }
            result = matcher.group(index);
        }

        return result;
    }

}
