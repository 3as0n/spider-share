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

package com.treefinance.crawler.framework.extension.manager;

import com.datatrees.crawler.core.processor.plugin.AbstractClientPlugin;
import com.treefinance.crawler.framework.exception.PluginException;

/**
 * @author Jerry
 * @since 01:02 23/01/2018
 */
public interface PluginManager extends ExtensionManager {

    /**
     * 从jar加载class
     */
    AbstractClientPlugin loadPlugin(String jarName, String mainClass, Long taskId) throws PluginException;

}
