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

package com.treefinance.crawler.framework.util.xpath.jsoup;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author <A HREF="mailto:wangcheng@datatrees.com.cn">Cheng Wang</A>
 * @version 1.0
 * @since 2015年7月6日 上午12:13:35
 */
public class JSoupQuerySyntax {

    private static final Logger log = LoggerFactory.getLogger(JSoupQuerySyntax.class);

    public static boolean isValid(String query) {
        // Check for null
        if (StringUtils.isEmpty(query)) {
            return false;
        }
        boolean flag;
        try {
            QueryParser.parse(query);
            flag = true;
        } catch (Exception e) // these exceptions are thrown if something is not ok
        {
            // ignore
            flag = false; // If something is not ok, the query is invalid
        }

        return flag; // All ok, query is valid
    }
}
