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

package com.treefinance.crawler.framework.process.fields;

import com.treefinance.crawler.framework.config.xml.extractor.FieldExtractor;
import com.treefinance.crawler.framework.context.AbstractProcessorContext;
import com.treefinance.crawler.framework.context.control.BusinessTypeDecider;
import com.treefinance.crawler.framework.context.pipeline.ProcessPipeline;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;

/**
 * @author Jerry
 * @since 15:53 2018/6/10
 */
public class FieldExtractorPipeline extends ProcessPipeline {

    public FieldExtractorPipeline(List<FieldExtractor> fieldExtractors, AbstractProcessorContext context) {
        if (CollectionUtils.isNotEmpty(fieldExtractors)) {
            for (FieldExtractor fieldExtractor : fieldExtractors) {
                if (fieldExtractor == null) {
                    continue;
                }

                if (BusinessTypeDecider.support(fieldExtractor.getBusinessType(), context)) {
                    FieldExtractorImpl fieldExtractorImpl = new FieldExtractorImpl(fieldExtractor);
                    addValve(fieldExtractorImpl);
                } else {
                    logger.warn("Skipped field-extractor[{}] with the forbidden business. field: {}, businessType: {} ,taskId: {}", fieldExtractor.getId(),
                        fieldExtractor.getField(), fieldExtractor.getBusinessType(), context.getTaskId());
                }
            }
        }
    }
}
