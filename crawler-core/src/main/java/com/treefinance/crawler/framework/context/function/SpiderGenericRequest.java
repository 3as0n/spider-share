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
import com.treefinance.crawler.framework.context.AbstractProcessorContext;
import com.treefinance.crawler.lang.AtomicAttributes;
import org.apache.commons.collections4.MapUtils;

import javax.annotation.Nonnull;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * @author Jerry
 * @since 20:53 2018/7/23
 */
public class SpiderGenericRequest extends AtomicAttributes implements SpiderRequest {

    private Object input;
    private AbstractProcessorContext context;
    private Configuration configuration;
    private Map<String, Object> extra;
    private Map<String, Object> localScope;
    private Map<String, Object> visibleScope;

    @Override
    public Object getInput() {
        return input;
    }

    @Override
    public void setInput(Object input) {
        this.input = input;
    }

    @Override
    public AbstractProcessorContext getProcessorContext() {
        return context;
    }

    @Override
    public void setProcessorContext(AbstractProcessorContext context) {
        this.context = context;
    }

    @Override
    public Configuration getConfiguration() {
        return configuration;
    }

    @Override
    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public Map<String, Object> getExtra() {
        return extra == null ? Collections.emptyMap() : Collections.unmodifiableMap(extra);
    }

    @Override
    public void setExtra(Map<String, Object> extra) {
        if (MapUtils.isEmpty(extra)) {
            if (MapUtils.isNotEmpty(this.extra)) {
                this.extra.clear();
            }
        } else {
            this.extra = new HashMap<>(extra);
        }
    }

    @Override
    public void addExtra(String name, Object value) {
        if (value == null) {
            extra().remove(name);
        } else {
            extra().put(name, value);
        }
    }

    @Override
    public void addExtra(Map<String, Object> extra) {
        if (extra != null) {
            extra().putAll(extra);
        }
    }

    @Override
    public Object computeExtraIfAbsent(@Nonnull String name, @Nonnull Function<String, Object> mappingFunction) {
        return extra().computeIfAbsent(name, mappingFunction);
    }

    @Override
    public void addLocalScope(Map<String, Object> localScope) {
        if (localScope != null) {
            localScope().putAll(localScope);
        }
    }

    @Override
    public Map<String, Object> getVisibleScope() {
        return visibleScope != null ? Collections.unmodifiableMap(visibleScope) : Collections.emptyMap();
    }

    @Override
    public void setVisibleScope(Map<String, Object> visibleScope) {
        if (MapUtils.isEmpty(visibleScope)) {
            if (MapUtils.isNotEmpty(this.visibleScope)) {
                this.visibleScope.clear();
            }
        } else {
            this.visibleScope = new HashMap<>(visibleScope);
        }
    }

    @Override
    public void addVisibleScope(String name, Object value) {
        if (value == null) {
            visibleScope().remove(name);
        } else {
            visibleScope().put(name, value);
        }
    }

    @Override
    public void addVisibleScope(Map<String, Object> visibleScope) {
        if (visibleScope != null) {
            visibleScope().putAll(visibleScope);
        }
    }

    @Override
    public Map<String, Object> getContextScope() {
        if (context == null) {
            return Collections.emptyMap();
        }

        return context.getVisibleScope();
    }

    @Override
    public void setContextScope(Map<String, Object> contextScope) {
        if (context != null) {
            context.setAttributes(contextScope);
        }
        addVisibleScope(contextScope);
    }

    @Override
    public void addContextScope(String name, Object value) {
        if (context != null) {
            context.setAttribute(name, value);
        }
        addVisibleScope(name, value);
    }

    @Override
    public void addContextScope(Map<String, Object> contextScope) {
        if (context != null) {
            context.addAttributes(contextScope);
        }
        addVisibleScope(contextScope);
    }

    @Override
    public Map<String, Object> getResultScope() {
        if (context == null) {
            return Collections.emptyMap();
        }

        return context.getResultScope();
    }

    @Override
    public void setResultScope(Map<String, Object> resultScope) {
        if (context != null) {
            context.setProcessorResult(resultScope);
        }
        addContextScope(resultScope);
    }

    @Override
    public void addResultScope(String name, Object value) {
        if (context != null) {
            context.addProcessorResult(name, value);
        }
        addContextScope(name, value);
    }

    @Override
    public void addResultScope(Map<String, Object> resultScope) {
        if (context != null) {
            context.addProcessorResult(resultScope);
        }
        addContextScope(resultScope);
    }

    @Override
    public GlobalScope getGlobalScope() {
        return new GlobalScope(getLocalScope(), getVisibleScope(), getContextScope());
    }

    @Override
    public Map<String, Object> getGlobalScopeAsMap() {
        return getGlobalScope().asMap();
    }

    @Override
    public Object getGlobalFieldValue(String name) {
        return getGlobalScope().getValue(name);
    }

    @Override
    public void clear() {
        if (this.localScope != null) {
            this.localScope.clear();
        }
    }

    private Map<String, Object> extra() {
        if (extra == null) {
            extra = new HashMap<>();
        }
        return extra;
    }

    private Map<String, Object> localScope() {
        if (localScope == null) {
            localScope = new HashMap<>();
        }
        return localScope;
    }

    private Map<String, Object> getLocalScope() {
        return localScope != null ? Collections.unmodifiableMap(localScope) : Collections.emptyMap();
    }

    private Map<String, Object> visibleScope() {
        if (visibleScope == null) {
            visibleScope = new HashMap<>();
        }
        return visibleScope;
    }

}