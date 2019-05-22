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

package com.treefinance.crawler.framework.context.function;

import com.datatrees.common.conf.Configuration;
import com.treefinance.crawler.exception.UnexpectedException;
import com.treefinance.crawler.framework.context.AbstractProcessorContext;
import com.treefinance.crawler.lang.Attributes;
import com.treefinance.crawler.lang.Copyable;

import javax.annotation.Nonnull;

/**
 * @author Jerry
 * @since 15:07 2018/8/15
 */
public interface SpiderRequest extends Attributes, RequestMetadata, FieldScopeAction, Copyable<SpiderRequest> {

    Object getInput();

    void setInput(Object input);

    AbstractProcessorContext getProcessorContext();

    void setProcessorContext(AbstractProcessorContext context);

    Configuration getConfiguration();

    void setConfiguration(Configuration configuration);

    @SuppressWarnings("unchecked")
    default SpiderRequest withInput(@Nonnull Object input) {
        try {
            SpiderRequest req = getClass().newInstance();
            req.addAttributes(this.getAttributes());
            req.setProcessorContext(this.getProcessorContext());
            req.setConfiguration(this.getConfiguration());
            req.addExtra(this.getExtra());
            req.addVisibleScope(this.getVisibleScope());
            req.setInput(input);
            return req;
        } catch (InstantiationException | IllegalAccessException e) {
            throw new UnexpectedException("Error doing request copy. class: " + getClass());
        }
    }

    @Override
    default SpiderRequest copy() {
        return withInput(this.getInput());
    }

    void clear();

}
