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

package com.treefinance.crawler.framework.util;

import org.apache.commons.lang.StringUtils;
import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Text;
import org.jdom2.input.SAXBuilder;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;

/**
 * @author Jerry
 * @since 16:34 2018/7/13
 */
public final class XmlDocumentHelper {

    private XmlDocumentHelper() {}

    public static Document createDocument(String xml) throws JDOMException, IOException {
        try (StringReader reader = new StringReader(xml)) {
            SAXBuilder saxBuilder = new SAXBuilder();
            return saxBuilder.build(reader);
        }
    }

    public static XPathExpression<Object> compileXpath(String path) {
        return XPathFactory.instance().compile(path);
    }

    public static Object evaluateFirst(Element element, String path) {
        XPathExpression<Object> xpath = compileXpath(path);
        return xpath.evaluateFirst(element);
    }

    public static List<Object> evaluate(Element element, String path) {
        XPathExpression<Object> xpath = compileXpath(path);
        return xpath.evaluate(element);
    }

    public static Element getElementByXPath(Element context, String path) {
        return (Element)evaluateFirst(context, path);
    }

    public static List<Object> getElementsByXPath(Element context, String path) {
        return evaluate(context, path);
    }

    public static String getElementValue(Object obj) {
        if (obj == null) {
            return StringUtils.EMPTY;
        }
        if (obj instanceof Attribute) {
            return ((Attribute)obj).getValue();
        } else if (obj instanceof Text) {
            return ((Text)obj).getText();
        } else {
            return obj.toString();
        }
    }

    public static String getStringValue(Element context, String path) {
        if (context == null) {
            return null;
        }
        Object obj = evaluateFirst(context, path);
        return getElementValue(obj);
    }

    public static Integer getIntValue(Element context, String path) {
        String value = getStringValue(context, path);
        if (StringUtils.isBlank(value)) {
            return null;
        }
        return Integer.valueOf(value);
    }

}
