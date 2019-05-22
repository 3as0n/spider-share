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

package com.treefinance.crawler.framework.parser;

import it.unimi.dsi.parser.BulletParser;
import org.apache.commons.lang.StringUtils;

import java.util.HashMap;

/**
 * HTMLParser.java 1.0 Jul 19, 2011 The content parser of page html content.
 * 
 * @author <A HREF="">Cheng Wang</A>
 * @version 1.0
 * @since 1.0
 */
public class HTMLParser {

    private BulletParser bulletParser;

    private LinkExtractor linkExtractor;

    /**
     * constructor
     */
    public HTMLParser() {
        bulletParser = new BulletParser();
        linkExtractor = new LinkExtractor();
    }

    /**
     * parse method
     * 
     * @param htmlContent
     * @param contextURL
     */
    public void parse(String htmlContent, String contextURL) {
        char[] chars = htmlContent.toCharArray();
        linkExtractor.setContextURL(contextURL);
        bulletParser.setCallback(linkExtractor);
        bulletParser.parse(chars);
    }

    /**
     * get page title
     * 
     * @return title
     */
    public String getTitle() {
        String title = linkExtractor.title.toString().trim();
        if (StringUtils.isNotEmpty(title) && title.length() > 2048) {
            title = title.substring(0, 2048);
        }
        return title;
    }

    /**
     * get found links
     * 
     * @return links
     */
    public HashMap<String, String> getLinks() {
        return linkExtractor.urls;
    }
}
