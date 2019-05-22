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

package com.treefinance.crawler.framework.protocol.util;

import com.datatrees.common.util.GsonUtils;
import com.google.gson.reflect.TypeToken;
import com.treefinance.crawler.framework.protocol.NameValuePair;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * it
 * 
 * @author <A HREF="">Cheng Wang</A>
 * @version 1.0
 * @since Feb 14, 2014 10:19:37 AM
 */
public class HeaderParser {

    public static final Logger LOG = LoggerFactory.getLogger(HeaderParser.class);

    public static List<NameValuePair> getHeaders(String header) {
        List<NameValuePair> headers = new ArrayList<NameValuePair>();
        try {
            if (StringUtils.isNotEmpty(header)) {
                Map<String, String> headerMap = GsonUtils.fromJson(header, new TypeToken<Map<String, String>>() {}.getType());
                for (Map.Entry<String, String> next : headerMap.entrySet()) {
                    headers.add(new NameValuePair(next.getKey(), next.getValue()));
                }
            }

        } catch (Exception e) {
            LOG.error("parser json error! " + header);
        }

        return headers;
    }

    public static Map<String, String> getHeaderMaps(String header) {
        Map<String, String> headers = new HashMap<String, String>();
        try {
            if (StringUtils.isNotEmpty(header)) {
                headers.putAll((Map)GsonUtils.fromJson(header, new TypeToken<Map<String, String>>() {}.getType()));
            }

        } catch (Exception e) {
            LOG.error("parser json error! " + header);
        }

        return headers;
    }
}
