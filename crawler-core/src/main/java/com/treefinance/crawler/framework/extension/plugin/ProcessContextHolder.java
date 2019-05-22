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

import com.treefinance.crawler.framework.context.AbstractProcessorContext;

/**
 * @author Jerry
 * @since 16:31 15/05/2017
 */
public final class ProcessContextHolder {

    private static final ThreadLocal<AbstractProcessorContext> CONTEXT_THREAD_LOCAL = new ThreadLocal<>();

    private ProcessContextHolder() {}

    public static AbstractProcessorContext getProcessorContext() {
        return CONTEXT_THREAD_LOCAL.get();
    }

    public static void setProcessorContext(AbstractProcessorContext context) {
        if (context != null) {
            CONTEXT_THREAD_LOCAL.set(context);
        }
    }

    public static void clearProcessorContext() {
        CONTEXT_THREAD_LOCAL.remove();
    }
}
