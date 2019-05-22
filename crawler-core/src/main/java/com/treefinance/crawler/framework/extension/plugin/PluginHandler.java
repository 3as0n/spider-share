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

package com.treefinance.crawler.framework.extension.plugin;

import com.datatrees.common.util.GsonUtils;
import com.treefinance.crawler.framework.config.xml.plugin.AbstractPlugin;
import com.treefinance.crawler.framework.context.AbstractProcessorContext;
import com.treefinance.crawler.framework.context.function.SpiderRequest;
import com.treefinance.crawler.framework.context.function.SpiderResponse;
import com.treefinance.crawler.framework.context.pipeline.ProcessorInvokerAdapter;
import com.treefinance.crawler.framework.exception.PluginInvokeException;

import javax.annotation.Nonnull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author <A HREF="">Cheng Wang</A>
 * @version 1.0
 * @since Feb 19, 2014 1:10:48 PM
 */
public abstract class PluginHandler<T extends AbstractPlugin> extends ProcessorInvokerAdapter {

    private final T metadata;

    private final AbstractProcessorContext context;

    public PluginHandler(@Nonnull T metadata, @Nonnull AbstractProcessorContext context) {
        this.metadata = Objects.requireNonNull(metadata);
        this.context = Objects.requireNonNull(context);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void process(@Nonnull SpiderRequest request, @Nonnull SpiderResponse response) throws Exception {
        try {
            Map<String, String> params = new HashMap<>();

            Object input = request.getInput();
            if (input instanceof Map) {
                params.putAll((Map<String, String>)input);
            }

            params.put(PluginConstants.EXTRA_CONFIG, metadata.getExtraConfig());

            String args = GsonUtils.toJson(params);

            Object result = invokePlugin(metadata, args, request);

            response.setOutPut(result);
        } catch (PluginInvokeException e) {
            logger.error("Error invoking plugin: {}", metadata.getId(), e);
            throw e;
        } catch (Throwable e) {
            logger.error("Error invoking plugin: {} ", metadata.getId(), e);
            throw new PluginInvokeException("Error invoking plugin: " + metadata.getId(), e);
        }
    }

    public AbstractProcessorContext getContext() {
        return context;
    }

    protected abstract Object invokePlugin(T metadata, String args, SpiderRequest request) throws Exception;
}
