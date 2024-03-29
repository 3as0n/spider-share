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

package com.treefinance.crawler.framework.config.xml.segment;

import com.treefinance.crawler.framework.config.annotation.Attr;
import com.treefinance.crawler.framework.config.annotation.Node;
import com.treefinance.crawler.framework.config.annotation.Path;

/**
 * @author Jerry
 * @datetime 2015-07-17 19:42
 */
@Path(".[@type='jsonpath']")
public class JsonPathSegment extends AbstractSegment {

    private String jsonpath;

    @Attr("value")
    public String getJsonpath() {
        return jsonpath;
    }

    @Node("@value")
    public void setJsonpath(String jsonpath) {
        this.jsonpath = jsonpath;
    }

}
