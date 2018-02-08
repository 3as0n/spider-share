/*
 * Copyright © 2015 - 2017 杭州大树网络技术有限公司. All Rights Reserved
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

package com.treefinance.crawler.framework.extension.spider;

import com.datatrees.crawler.core.processor.SearchProcessorContext;
import com.datatrees.crawler.core.processor.common.resource.ProxyManager;
import com.treefinance.crawler.framework.extension.Interrupter;

/**
 * @author Jerry
 * @since 14:32 27/11/2017
 */
public interface Spider {

    void run() throws InterruptedException;

    void setContext(SearchProcessorContext context);

    void setProxyManager(ProxyManager proxyManager);

    void setPageProcessor(PageProcessor customPageProcessor);

    void setInterrupter(Interrupter interrupter);
}