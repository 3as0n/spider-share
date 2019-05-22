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

package com.treefinance.crawler.framework.extension;

import com.treefinance.crawler.framework.config.xml.plugin.AbstractPlugin;
import com.treefinance.crawler.framework.config.xml.plugin.JavaPlugin;
import com.treefinance.crawler.framework.context.SearchProcessorContext;
import com.treefinance.crawler.framework.exception.UnsupportedExtensionException;
import com.treefinance.crawler.framework.extension.manager.WrappedExtension;
import com.treefinance.crawler.framework.extension.spider.PageProcessor;
import com.treefinance.crawler.framework.extension.spider.Spider;

import javax.annotation.Nonnull;

import java.util.Objects;

/**
 * @author Jerry
 * @since 15:11 15/01/2018
 */
public final class ExtensionFactory {

    private ExtensionFactory() {}

    public static Spider getSpider(@Nonnull final AbstractPlugin pluginMetadata, @Nonnull final SearchProcessorContext context, final PageProcessor pageProcessor) {
        return getSpider(pluginMetadata, context, pageProcessor, null);
    }

    public static Spider getSpider(@Nonnull final AbstractPlugin pluginMetadata, @Nonnull final SearchProcessorContext context, final PageProcessor pageProcessor,
        final Interrupter interrupter) {
        Objects.requireNonNull(context);
        if (!(pluginMetadata instanceof JavaPlugin)) {
            throw new UnsupportedExtensionException("Unsupported extension type - " + pluginMetadata.getClass());
        }

        WrappedExtension<Spider> extension = context.loadExtension(pluginMetadata, Spider.class);
        Spider spider = extension.getExtension();

        spider.setContext(context);
        spider.setProxyManager(context.getProxyManager());
        spider.setPageProcessor(pageProcessor);
        spider.setInterrupter(interrupter);

        return spider;
    }

}
