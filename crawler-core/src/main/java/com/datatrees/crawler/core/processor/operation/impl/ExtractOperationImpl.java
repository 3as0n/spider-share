/**
 * This document and its contents are protected by copyright 2015 and owned by datatrees.com Inc.
 * The copying and reproduction of this document and/or its content (whether wholly or partly) or
 * any incorporation of the same into any other material in any media or format of any kind is
 * strictly prohibited. All rights are reserved.
 * 
 * Copyright (c) datatrees.com Inc. 2015
 */
package com.datatrees.crawler.core.processor.operation.impl;

import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.datatrees.common.pipeline.Request;
import com.datatrees.common.pipeline.Response;
import com.datatrees.common.protocol.Constant;
import com.datatrees.crawler.core.processor.common.RequestUtil;
import com.datatrees.crawler.core.processor.common.html.HTMLParser;
import com.datatrees.crawler.core.processor.extractor.util.TextUrlExtractor;
import com.datatrees.crawler.core.processor.operation.Operation;

/**
 * handle codec operation decode/encode etc..
 * 
 * @author <A HREF="mailto:wangcheng@datatrees.com.cn">Cheng Wang</A>
 * @version 1.0
 * @since Mar 27, 2014 12:30:43 PM
 */
public class ExtractOperationImpl extends Operation {

    private static final Logger log  = LoggerFactory.getLogger(ExtractOperationImpl.class);

    private String extractHtmlLink(String content, String baseURL) {
        // parser url from html
        HTMLParser htmlParser = new HTMLParser();
        htmlParser.parse(content, baseURL);
        if (MapUtils.isNotEmpty(htmlParser.getLinks())) {
            Iterator<Entry<String, String>> urlIterator = htmlParser.getLinks().entrySet().iterator();
            Entry<String, String> fl = urlIterator.next();
            return fl.getKey();
        } else {
            return null;
        }
    }

    @Override
    public void process(Request request, Response response) throws Exception {
        // get input
        String content = getInput(request, response);
        String baseURL = RequestUtil.getCurrentUrl(request).getUrl();
        String url = this.extractHtmlLink(content, baseURL);
        if (StringUtils.isBlank(url)) {
            List<String> textUrls = TextUrlExtractor.extractor(content, Constant.URL_REGEX, 1);
            url = CollectionUtils.isNotEmpty(textUrls) ? textUrls.get(0) : "";
        }
        if (log.isDebugEnabled()) {
            log.debug("Extract operation result:" + url + " , content:" + content);
        }
        response.setOutPut(url);
    }
}
