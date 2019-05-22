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

package com.treefinance.crawler.framework.context;

import com.treefinance.crawler.framework.config.xml.page.AbstractPage;
import com.treefinance.crawler.framework.consts.Constants;
import com.treefinance.crawler.framework.context.function.LinkNode;
import com.treefinance.crawler.framework.context.function.SpiderResponse;
import com.treefinance.crawler.framework.process.fields.FieldExtractResultSet;
import com.treefinance.crawler.framework.protocol.ProtocolOutput;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author <A HREF="">Cheng Wang</A>
 * @version 1.0
 * @since Mar 6, 2014 4:45:30 PM
 */
public class ResponseUtil {

    private ResponseUtil() {}

    public static List<Object> getResponseObjectList(SpiderResponse response) {
        return (List<Object>)response.getAttribute(Constants.PAGE_EXTRACT_OBJECT_LIST);
    }

    public static void setResponseObjectList(SpiderResponse response, List<Object> objects) {
        response.setAttribute(Constants.PAGE_EXTRACT_OBJECT_LIST, objects);
    }

    public static void setPageExtractor(SpiderResponse response, AbstractPage pageExtractor) {
        response.setAttribute(Constants.PAGE_EXTRACT, pageExtractor);
    }

    public static AbstractPage getPageExtractor(SpiderResponse response) {
        return (AbstractPage)response.getAttribute(Constants.PAGE_EXTRACT);
    }

    public static FieldExtractResultSet prepareFieldExtractResultSet(SpiderResponse response) {
        return response.computeAttributeIfAbsent(Constants.FIELDS_RESULT_MAP, key -> new FieldExtractResultSet(), FieldExtractResultSet.class);
    }

    public static void setFieldExtractResultSet(SpiderResponse response, FieldExtractResultSet resultMap) {
        response.setAttribute(Constants.FIELDS_RESULT_MAP, resultMap);
    }

    public static FieldExtractResultSet getFieldExtractResultSet(SpiderResponse response) {
        return response.getAttribute(Constants.FIELDS_RESULT_MAP, FieldExtractResultSet.class);
    }

    public static FieldExtractResultSet removeFieldExtractResultSet(SpiderResponse response) {
        return (FieldExtractResultSet)response.removeAttribute(Constants.FIELDS_RESULT_MAP);
    }

    public static Map<String, Object> getFieldExtractResultMap(SpiderResponse response) {
        FieldExtractResultSet fieldExtractResultSet = getFieldExtractResultSet(response);

        return fieldExtractResultSet == null ? Collections.emptyMap() : fieldExtractResultSet.resultMap();
    }

    public static List<LinkNode> getResponseLinkNodes(SpiderResponse response) {
        return (List<LinkNode>)response.getAttribute(Constants.RESPONSE_LINKNODES);
    }

    public static void setResponseLinkNodes(SpiderResponse response, List<LinkNode> nodes) {
        response.setAttribute(Constants.RESPONSE_LINKNODES, nodes);
    }

    public static void setProtocolResponse(SpiderResponse response, ProtocolOutput out) {
        response.setAttribute(Constants.RESPONSE_Protocol_OUTPUT, out);
    }

    public static ProtocolOutput getProtocolResponse(SpiderResponse response) {
        return (ProtocolOutput)response.getAttribute(Constants.RESPONSE_Protocol_OUTPUT);
    }

}
