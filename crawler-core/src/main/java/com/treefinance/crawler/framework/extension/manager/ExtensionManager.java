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

package com.treefinance.crawler.framework.extension.manager;

import com.treefinance.crawler.framework.config.xml.plugin.AbstractPlugin;
import com.treefinance.crawler.framework.exception.ExtensionException;

/**
 * @author Jerry
 * @since 11:03 23/01/2018
 */
public interface ExtensionManager {

    <T> WrappedExtension<T> loadExtension(AbstractPlugin metadata, Class<T> extensionType, Long taskId) throws ExtensionException;

    <T> T loadExtension(String jarName, String mainClass, Class<T> extensionType, Long taskId) throws ExtensionException;

}
