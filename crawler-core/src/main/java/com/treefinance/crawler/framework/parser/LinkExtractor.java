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

package com.treefinance.crawler.framework.parser;

import com.treefinance.crawler.framework.protocol.util.UrlUtils;
import it.unimi.dsi.lang.MutableString;
import it.unimi.dsi.parser.Attribute;
import it.unimi.dsi.parser.BulletParser;
import it.unimi.dsi.parser.Element;
import it.unimi.dsi.parser.callback.DefaultCallback;
import it.unimi.dsi.util.TextPattern;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * LinkExtractor.java 1.0 Jul 19, 2011 A callback extracting links.
 * 
 * @author <A HREF="">Cheng Wang</A>
 * @version 1.0
 * @since 1.0
 */
public class LinkExtractor extends DefaultCallback {

    /**
     * The pattern prefixing the URL in a <samp>META </samp> <samp>HTTP-EQUIV </samp> element of refresh type.
     */
    private static final TextPattern URLEQUAL_PATTERN = new TextPattern("URL=", TextPattern.CASE_INSENSITIVE);

    private static Logger log = LoggerFactory.getLogger(LinkExtractor.class);

    /** The URLs resulting from the parsing process. */
    // public final Set<String> urls = new ObjectLinkedOpenHashSet<String>();
    public final HashMap<String, String> urls = new LinkedHashMap<String, String>(); // URL,

    /** The title resulting from the parsing process. */
    public final MutableString title = new MutableString();

    // anchorText
    private String nextURL = null;

    private MutableString nextAnchorText = null;

    /**
     * The URL contained in the first <samp>META </samp> <samp>HTTP-EQUIV </samp> element of refresh type (if any).
     */
    private String metaRefresh = null;

    /**
     * The URL contained in the first <samp>META </samp> <samp>HTTP-EQUIV </samp> element of location type (if any).
     */
    private String metaLocation = null;

    /** The URL contained in the first <samp>BASE </samp> element (if any). */
    private String base = null;

    private String contexURL = null;

    /** True if we are in the middle of the title. */
    private boolean inTitle;

    private boolean firstTitle = true;

    /**
     * Configure the parser to parse elements and certain attributes.
     * 
     * @param parser the bullet parser
     */
    public void configure(final BulletParser parser) {
        parser.parseText(true);
        parser.parseTags(true);
        parser.parseAttributes(true);
        parser.parseAttribute(Attribute.SRC);
        parser.parseAttribute(Attribute.HREF);
        parser.parseAttribute(Attribute.ONCLICK);
        parser.parseAttribute(Attribute.HTTP_EQUIV);
        parser.parseAttribute(Attribute.CONTENT);
        parser.parseAttribute(Attribute.DATA);
    }

    /**
     * callback when document started.
     */
    public void startDocument() {
        urls.clear();
        base = null;
        metaLocation = null;
        metaRefresh = null;
        title.length(0);
        inTitle = false;
    }

    /**
     * callback when text characters started.
     * 
     * @param characters text
     * @param offset offset
     * @param length length
     * @param flowBroken broken flow
     * @return true or false
     */
    public boolean characters(final char[] characters, final int offset, final int length, final boolean flowBroken) {
        if (inTitle) {
            title.append(characters, offset, length);
        }
        if (nextURL != null) {
            if (nextAnchorText == null) {
                nextAnchorText = new MutableString();
            }
            nextAnchorText.append(characters, offset, length);
        }
        return true;
    }

    /**
     * callback when an element ended
     * 
     * @param element element
     * @return true or false
     */
    public boolean endElement(final Element element) {
        inTitle = false;
        if (nextURL != null) {
            String anchorText = null;
            if (nextAnchorText != null) {
                anchorText = nextAnchorText.toString().trim();
            }
            addURL(nextURL, anchorText);
            nextURL = null;
            nextAnchorText = null;
        }
        return true;
    }

