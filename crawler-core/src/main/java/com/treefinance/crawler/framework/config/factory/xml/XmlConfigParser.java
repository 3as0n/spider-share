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

package com.treefinance.crawler.framework.config.factory.xml;

import com.datatrees.common.util.ReflectionUtils;
import com.treefinance.crawler.framework.config.SpiderConfig;
import com.treefinance.crawler.framework.config.annotation.Node;
import com.treefinance.crawler.framework.config.annotation.Path;
import com.treefinance.crawler.framework.config.factory.ConfigParser;
import com.treefinance.crawler.framework.config.factory.ParentConfigHandler;
import com.treefinance.crawler.framework.exception.ConfigParseException;
import com.treefinance.crawler.framework.util.XmlDocumentHelper;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author <A HREF="mailto:wangcheng@datatrees.com.cn">Cheng Wang</A>
 * @version 1.0
 * @since Jan 9, 2014 5:48:40 PM
 */
public class XmlConfigParser implements ConfigParser {

    private static final Logger logger = LoggerFactory.getLogger(XmlConfigParser.class);
    private static final List<Class<?>> valueTypes = Arrays.asList(new Class<?>[] {String.class, Boolean.class, Short.class, Integer.class, Long.class, Float.class, Double.class});
    private final Map<String, Object> contentMap = new HashMap<>();
    private final Map<Class<?>, List<Method>> typeSetMethodMap = new HashMap<>();

    private XmlConfigParser() {}

    public static XmlConfigParser newParser() {
        return new XmlConfigParser();
    }

    @Override
    public <T extends SpiderConfig> T parse(String config, Class<T> type, ParentConfigHandler<T> handler) throws ConfigParseException {
        T result = parse(config, type);
        if (handler != null) {
            try {
                result = handler.handle(result);
            } catch (Exception e) {
                throw new ConfigParseException("Error handing the parent config. class: " + type, e);
            }
        }
        return result;
    }

    @Override
    public <T extends SpiderConfig> T parse(String config, Class<T> type) throws ConfigParseException {
        try {
            Document document = XmlDocumentHelper.createDocument(config);
            return processNodes(document.getRootElement(), type);
        } catch (Exception e) {
            throw new ConfigParseException("Error parsing crawler config, class: " + type, e);
        } finally {
            contentMap.clear();
        }
    }

    private <V> V processNodes(Element e, Class<V> type) throws Exception {
        e = processPath(e, type);
        if (e == null) {
            return null;
        }

        V parent = ReflectionUtils.newInstance(type);

        List<Method> methods = listOrderedSetMethod(type);
        for (Method method : methods) {
            processNodeMethod(e, parent, method);
        }

        return parent;
    }

    @SuppressWarnings("unchecked")
    private <V> List<Method> listOrderedSetMethod(Class<V> type) {
        return typeSetMethodMap.computeIfAbsent(type, t -> {
            List<Method> methods = ReflectionUtils.listSetMethodWithAnnotations(type, Node.class);

            if (CollectionUtils.isNotEmpty(methods)) {
                return methods.stream().sorted((o1, o2) -> {
                    Node node1 = o1.getAnnotation(Node.class);
                    Node node2 = o2.getAnnotation(Node.class);

                    if (node1.registered() == node2.registered()) {
                        return 0;
                    } else if (node1.registered()) {
                        return -1;
                    } else {
                        return 1;
                    }
                }).collect(Collectors.toList());
            }

            return Collections.emptyList();
        });
    }

    private Element processPath(Element e, Class<?> type) {
        if (type.isAnnotationPresent(Path.class)) {
            Path path = type.getAnnotation(Path.class);
            if (StringUtils.isNotBlank(path.value())) {
                e = XmlDocumentHelper.getElementByXPath(e, path.value());
            }
        }
        return e;
    }

    private void processNodeMethod(Element e, Object parent, Method method) throws Exception {
        Class<?> setClassType = method.getParameterTypes()[0];
        Node node = method.getAnnotation(Node.class);
        List<Object> elements;
        if (StringUtils.isNotBlank(node.value())) {
            elements = XmlDocumentHelper.getElementsByXPath(e, node.value());
        } else {
            elements = Collections.singletonList(e);
        }

        Class<?>[] nodeTypes = node.types();
        int length = nodeTypes.length;
        if (length <= 0) {
            nodeTypes = new Class[] {setClassType};
        }

        if (valueTypes.contains(nodeTypes[0])) {
            setBaseValue(elements, method, nodeTypes[0], parent);
        } else {
            setNodeValue(elements, method, nodeTypes, parent);
        }
    }

    private void setNodeValue(List<Object> elements, Method method, Class<?>[] nodeTypes, Object parent) throws Exception {
        for (Object element : elements) {
            for (Class<?> type : nodeTypes) {
                if (this.customTypeProcess(element, type, parent, method) != null)
                    break;
            }
        }
    }

    private void setBaseValue(List<Object> elements, Method method, Class<?> paramType, Object parent) {
        for (Object element : elements) {
            Object value = processValue(element, paramType);
            if (value != null) {
                logger.trace("invoke method : {} for target : {} with value : {}", method.getName(), parent, value);
                ReflectionUtils.invokeMethod(method, parent, value);
            }
        }
    }

    private Object customTypeProcess(Object element, Class<?> setClassType, Object parent, Method method) throws Exception {
        Node node = method.getAnnotation(Node.class);
        Object value = null;
        if (node.referenced()) {
            String id = (String)processValue(element, String.class);// get id
            value = contentMap.get(id);
        } else {
            value = processNodes((Element)element, setClassType);
        }

        if (value != null) {
            logger.trace("invoke method : {} for target : {} with value : {}", method.getName(), parent, value);
            ReflectionUtils.invokeMethod(method, parent, value);
            if (node.registered()) {
                String id = value.toString();
                Object oldBeanDefinition = contentMap.get(id);
                if (oldBeanDefinition != null) {
                    throw new ConfigParseException("exist the same BeanDefinition named " + id);
                } else {
                    contentMap.put(id, value);
                }
            }
        }

        return value;
    }

    private Object processValue(Object obj, Class<?> type) {
        String value = XmlDocumentHelper.getElementValue(obj);
        if (value != null && String.class.equals(type) && obj instanceof Attribute) {// Attribute
            // allow " "
            return value;
        } else if (StringUtils.isBlank(value)) {
            return null;
        }

        value = value.trim();

        if (Boolean.class.equals(type)) {
            return Boolean.valueOf(value);
        } else if (Short.class.equals(type)) {
            return Short.valueOf(value);
        } else if (Integer.class.equals(type)) {
            return Integer.valueOf(value);
        } else if (Long.class.equals(type)) {
            return Long.valueOf(value);
        } else if (Float.class.equals(type)) {
            return Float.valueOf(value);
        } else if (Double.class.equals(type)) {
            return Double.valueOf(value);
        }

        return value;
    }

}
