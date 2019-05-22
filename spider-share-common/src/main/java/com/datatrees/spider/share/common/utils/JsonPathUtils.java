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

package com.datatrees.spider.share.common.utils;

import com.jayway.jsonpath.PathNotFoundException;
import com.jayway.jsonpath.ReadContext;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonPathUtils {

    private static final Logger logger = LoggerFactory.getLogger(JsonPathUtils.class);

    public static <T> T select(ReadContext ctx, String select) {
        if (StringUtils.isBlank(select)) {
            return null;
        }
        try {
            return ctx.read(select);
        } catch (PathNotFoundException e) {
            logger.error("result not found for select={}", select);
            return null;
        }
    }

}
