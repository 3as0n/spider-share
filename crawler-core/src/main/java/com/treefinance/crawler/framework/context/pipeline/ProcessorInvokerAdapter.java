/*
 * Copyright © 2015 - 2018 杭州大树网络技术有限公司. All Rights Reserved
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.treefinance.crawler.framework.context.pipeline;

import javax.annotation.Nonnull;

import com.treefinance.crawler.framework.exception.ResultEmptyException;
import com.treefinance.crawler.framework.context.function.SpiderRequest;
import com.treefinance.crawler.framework.context.function.SpiderResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Jerry
 * @since 00:12 2018/5/24
 */
public abstract class ProcessorInvokerAdapter implements Processor, Invoker {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public final void invoke(@Nonnull SpiderRequest request, @Nonnull SpiderResponse response) throws InvokeException, ResultEmptyException {
        try {
            preProcess(request, response);
            process(request, response);
            postProcess(request, response);
        } catch (ResultEmptyException | InvokeException e) {
            throw e;
        } catch (Exception e) {
            throw new ProcessingException("Error invoking processor[" + getClass().getName() + "].", e);
        }
    }

    protected void preProcess(@Nonnull SpiderRequest request, @Nonnull SpiderResponse response) throws Exception {}

    protected void postProcess(@Nonnull SpiderRequest request, @Nonnull SpiderResponse response) throws Exception {}

}
