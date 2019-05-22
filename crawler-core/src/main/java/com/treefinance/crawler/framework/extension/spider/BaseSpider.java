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

package com.treefinance.crawler.framework.extension.spider;

import com.treefinance.crawler.framework.context.SearchProcessorContext;
import com.treefinance.crawler.framework.extension.Interrupter;
import com.treefinance.crawler.framework.proxy.ProxyManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Jerry
 * @since 18:53 27/11/2017
 */
public abstract class BaseSpider implements Spider {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    private SearchProcessorContext context;

    private ProxyManager proxyManager;

    private PageProcessor pageProcessor;

    private Interrupter interrupter;

    protected SearchProcessorContext getContext() {
        return context;
    }

    @Override
    public void setContext(SearchProcessorContext context) {
        this.context = context;
    }

    protected ProxyManager getProxyManager() {
        return proxyManager;
    }

    @Override
    public void setProxyManager(ProxyManager proxyManager) {
        this.proxyManager = proxyManager;
    }

    protected PageProcessor getPageProcessor() {
        return pageProcessor;
    }

    @Override
    public void setPageProcessor(PageProcessor pageProcessor) {
        this.pageProcessor = pageProcessor;
    }

    protected Interrupter getInterrupter() {
        return interrupter;
    }

    @Override
    public void setInterrupter(Interrupter interrupter) {
        this.interrupter = interrupter;
    }
}
