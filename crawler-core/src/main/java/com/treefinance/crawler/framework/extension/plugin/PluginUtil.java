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

package com.treefinance.crawler.framework.extension.plugin;

import com.datatrees.common.util.GsonUtils;
import com.google.gson.reflect.TypeToken;
import com.treefinance.crawler.framework.exception.PluginException;
import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.Map;

/**
 * @author <A HREF="">Cheng Wang</A>
 * @version 1.0
 * @since Apr 1, 2014 4:27:41 PM
 */
public final class PluginUtil {

    private PluginUtil() {}

    public static Map<String, Object> checkPluginResult(String result) throws PluginException {
        if (StringUtils.isEmpty(result)) {
            return Collections.emptyMap();
        }

        Map<String, Object> map;
        try {
            map = GsonUtils.fromJson(result, new TypeToken<Map<String, Object>>() {}.getType());
        } catch (Exception e) {
            throw new PluginException("Error parsing plugin result!", e);
        }

        if (map.containsKey("errorCode")) {
            throw new PluginException("error invoking plugin!");
        }

        return map;
    }
}
