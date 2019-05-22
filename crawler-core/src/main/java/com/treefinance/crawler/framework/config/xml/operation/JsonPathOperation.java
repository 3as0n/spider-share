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

package com.treefinance.crawler.framework.config.xml.operation;

import com.treefinance.crawler.framework.config.annotation.Attr;
import com.treefinance.crawler.framework.config.annotation.Node;
import com.treefinance.crawler.framework.config.annotation.Path;
import com.treefinance.crawler.framework.config.annotation.Tag;

/**
 * @author Jerry
 * @datetime 2015-07-17 20:00
 */
@Tag("operation")
@Path(".[@type='jsonpath']")
public class JsonPathOperation extends AbstractOperation {

    /**
     *
     */
    private static final long serialVersionUID = -926221137233814333L;
    private String jsonpath;
    private Boolean emptyToNull;

    @Tag
    public String getJsonpath() {
        return jsonpath;
    }

    @Node("text()")
    public void setJsonpath(String jsonpath) {
        this.jsonpath = jsonpath;
    }

    @Attr("empty-to-null")
    public Boolean getEmptyToNull() {
        return emptyToNull;
    }

    @Node("@empty-to-null")
    public void setEmptyToNull(Boolean emptyToNull) {
        this.emptyToNull = emptyToNull;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "JsonPathOperation [jsonpath=" + jsonpath + "]";
    }

}
