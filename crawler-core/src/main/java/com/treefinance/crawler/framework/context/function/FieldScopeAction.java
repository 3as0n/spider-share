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

package com.treefinance.crawler.framework.context.function;

import java.util.Map;

/**
 * @author Jerry
 * @since 10:31 2018/7/31
 */
public interface FieldScopeAction {

    /**
     * the shared fields map with the global request scope.
     * <p>
     * Notice: it's a temp scope. only used in nested segment processing.
     * </p>
     */
    void addLocalScope(Map<String, Object> localScope);

    /**
     * the shared fields map with the global request scope.
     * 
     * @return the unmodifiable map.
     */
    Map<String, Object> getVisibleScope();

    void setVisibleScope(Map<String, Object> visibleScope);

    void addVisibleScope(String name, Object value);

    void addVisibleScope(Map<String, Object> visibleScope);

    /**
     * the shared fields map with the global context scope.
     * 
     * @return the unmodifiable map.
     */
    Map<String, Object> getContextScope();

    /**
     * reset context scope with the given fields, and also put into request scope as well
     */
    void setContextScope(Map<String, Object> contextScope);

    /**
     * put the field into context scope, and also put into request scope as well
     */
    void addContextScope(String name, Object value);

    /**
     * put the fields into context scope, and also put into request scope as well
     */
    void addContextScope(Map<String, Object> contextScope);

    /**
     * the shared fields map with the global processor-result scope.
     * 
     * @return the unmodifiable map.
     */
    Map<String, Object> getResultScope();

    /**
     * reset result scope with the given fields, and also put into request scope and context scope as well
     */
    void setResultScope(Map<String, Object> resultScope);

    /**
     * put the field into result scope, and also put into request scope and context scope as well
     */
    void addResultScope(String name, Object value);

    /**
     * put the fields into result scope, and also put into request scope and context scope as well
     */
    void addResultScope(Map<String, Object> resultScope);

    GlobalScope getGlobalScope();

    /**
     * the shared fields map with the visible scope that contains request scope, context scope and processor_result
     * scope.
     * 
     * @return the unmodifiable map.
     */
    Map<String, Object> getGlobalScopeAsMap();

    Object getGlobalFieldValue(String name);
}
