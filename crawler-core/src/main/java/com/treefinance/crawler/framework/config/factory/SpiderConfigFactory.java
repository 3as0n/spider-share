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

package com.treefinance.crawler.framework.config.factory;

import com.treefinance.crawler.exception.UnexpectedException;
import com.treefinance.crawler.framework.config.SpiderConfig;
import com.treefinance.crawler.framework.config.factory.xml.XmlConfigParser;
import com.treefinance.crawler.framework.exception.ConfigParseException;

/**
 * @author Jerry
 * @since 15:28 2018/7/13
 */
public class SpiderConfigFactory {

    public static <C extends SpiderConfig> C build(String config, Class<C> configClass) {
        try {
            return XmlConfigParser.newParser().parse(config, configClass);
        } catch (ConfigParseException e) {
            throw new UnexpectedException("Unexpected exception when building crawler config.", e);
        }
    }

    public static <C extends SpiderConfig> C build(String config, Class<C> configClass, ParentConfigHandler<C> parentConfigHandler) {
        try {
            return XmlConfigParser.newParser().parse(config, configClass, parentConfigHandler);
        } catch (ConfigParseException e) {
            throw new UnexpectedException("Unexpected exception when building crawler config.", e);
        }
    }
}