    /**
     * callback for an element started.
     * 
     * @param element the element
     * @param attrMap attributes
     * @return true or false
     */
    public boolean startElement(final Element element, final Map<Attribute, MutableString> attrMap) {
        inTitle = element == Element.TITLE && firstTitle;
        if (inTitle)
            firstTitle = false;

        Object s;

        if (element == Element.A || element == Element.AREA || element == Element.LINK) {
            s = attrMap.get(Attribute.HREF);
            if (s != null) {
                nextURL = s.toString();
            }
            s = attrMap.get(Attribute.ONCLICK);
            if (s != null) {
                String str = s.toString();
                if (str.contains("window.open(")) {
                    String[] splittedStrings = str.split("'|\"");
                    if (splittedStrings.length >= 2) {
                        str = splittedStrings[1];
                        log.debug("window open url: {}", str);
                        nextURL = str;
                    }
                } else {
                    // remove onclick js url,like return xxx
                    // addURL(str, "onclick");
                }
            }
        }

        // IFRAME or FRAME + SRC
        if (element == Element.IFRAME || element == Element.FRAME || element == Element.EMBED) {
            s = attrMap.get(Attribute.SRC);
            if (s != null) {
                nextURL = s.toString();
            }
        }

        // object + data
        if (element == Element.OBJECT) {
            s = attrMap.get(Attribute.DATA);
            if (s != null) {
                nextURL = s.toString();
            }
        }
        // META REFRESH/LOCATION
        if (element == Element.META) {
            final MutableString equiv = attrMap.get(Attribute.HTTP_EQUIV);
            final MutableString content = attrMap.get(Attribute.CONTENT);
            if (equiv != null && content != null) {
                equiv.toLowerCase();

                // http-equiv="refresh" content="0;URL=http://foo.bar/..."
                if (equiv.equals("refresh") && (metaRefresh == null)) {

                    final int pos = URLEQUAL_PATTERN.search(content);
                    if (pos != -1) {
                        metaRefresh = content.substring(pos + URLEQUAL_PATTERN.length()).toString();
                        addURL(metaRefresh, null);
                    }
                }

                // http-equiv="location" content="http://foo.bar/..."
                if (equiv.equals("location") && (metaLocation == null)) {
                    metaLocation = attrMap.get(Attribute.CONTENT).toString();
                    addURL(metaLocation, null);
                }
            }
        }
        if (StringUtils.isNotBlank(nextURL)) {
            addURL(nextURL, null);
        }
        return true;
    }

    /**
     * Returns the URL specified by <samp>META </samp> <samp>HTTP-EQUIV </samp> elements of location type. More
     * precisely, this method returns a non- <code>null</code> result iff there is at least one <samp>META HTTP-EQUIV
     * </samp> element specifying a location URL (if there is more than one, we keep the first one).
     * 
     * @return the first URL specified by a <samp>META </samp> <samp>HTTP-EQUIV </samp> elements of location type, or
     *         <code>null</code>.
     */
    public String metaLocation() {
        return metaLocation;
    }

    /**
     * Returns the URL specified by the <samp>BASE </samp> element. More precisely, this method returns a non-
     * <code>null</code> result iff there is at least one <samp>BASE </samp> element specifying a derelativisation URL
     * (if there is more than one, we keep the first one).
     * 
     * @return the first URL specified by a <samp>BASE </samp> element, or <code>null</code>.
     */
    public String base() {
        return base;
    }

    /**
     * Returns the URL specified by <samp>META </samp> <samp>HTTP-EQUIV </samp> elements of refresh type. More
     * precisely, this method returns a non- <code>null</code> result iff there is at least one <samp>META HTTP-EQUIV
     * </samp> element specifying a refresh URL (if there is more than one, we keep the first one).
     * 
     * @return the first URL specified by a <samp>META </samp> <samp>HTTP-EQUIV </samp> elements of refresh type, or
     *         <code>null</code>.
     */
    public String metaRefresh() {
        return metaRefresh;
    }

    /**
     * set context url for this object
     * 
     * @param contextURL
     */
    public void setContextURL(String contextURL) {
        this.contexURL = contextURL;
    }

    /**
     * get absolute url and add urls into url map
     * 
     * @param url
     * @param anchorText
     */
    private void addURL(String url, String anchorText) {
        String absoluteURL = UrlUtils.resolveUrl(contexURL, url.trim());
        if (StringUtils.isNotEmpty(anchorText) && anchorText.length() > 2048) {
            anchorText = "";
        }
        urls.put(absoluteURL, anchorText);
    }
}
