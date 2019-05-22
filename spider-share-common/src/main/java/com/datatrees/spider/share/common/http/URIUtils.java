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

package com.datatrees.spider.share.common.http;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * url特殊处理
 * 
 * @author zhouxinghai
 * @date 2017/11/21
 */
public class URIUtils {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(URIUtils.class);

    public static URI create(String fullUrl) {
        try {
            return URI.create(fullUrl);
        } catch (Exception e) {
            logger.warn("create url error,fullUrl={}", fullUrl, e);
            return createAndEncode(fullUrl);
        }
    }

    public static URI createAndEncode(String fullUrl) {
        try {
            if (!StringUtils.contains(fullUrl, "?")) {
                return URI.create(fullUrl);
            }
            Map<String, String> params = getQueryParams(fullUrl);
            if (null == params || params.isEmpty()) {
                return URI.create(fullUrl);
            }
            String url = StringUtils.substringBefore(fullUrl, "?");
            List<NameValuePair> pairs = new ArrayList<NameValuePair>(params.size());
            for (Map.Entry<String, String> entry : params.entrySet()) {
                pairs.add(new BasicNameValuePair(entry.getKey(), null == entry.getValue() ? "" : String.valueOf(entry.getValue())));
            }
            url = url + "?" + EntityUtils.toString(new UrlEncodedFormEntity(pairs, "UTF-8"));
            return URI.create(url);
        } catch (Throwable e) {
            logger.error("createAndEncode error fullUrl={}", fullUrl, e);
            return null;
        }
    }

    public static Map<String, String> getQueryParams(String fullUrl) {
        if (StringUtils.isBlank(fullUrl)) {
            logger.warn("fullUrl is blank");
            return null;
        }
        Map<String, String> map = new HashMap<>();
        String queryString = StringUtils.substringAfter(fullUrl, "?");
        if (StringUtils.isNotBlank(queryString)) {
            String[] kvs = StringUtils.split(queryString, "&");
            if (null == kvs || kvs.length == 0) {
                logger.warn("query param is blank ,fullUrl={}", fullUrl);
                return map;
            }
            for (String kv : kvs) {
                if (StringUtils.isBlank(kv)) {
                    logger.warn("key value is blank");
                    continue;
                }
                String[] ss = StringUtils.split(kv, "=");
                if (ss.length == 1) {
                    logger.warn("invalid namevalue kv={}", kv);
                    map.put(ss[0], "");
                } else {
                    map.put(ss[0], ss[1]);
                }
            }
        }
        return map;
    }

}
