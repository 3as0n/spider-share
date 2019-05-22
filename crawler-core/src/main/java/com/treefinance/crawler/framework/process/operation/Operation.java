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

package com.treefinance.crawler.framework.process.operation;

import com.treefinance.crawler.framework.config.xml.extractor.FieldExtractor;
import com.treefinance.crawler.framework.config.xml.operation.AbstractOperation;
import com.treefinance.crawler.framework.context.function.SpiderRequest;
import com.treefinance.crawler.framework.context.function.SpiderResponse;
import com.treefinance.crawler.framework.context.pipeline.ProcessingException;
import com.treefinance.crawler.framework.context.pipeline.ProcessorValve;
import com.treefinance.crawler.framework.exception.InvalidDataException;
import com.treefinance.crawler.framework.exception.InvalidOperationException;
import com.treefinance.toolkit.util.json.GsonUtils;
import com.treefinance.toolkit.util.json.Jackson;

import javax.annotation.Nonnull;

import java.util.Objects;

/**
 * @author <A HREF="">Cheng Wang</A>
 * @version 1.0
 * @since Feb 18, 2014 1:45:54 PM
 */
public abstract class Operation<T extends AbstractOperation> extends ProcessorValve {

    protected final T operation;

    protected final FieldExtractor extractor;

    private boolean needReturn = true;

    public Operation(@Nonnull T operation, @Nonnull FieldExtractor extractor) {
        this(operation, extractor, true);
    }

    public Operation(@Nonnull T operation, @Nonnull FieldExtractor extractor, boolean needReturn) {
        this.operation = Objects.requireNonNull(operation);
        this.extractor = Objects.requireNonNull(extractor);
        this.needReturn = needReturn;
    }

    public T getOperation() {
        return operation;
    }

    public FieldExtractor getExtractor() {
        return extractor;
    }

    @Override
    public final void process(@Nonnull SpiderRequest request, @Nonnull SpiderResponse response) throws Exception {
        OperationEntity entity = getOperatingEntity(request, response);

        Object data = entity.getData();
        logger.debug("Operating data: {}", data);

        try {
            Object result = doOperation(operation, data, request, response);

            if (needReturn) {
                logger.debug("Operating result: {}", result);
                entity.update(result, operation);
            } else {
                entity.update(operation);
            }
        } catch (InvalidDataException e) {
            throw new InvalidDataException("Error doing operation[" + Jackson.toJSONString(operation) + "]. >> " + e.getMessage() + e.getCause());
        } catch (InvalidOperationException e) {
            throw new InvalidOperationException("Error doing operation[" + Jackson.toJSONString(operation) + "]. >> " + e.getMessage() + e.getCause());
        } catch (Exception e) {
            if (logger.isDebugEnabled()) {
                logger.debug("Error doing operation[{}]\nOperating data:\n{}", Jackson.toJSONString(operation), data);
            }
            throw new ProcessingException("Error doing operation[" + Jackson.toJSONString(operation) + "]", e);
        }
    }

    @Override
    protected boolean isSkipped(@Nonnull SpiderRequest request, @Nonnull SpiderResponse response) {
        boolean skipped = getOperatingEntity(request, response).isEmpty();

        // invalid operation and skip
        return skipped || isSkipped(operation, request, response);
    }

    protected boolean isSkipped(@Nonnull T operation, @Nonnull SpiderRequest request, @Nonnull SpiderResponse response) {
        return false;
    }

    @Override
    protected void triggerAfterSkipped(@Nonnull SpiderRequest request, @Nonnull SpiderResponse response) {
        logger.warn("Skipped operation processor : {}", GsonUtils.toJson(operation));

        OperationEntity entity = getOperatingEntity(request, response);
        entity.skip(operation);
    }

    @Override
    protected final boolean preProcess(@Nonnull SpiderRequest request, @Nonnull SpiderResponse response) throws Exception {
        if (logger.isDebugEnabled()) {
            logger.debug("Starting operation processing >> {}", Jackson.toJSONString(operation));
        }

        validate(operation, request, response);

        return true;
    }

    protected void validate(@Nonnull T operation, @Nonnull SpiderRequest request, @Nonnull SpiderResponse response) throws Exception {

    }

    protected abstract Object doOperation(@Nonnull T operation, @Nonnull Object operatingData, @Nonnull SpiderRequest request, @Nonnull SpiderResponse response) throws Exception;

    @Override
    protected final void postProcess(@Nonnull SpiderRequest request, @Nonnull SpiderResponse response) throws Exception {
        if (logger.isDebugEnabled()) {
            logger.debug("Completed operation processing >> {}", Jackson.toJSONString(operation));
        }
    }

    @Override
    protected final boolean isEnd(@Nonnull SpiderRequest request, @Nonnull SpiderResponse response) {
        return getOperatingEntity(request, response).isEmpty();
    }

    private final OperationEntity getOperatingEntity(@Nonnull SpiderRequest request, @Nonnull SpiderResponse response) {
        OperationEntity entity = (OperationEntity)response.getOutPut();
        if (entity == null) {
            String content = (String)request.getInput();
            entity = OperationEntity.wrap(content);
            response.setOutPut(entity);
        }
        return entity;
    }

}
