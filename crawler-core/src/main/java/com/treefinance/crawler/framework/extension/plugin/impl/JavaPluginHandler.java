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

package com.treefinance.crawler.framework.extension.plugin.impl;

import com.treefinance.crawler.framework.config.xml.plugin.JavaPlugin;
import com.treefinance.crawler.framework.context.AbstractProcessorContext;
import com.treefinance.crawler.framework.context.SearchProcessorContext;
import com.treefinance.crawler.framework.context.function.SpiderRequest;
import com.treefinance.crawler.framework.extension.plugin.AbstractClientPlugin;
import com.treefinance.crawler.framework.extension.plugin.PluginHandler;
import com.treefinance.crawler.framework.extension.plugin.ProcessContextHolder;
import com.treefinance.crawler.framework.protocol.WebClientUtil;

/**
 * java plugin for
 * 
 * @author <A HREF="">Cheng Wang</A>
 * @version 1.0
 * @since Feb 19, 2014 1:10:33 PM
 */
public class JavaPluginHandler extends PluginHandler<JavaPlugin> {

    public JavaPluginHandler(JavaPlugin metadata, AbstractProcessorContext context) {
        super(metadata, context);
    }

    @Override
    protected Object invokePlugin(JavaPlugin metadata, String args, SpiderRequest request) throws Exception {
        ProcessContextHolder.setProcessorContext(getContext());

        try {
            AbstractClientPlugin clientPlugin = getContext().loadPlugin(metadata);
            if (getContext() instanceof SearchProcessorContext) {
                // add proxy if necessary
                clientPlugin.setProxyManager(((SearchProcessorContext)getContext()).getProxyManager());
            }
            // add http client
            clientPlugin.setWebClient(WebClientUtil.getWebClient());

            // 插件自定义参数
            return clientPlugin.process(args, metadata.getParams());
        } finally {
            ProcessContextHolder.clearProcessorContext();
        }
    }
}
