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

package com.datatrees.spider.share.common.utils;

import cn.wanghaomiao.xpath.model.JXDocument;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class JsoupXpathUtils {

    private static final Logger logger = LoggerFactory.getLogger(JsoupXpathUtils.class);

    public static String selectFirst(String document, String xpath) {
        CheckUtils.checkNotBlank(document, "empty doc");
        CheckUtils.checkNotBlank(xpath, "empty xpath");
        JXDocument jxDocument = new JXDocument(document);
        String result = "";
        try {
            List<Object> list = jxDocument.sel(xpath);
            if (list.isEmpty()) {
                logger.warn("not found content for xpath={}", xpath);
                return null;
            }
            logger.info("selectFirst success xpath={},content={}", xpath, result.length() > 200 ? result.substring(0, 200) : result);
            return list.get(0).toString();
        } catch (Exception e) {
            logger.error("selectFirst error, xpath={},content={}", xpath, result.length() > 200 ? result.substring(0, 200) + "....." : result, e);
            return null;
        }
    }

    public static List<Map<String, String>> selectAttributes(String document, String xpath) {
        CheckUtils.checkNotBlank(document, "empty doc");
        CheckUtils.checkNotBlank(xpath, "empty xpath");
        JXDocument jxDocument = new JXDocument(document);
        List<Map<String, String>> list = new ArrayList<>();
        try {
            List<Object> elements = jxDocument.sel(xpath);
            if (null != elements && !elements.isEmpty()) {
                for (Object object : elements) {
                    Element element = Element.class.cast(object);
                    Iterator<Attribute> iterator = element.attributes().iterator();
                    Map<String, String> map = null;
                    while (iterator.hasNext()) {
                        if (null == map) {
                            map = new HashMap<>();
                        }
                        Attribute attribute = iterator.next();
                        map.put(attribute.getKey(), attribute.getValue());
                    }
                    if (null != map && !map.isEmpty()) {
                        list.add(map);
                    }
                }
            }
            logger.info("selectAttributes success xpath={},size={}", xpath, list.size());
        } catch (Exception e) {
            logger.error("selectAttributes error, xpath={},document={}", xpath, document, e);
        }
        return list;
    }

    public static List<Element> selectElements(String document, String xpath) {
        CheckUtils.checkNotBlank(document, "empty doc");
        CheckUtils.checkNotBlank(xpath, "empty xpath");
        JXDocument jxDocument = new JXDocument(document);
        try {
            List<Element> list = new ArrayList<>();
            List<Object> sels = jxDocument.sel(xpath);
            if (CollectionUtils.isNotEmpty(sels)) {
                for (Object o : sels) {
                    list.add((Element)o);
                }
            }
            logger.info("selectElementList size={},xpath={}", list.size(), xpath);
            return list;
        } catch (Throwable e) {
            logger.error("selectElementList error document={},xpath={}", document, xpath, e);
            return null;
        }
    }

}
