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

import com.treefinance.crawler.framework.config.xml.plugin.AbstractPlugin;
import com.treefinance.crawler.framework.context.SearchProcessorContext;
import com.treefinance.crawler.framework.extension.ExtensionFactory;
import com.treefinance.crawler.framework.extension.Interrupter;

import javax.annotation.Nonnull;

/**
 * @author Jerry
 * @since 15:06 15/01/2018
 */
public final class Spiders {

    private Spiders() {}

    public static void run(@Nonnull final AbstractPlugin pluginMetadata, @Nonnull final SearchProcessorContext context, final PageProcessor pageProcessor)
        throws InterruptedException {
        run(pluginMetadata, context, pageProcessor, null);
    }

    public static void run(@Nonnull final AbstractPlugin pluginMetadata, @Nonnull final SearchProcessorContext context, final PageProcessor pageProcessor,
        final Interrupter interrupter) throws InterruptedException {
        Spider spider = ExtensionFactory.getSpider(pluginMetadata, context, pageProcessor, interrupter);
        spider.run();
    }
}
