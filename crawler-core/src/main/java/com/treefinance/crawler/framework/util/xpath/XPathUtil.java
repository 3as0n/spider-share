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

package com.treefinance.crawler.framework.util.xpath;

import com.treefinance.crawler.framework.util.xpath.jsoup.JSoupQuerySyntax;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.htmlcleaner.BaseToken;
import org.htmlcleaner.ContentNode;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.JDomSerializer;
import org.htmlcleaner.TagNode;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.jdom2.output.support.AbstractXMLOutputProcessor;
import org.jdom2.output.support.FormatStack;
import org.jdom2.util.NamespaceStack;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author <A HREF="mailto:wangcheng@datatrees.com.cn">Cheng Wang</A>
 * @version 1.0
 * @since 2015年7月6日 上午12:06:37
 */
public class XPathUtil {

    private static final Logger log = LoggerFactory.getLogger(XPathUtil.class);

    private static final String TEXT_SIGN = "/text(";

    private static final String SEPARATOR = "\"";

    public static List<String> getXpath(String select, String content) {
        List<String> result = new ArrayList<>();
        if (StringUtils.isEmpty(select) || StringUtils.isEmpty(content)) {
            log.warn("empty xpath or content");
            return result;
        }
        for (String selectString : select.split(SEPARATOR)) {
            result.addAll(routeToSelect(selectString, content));
        }
        return result;
    }

    /**
     * check to use jsoup or xpath
     */
    private static List<String> routeToSelect(String xpath, String text) {
        String content = contentPreClean(text);

        String select = xpath;
        String separate = StringUtils.EMPTY;
        boolean extractText = false;
        int i = xpath.indexOf(TEXT_SIGN);
        if (i != -1) {
            extractText = true;
            select = xpath.substring(0, i);

            int fromIndex = i + TEXT_SIGN.length();
            int end = xpath.indexOf(")", fromIndex);
            if (end != -1) {
                separate = xpath.substring(fromIndex, end);
            } else {
                separate = StringUtils.EMPTY;
            }
        }

        List<String> resList;
        if (JSoupQuerySyntax.isValid(select)) {
            log.debug("route to jsoup {}", select);

            if (extractText) {
                List<String> resultStrings = useJsoupSelect(select, content);
                String newContent = resultStrings.stream().collect(Collectors.joining());

                resList = useXpathSelect(".", newContent, true, separate);
            } else {
                resList = useJsoupSelect(select, content);
            }
        } else {
            log.debug("route to xpath {}", select);
            resList = useXpathSelect(select, content, extractText, separate);
        }
        if (CollectionUtils.isEmpty(resList)) {
            log.warn("empty select content! - {}", select);
        }
        return resList;
    }

    /**
     * @param select
     * @param content
     * @return
     */
    private static List<String> useJsoupSelect(String select, String content) {
        List<String> splits = new LinkedList<>();
        try {
            Elements elements = Jsoup.parse(content).select(select);
            if (elements != null) {
                for (org.jsoup.nodes.Element element : elements) {
                    splits.add(element.outerHtml());
                }
            }
        } catch (Exception e) {
            log.error("select use jsoup error! select " + select);
        }
        return splits;
    }

    /**
     * fix htmlcleaner's bug ,support clean dom wrapped with
     * <tr>
     * or
     * <td>
     */
    private static String contentPreClean(String content) {
        content = content.trim();
        if (content.endsWith("</td>") || content.endsWith("</tr>")) {
            content = "<table>" + content + "</table>";
        }
        return content;
    }

    /**
     * @param xpath
     * @param content
     * @return
     */
    private static List<String> useXpathSelect(String xpath, String content, boolean extractText, String separate) {
        List<String> splits = new LinkedList<>();
        try {
            HtmlCleaner cleaner = new HtmlCleaner();
            TagNode node = cleaner.clean(content);
            XMLOutputter out = new XMLOutputter();
            out.setFormat(Format.getCompactFormat().setEncoding("utf-8"));
            out.setXMLOutputProcessor(new CustomProcessor());
            JDomSerializer jdomSerializer = new JDomSerializer(cleaner.getProperties(), false);
            Object[] elements = node.evaluateXPath(xpath);
            if (ArrayUtils.isNotEmpty(elements)) {
                for (Object obj : elements) {
                    if (obj instanceof TagNode) {
                        TagNode resultNode = (TagNode)obj;
                        if (extractText) {
                            String text;
                            if (StringUtils.isEmpty(separate)) {
                                text = resultNode.getText().toString();
                            } else {
                                text = text(resultNode, separate);
                            }
                            splits.add(com.datatrees.common.util.StringUtils.replaceBlank(text));
                        } else {
                            Document doc = jdomSerializer.createJDom(resultNode);
                            splits.add(out.outputString(doc.getRootElement()));
                        }
                    } else if (obj instanceof CharSequence) {
                        splits.add(obj.toString());
                    }
                }
            }
        } catch (Exception e) {
            log.error("select use xpath error! xpath " + xpath);
        }
        return splits;
    }

    private static String text(TagNode tagNode, String separate) {
        List<String> list = collectTexts(tagNode);

        return list.stream().collect(Collectors.joining(separate));
    }

    private static List<String> collectTexts(TagNode tagNode) {
        List<String> list = new LinkedList<>();

        for (BaseToken item : tagNode.getAllChildren()) {
            if (item instanceof ContentNode) {
                String text = ((ContentNode)item).getContent();
                if (StringUtils.isNotBlank(text)) {
                    list.add(text);
                }
            } else if (item instanceof TagNode) {
                list.addAll(collectTexts((TagNode)item));
            }
        }

        return list;
    }

    static final class CustomProcessor extends AbstractXMLOutputProcessor {

        @Override
        public void process(final Writer out, final Format format, final Element element) throws IOException {

            FormatStack fStack = new FormatStack(format);
            fStack.setEscapeOutput(false);
            // If this is the root element we could pre-initialize the
            // namespace stack with the namespaces
            printElement(out, fStack, new NamespaceStack(), element);
            out.flush();
        }

    }

}
