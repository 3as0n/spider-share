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

package com.treefinance.crawler.framework.config.factory.xml;

import com.datatrees.common.util.ReflectionUtils;
import com.treefinance.crawler.framework.config.SpiderConfig;
import com.treefinance.crawler.framework.config.annotation.Attr;
import com.treefinance.crawler.framework.config.annotation.ChildTag;
import com.treefinance.crawler.framework.config.annotation.Tag;
import com.treefinance.crawler.framework.config.factory.ConfigBuilder;
import com.treefinance.crawler.framework.config.xml.AbstractBeanDefinition;
import org.apache.commons.lang.StringUtils;
import org.jdom2.CDATA;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Parent;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * @author <A HREF="mailto:wangcheng@datatrees.com.cn">Cheng Wang</A>
 * @version 1.0
 * @since Jan 9, 2014 5:49:13 PM
 */
public class XmlConfigBuilder implements ConfigBuilder {

    private static final XmlConfigBuilder INSTANCE = new XmlConfigBuilder();

    private static final String splitRegex = "/";

    private static final List<Class> boxingTypes = Arrays.asList(new Class[] {String.class, Long.class, Integer.class, Short.class, Double.class, Float.class, Boolean.class});

    private XmlConfigBuilder() {}

    public static XmlConfigBuilder newBuilder() {
        return INSTANCE;
    }

    /*  
    private static final class MyXMLProcessor extends AbstractXMLOutputProcessor {
        @Override
        public void process(final Writer out, final Format format, final Element element) throws IOException {
            FormatStack fStack = new FormatStack(format);
            fStack.setEscapeOutput(false);
            // If this is the root element we could pre-initialize the
            // namespace stack with the namespaces
            printElement(out, fStack, new NamespaceStack(), element);
            out.flush();
        }
    }*/

    @Override
    public <C extends SpiderConfig> String build(C obj) {
        Document document = new Document();
        if (obj != null) {
            this.processObjectWithTag(document, obj);
        }
        return outputDocument(document);
    }

    private String outputDocument(Document document) {
        XMLOutputter fmt = new XMLOutputter();
        Format f = Format.getPrettyFormat();
        // fmt.setXMLOutputProcessor(new MyXMLProcessor());
        f.setExpandEmptyElements(true);
        fmt.setFormat(f);
        return fmt.outputString(document);
    }

    private void processObjectWithTag(Parent parent, Object obj) {
        if (this.isValueObj(obj)) {
            parent.addContent(new CDATA(obj.toString()));
        } else {
            if (obj.getClass().isAnnotationPresent(Tag.class)) {
                Tag objTag = obj.getClass().getAnnotation(Tag.class);
                Element element = this.createElementWithTag(objTag);
                parent.addContent(element);
                parent = element;
            }

            if (parent instanceof Element) {
                this.processObjectWithAttr((Element)parent, obj);
            }
        }
        processObjectForTagMethods(parent, obj);
    }

    private void processObjectForTagMethods(Parent parent, Object obj) {
        @SuppressWarnings("unchecked")
        List<Method> tagMethods = ReflectionUtils.listGetMethodWithAnnotations(obj.getClass(), Tag.class, ChildTag.class);
        for (Method tagMethod : tagMethods) {
            if (tagMethod.isAnnotationPresent(Tag.class)) {
                this.processTagMethod(parent, obj, tagMethod);
            } else if (tagMethod.isAnnotationPresent(ChildTag.class)) {
                this.processChildTagMethod(parent, obj, tagMethod);
            }
        }
    }

    private void processTagMethod(Parent parent, Object obj, Method tagMethod) {
        Object child = ReflectionUtils.invokeMethod(tagMethod, obj);
        if (child != null) {
            Tag tag = tagMethod.getAnnotation(Tag.class);
            String[] splitList = tag.value().split(splitRegex);
            String lastPath = splitList.length > 0 ? splitList[splitList.length - 1] : "";
            Element lastParentElement = (Element)(splitList.length > 1 ? createElement(splitList[splitList.length - 2]) : parent);
            Element element = this.createElementWithTag(lastPath);
            if (element == null && parent instanceof Element) {
                element = (Element)parent;
            } else if (element != null) {
                lastParentElement.addContent(element);
            }

            if (ReflectionUtils.isColleciton(child)) {
                for (Iterator<?> iterator = ReflectionUtils.iteratorColleciton(child); iterator.hasNext();) {
                    Object item = iterator.next();
                    this.processObjectWithTag(element, item);
                }
            } else {
                this.processObjectWithTag(element, child);
            }
            this.emptyTagsCompletion(splitList, lastParentElement, parent);
        }
    }

    private void processChildTagMethod(Parent parent, Object obj, Method tagMethod) {
        Object child = ReflectionUtils.invokeMethod(tagMethod, obj);
        if (ReflectionUtils.isColleciton(child)) {
            ChildTag tag = tagMethod.getAnnotation(ChildTag.class);
            String[] splitList = tag.value().split(splitRegex);
            String lastPath = splitList.length > 0 ? splitList[splitList.length - 1] : "";
            Element lastParentElement = (Element)(splitList.length > 1 ? createElement(splitList[splitList.length - 2]) : parent);
            if (StringUtils.isNotBlank(lastPath)) {
                for (Iterator<?> iterator = ReflectionUtils.iteratorColleciton(child); iterator.hasNext();) {
                    Object item = iterator.next();
                    Element element = createElement(lastPath);
                    if (tag.referenced()) {// ref bean
                        AbstractBeanDefinition newBean = new AbstractBeanDefinition();
                        newBean.setId(item.toString());
                        item = newBean;
                    }
                    this.processObjectWithTag(element, item);
                    lastParentElement.addContent(element);
                }
            }
            this.emptyTagsCompletion(splitList, lastParentElement, parent);
        }
    }

    private void emptyTagsCompletion(String[] tagNmaes, Element lastParentElement, Parent parent) {
        for (int i = tagNmaes.length - 3; i >= 0; i--) {
            Element element = createElement(tagNmaes[i]);
            element.addContent(lastParentElement);
            lastParentElement = element;
        }
        if (!lastParentElement.equals(parent))
            parent.addContent(lastParentElement);
    }

    private void processObjectWithAttr(Element parent, Object obj) {
        @SuppressWarnings("unchecked")
        List<Method> attrMethods = ReflectionUtils.listGetMethodWithAnnotations(obj.getClass(), Attr.class);
        for (Method method : attrMethods) {
            Object attrObj = ReflectionUtils.invokeMethod(method, obj);
            if (attrObj != null) {
                Attr attr = method.getAnnotation(Attr.class);
                parent.setAttribute(attr.value(), attrObj.toString());
            }
        }
    }

    private boolean isValueObj(Object obj) {
        return obj.getClass().isPrimitive() || obj.getClass().isEnum() || boxingTypes.contains(obj.getClass());
    }

    private Element createElementWithTag(Tag tag) {
        if (tag.value().equals("")) {
            return null;
        }
        return createElement(tag.value());
    }

    private Element createElementWithTag(String name) {
        if (name == null || name.equals("")) {
            return null;
        }
        return createElement(name);
    }

    private Element createElement(String name) {
        return new Element(name);
    }
}
