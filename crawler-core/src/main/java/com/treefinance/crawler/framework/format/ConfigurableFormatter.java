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

package com.treefinance.crawler.framework.format;

import com.datatrees.common.conf.Configurable;
import com.datatrees.common.conf.Configuration;
import com.datatrees.common.conf.PropertiesConfiguration;

/**
 * @author Jerry
 * @since 16:08 2018/5/14
 */
public abstract class ConfigurableFormatter<R> extends CommonFormatter<R> implements Configurable {

    private Configuration configuration;

    @Override
    public Configuration getConf() {
        return configuration == null ? PropertiesConfiguration.getInstance() : configuration;
    }

    @Override
    public void setConf(Configuration conf) {
        this.configuration = conf;
    }

}
