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

package com.datatrees.spider.share.domain;

import java.util.HashMap;
import java.util.Map;

/**
 * @author <A HREF="">Cheng Wang</A>
 * @version 1.0
 * @since 2015年7月31日 下午12:21:28
 */
@SuppressWarnings({"rawtypes", "serial", "unchecked"})
public abstract class AbstractData extends HashMap<String, Object> {

    public static final String UNIQUESIGN = "uniqueSign";

    public static final String URL = "url";

    // maybe collection
    public static final String PAGECONTENT = "pageContent";

    public static final String RESULTTYPE = "resultType";

    public static final String EXTRAINFO = "extraInfo";

    public String getResultType() {
        return (String)this.get(RESULTTYPE);
    }

    public void setResultType(String resultType) {
        this.put(RESULTTYPE, resultType);
    }

    public Object getPageContent() {
        return this.get(PAGECONTENT);
    }

    public void setPageContent(Object pageContent) {
        this.put(PAGECONTENT, pageContent);
    }

    public String getUniqueSign() {
        return (String)this.get(UNIQUESIGN);
    }

    public void setUniqueSign(String uniqueSign) {
        this.put(UNIQUESIGN, uniqueSign);
    }

    public String getUrl() {
        return (String)this.get(URL);
    }

    public void setUrl(String url) {
        this.put(URL, url);
    }

    public Map<String, Object> getExtraInfo() {
        if (this.get(EXTRAINFO) != null && this.get(EXTRAINFO) instanceof Map) {
            return (Map<String, Object>)this.get(EXTRAINFO);
        } else {
            return null;
        }
    }

}
