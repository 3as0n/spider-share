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

package com.treefinance.crawler.framework.context;

import com.treefinance.crawler.framework.config.xml.plugin.AbstractPlugin;
import com.treefinance.crawler.framework.config.xml.plugin.JavaPlugin;
import com.treefinance.crawler.framework.extension.manager.WrappedExtension;
import com.treefinance.crawler.framework.extension.plugin.AbstractClientPlugin;
import com.treefinance.crawler.lang.SynchronizedMap;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nonnull;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

/**
 * @author <A HREF="">Cheng Wang</A>
 * @version 1.0
 * @since 2015年7月7日 下午7:09:02
 */
public abstract class AbstractProcessorContext extends ProcessContext {

    protected final Long taskId;

    private final Map<String, Object> statusContext;

    private final Map<Thread, Object> threadContext;

    private ProcessorResult<String, Object> processorResult;

    public AbstractProcessorContext(Website website, Long taskId) {
        super(website);
        this.taskId = taskId;
        this.statusContext = new SynchronizedMap<>();
        this.threadContext = new SynchronizedMap<>();
        this.processorResult = new ProcessorResult<>();
    }

    public abstract void init();

    public Long getTaskId() {
        return taskId;
    }

    /**
     * @return the statusContext
     */
    public Map<String, Object> getStatusContext() {
        return statusContext;
    }

    public void addStatusAttr(String name, Object value) {
        getStatusContext().put(name, value);
    }

    /**
     * @return the threadContext
     */
    public Map<Thread, Object> getThreadContext() {
        return threadContext;
    }

    public Object getThreadAttr(Thread thread, String key) {
        Map<String, Object> map = threadAttributes(thread);
        return map.get(key);
    }

    public Object computeThreadAttrIfAbsent(Thread thread, String key, Function<String, Object> mappingFunction) {
        Map<String, Object> map = threadAttributes(thread);

        return map.computeIfAbsent(key, mappingFunction);
    }

    public void removeThreadAttr(Thread thread, String key) {
        Map<String, Object> map = threadAttributes(thread);
        map.remove(key);
    }

    /**
     * @return the processorResult
     */
    public ProcessorResult<String, Object> getProcessorResult() {
        return processorResult;
    }

    public void setProcessorResult(Map<String, Object> processorResult) {
        if (processorResult == null) {
            getProcessorResult().clear();
        } else {
            this.processorResult = new ProcessorResult<>(processorResult);
        }
    }

    /**
     * the shared fields map with the global processor-result scope.
     *
     * @return the unmodifiable map.
     */
    public Map<String, Object> getResultScope() {
        return Collections.unmodifiableMap(processorResult);
    }

    public void addProcessorResult(String name, Object value) {
        if (value == null) {
            getProcessorResult().remove(name);
        } else {
            getProcessorResult().put(name, value);
        }
    }

    public void addProcessorResult(Map<String, Object> processorResult) {
        if (processorResult != null) {
            getProcessorResult().putAll(processorResult);
        }
    }

    /**
     * 给context赋值
     *
     * @param key
     * @param value
     * @return
     */
    public void set(String key, Object value) {
        setAttribute(key, value);
    }

    /**
     * 给context赋值
     *
     * @param key
     * @param value
     * @return
     */
    public void setString(String key, Object value) {
        if (null == value) {
            getAttributes().put(key, null);
            return;
        }
        getAttributes().put(key, value.toString());
    }

    /**
     * 从context取值
     *
     * @param key
     * @return
     */
    public String getString(String key) {
        if (StringUtils.isEmpty(key)) {
            return null;
        }
        Object v = getAttribute(key);
        if (null == v) {
            return null;
        }
        return v.toString();
    }

    /**
     * 从context取值
     *
     * @param key
     * @return
     */
    public Long getLong(String key) {
        if (StringUtils.isEmpty(key)) {
            return null;
        }
        Object v = getAttribute(key);
        if (null == v) {
            return null;
        } else if (v instanceof Long) {
            return (Long)v;
        } else if (v instanceof String) {
            return Long.valueOf((String)v);
        }
        return Long.valueOf(v.toString());
    }

    /**
     * 从context取值
     *
     * @param key
     * @return
     */
    public Boolean getBoolean(String key) {
        if (StringUtils.isEmpty(key)) {
            return null;
        }
        Object v = getAttribute(key);
        if (null == v) {
            return null;
        } else if (v instanceof Boolean) {
            return (Boolean)v;
        } else if (v instanceof String) {
            return Boolean.valueOf((String)v);
        }
        throw new ClassCastException("Can not cast class '" + v.getClass() + "' to 'Boolean'.");
    }

    public <T> WrappedExtension<T> loadExtension(@Nonnull AbstractPlugin pluginMetadata, @Nonnull Class<T> extensionType) {
        Objects.requireNonNull(pluginMetadata);

        return getPluginManager().loadExtension(pluginMetadata, extensionType, taskId);
    }

    public AbstractClientPlugin loadPlugin(@Nonnull JavaPlugin pluginMetadata) {
        Objects.requireNonNull(pluginMetadata);
        String fileName = pluginMetadata.getFileName();
        String mainClass = pluginMetadata.getMainClass();

        return getPluginManager().loadPlugin(fileName, mainClass, taskId);
    }

    @SuppressWarnings("unchecked")
    @Nonnull
    private Map<String, Object> threadAttributes(Thread thread) {
        return (Map<String, Object>)getThreadContext().computeIfAbsent(thread, t -> new HashMap<String, Object>());
    }
}
