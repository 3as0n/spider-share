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

package com.treefinance.crawler.framework.process.operation.impl;

import com.treefinance.crawler.framework.config.xml.extractor.FieldExtractor;
import com.treefinance.crawler.framework.config.xml.operation.ExtractOperation;
import com.treefinance.crawler.framework.context.RequestUtil;
import com.treefinance.crawler.framework.context.function.SpiderRequest;
import com.treefinance.crawler.framework.context.function.SpiderResponse;
import com.treefinance.crawler.framework.parser.HTMLParser;
import com.treefinance.crawler.framework.process.operation.Operation;
import com.treefinance.crawler.framework.util.UrlExtractor;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nonnull;

import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

/**
 * handle codec operation decode/encode etc..
 * 
 * @author <A HREF="">Cheng Wang</A>
 * @version 1.0
 * @since Mar 27, 2014 12:30:43 PM
 */
public class ExtractOperationImpl extends Operation<ExtractOperation> {

    public ExtractOperationImpl(@Nonnull ExtractOperation operation, @Nonnull FieldExtractor extractor) {
        super(operation, extractor);
    }

    @Override
    protected Object doOperation(@Nonnull ExtractOperation operation, @Nonnull Object operatingData, @Nonnull SpiderRequest request, @Nonnull SpiderResponse response)
        throws Exception {
        // get input
        String content = (String)operatingData;
        String baseURL = RequestUtil.getCurrentUrl(request).getUrl();
        String url = this.extractHtmlLink(content, baseURL);
        if (StringUtils.isBlank(url)) {
            List<String> textUrls = UrlExtractor.extract(content);
            url = CollectionUtils.isNotEmpty(textUrls) ? textUrls.get(0) : StringUtils.EMPTY;
        }

        return url;
    }

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
}
