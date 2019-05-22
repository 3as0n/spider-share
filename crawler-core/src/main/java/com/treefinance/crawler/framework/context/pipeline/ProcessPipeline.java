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

package com.treefinance.crawler.framework.context.pipeline;

import com.treefinance.crawler.framework.context.function.SpiderRequest;
import com.treefinance.crawler.framework.context.function.SpiderResponse;
import com.treefinance.crawler.framework.exception.ResultEmptyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;

import java.util.List;
import java.util.Objects;

/**
 * @author <A HREF="">Cheng Wang</A>
 * @version 1.0
 * @since Feb 20, 2014 7:50:10 PM
 */
public abstract class ProcessPipeline {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    private Pipeline pipeline = new StandardPipeline();

    public ProcessPipeline() {}

    public ProcessPipeline(List<Valve> valves) {
        Objects.requireNonNull(valves);
        for (Valve processor : valves) {
            addValve(processor);
        }
    }

    public void addValve(Valve valve) {
        if (valve != null)
            this.pipeline.addValve(valve);
    }

    public void invoke(@Nonnull SpiderRequest request, @Nonnull SpiderResponse response) throws InvokeException, ResultEmptyException {
        Valve valve = pipeline.getFirst();

        if (valve != null) {
            valve.invoke(request, response);
        }
    }

    public void invokeQuietly(@Nonnull SpiderRequest request, @Nonnull SpiderResponse response) throws ResultEmptyException {
        try {
            invoke(request, response);
        } catch (ResultEmptyException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Error invoking processor pipeline!", e);
        }
    }

    public boolean isPrepared() {
        return pipeline.getFirst() != null;
    }
}
