package com.datatrees.rawdatacentral.common.utils;

import java.util.*;

import cn.wanghaomiao.xpath.model.JXDocument;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsoupXpathUtils {

    private static final Logger logger = LoggerFactory.getLogger(JsoupXpathUtils.class);

    public static String selectFirst(String document, String xpath) {
        CheckUtils.checkNotBlank(document, "empty doc");
        CheckUtils.checkNotBlank(xpath, "empty xpath");
        JXDocument jxDocument = new JXDocument(document);
        String result = "";
        try {
            List<Object> list = jxDocument.sel(xpath);
            if (!list.isEmpty()) {
                logger.warn("not found content for xpath={}", xpath);
                result = list.get(0).toString();
            }
            logger.info("selectFirst success xpath={},content={}", xpath, result.length() > 200 ? result.substring(0, 200) : result);
        } catch (Exception e) {
            logger.error("selectFirst error, xpath={},content={}", xpath, result.length() > 200 ? result.substring(0, 200) + "....." : result, e);
        }
        return result;
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

}