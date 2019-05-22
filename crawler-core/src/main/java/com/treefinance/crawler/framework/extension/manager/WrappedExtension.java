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

package com.treefinance.crawler.framework.extension.manager;

import com.treefinance.crawler.framework.config.xml.plugin.AbstractPlugin;

/**
 * @author Jerry
 * @since 10:49 23/01/2018
 */
public class WrappedExtension<T> {

    private final AbstractPlugin metadata;

    private T extension;

    public WrappedExtension(AbstractPlugin metadata) {
        this.metadata = metadata;
    }

    public WrappedExtension(AbstractPlugin metadata, T extension) {
        this.metadata = metadata;
        this.extension = extension;
    }

    public AbstractPlugin getMetadata() {
        return metadata;
    }

    public T getExtension() {
        return extension;
    }

    public void setExtension(T extension) {
        this.extension = extension;
    }
}
