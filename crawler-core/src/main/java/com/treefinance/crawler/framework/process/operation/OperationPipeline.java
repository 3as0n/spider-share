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

package com.treefinance.crawler.framework.process.operation;

import com.treefinance.crawler.framework.config.xml.extractor.FieldExtractor;
import com.treefinance.crawler.framework.config.xml.operation.AbstractOperation;
import com.treefinance.crawler.framework.context.ResponseUtil;
import com.treefinance.crawler.framework.context.function.SpiderRequest;
import com.treefinance.crawler.framework.context.function.SpiderResponse;
import com.treefinance.crawler.framework.context.function.SpiderResponseFactory;
import com.treefinance.crawler.framework.context.pipeline.ProcessPipeline;
import com.treefinance.crawler.framework.exception.InvalidDataException;
import com.treefinance.crawler.framework.exception.InvalidOperationException;
import com.treefinance.crawler.framework.exception.OperationException;
import com.treefinance.crawler.framework.exception.ResultEmptyException;
import com.treefinance.crawler.framework.process.ProcessorFactory;
import com.treefinance.crawler.framework.process.fields.FieldExtractResultSet;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;

/**
 * @author Jerry
 * @since 20:59 2018/5/14
 */
public class OperationPipeline extends ProcessPipeline {

    private final FieldExtractor fieldExtractor;

    public OperationPipeline(FieldExtractor fieldExtractor) {
        this.fieldExtractor = fieldExtractor;
        List<AbstractOperation> operations = fieldExtractor.getOperationList();
        if (CollectionUtils.isNotEmpty(operations)) {
            for (AbstractOperation operation : operations) {
                if (operation == null) {
                    continue;
                }
                addValve(ProcessorFactory.getOperation(operation, fieldExtractor));
            }
        }
    }

    public Object start(SpiderRequest request, FieldExtractResultSet fieldExtractResultSet) throws OperationException, ResultEmptyException {
        if (isPrepared()) {
            SpiderResponse resp = SpiderResponseFactory.make();
            ResponseUtil.setFieldExtractResultSet(resp, fieldExtractResultSet);
            try {
                super.invoke(request, resp);

                OperationEntity outPut = (OperationEntity)resp.getOutPut();
                logger.debug("operations result: {}", outPut);
                if (outPut != null) {
                    return outPut.getData();
                } else {
                    logger.warn("Unexpected operation result! No operations processed for {}", fieldExtractor);
                }
            } catch (InvalidDataException e) {
                logger.warn("Unexpected operating data! >> {}, field-extractor: {}", e.getMessage(), fieldExtractor);
                return null;
            } catch (InvalidOperationException e) {
                throw new OperationException("Unexpected operation! >> " + e.getMessage(), e.getCause());
            } catch (ResultEmptyException e) {
                throw e;
            } catch (Exception e) {
                throw new OperationException("Unexpected exception! >> " + e.getMessage(), e.getCause());
            }
        } else {
            logger.warn("Not found available operations for {}", fieldExtractor);
        }

        return request.getInput();
    }
}
