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

package com.treefinance.crawler.framework.config.xml.filter;

import com.treefinance.crawler.framework.config.annotation.Attr;
import com.treefinance.crawler.framework.config.annotation.Node;
import com.treefinance.crawler.framework.config.annotation.Tag;
import com.treefinance.crawler.framework.config.enums.url.FilterType;

import java.io.Serializable;

/**
 * @author <A HREF="mailto:wangcheng@datatrees.com.cn">Cheng Wang</A>
 * @version 1.0
 * @since Feb 7, 2014 10:50:15 AM
 */
@Tag("url-filter")
public class UrlFilter implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 8670172857562280034L;

    private FilterType type;

    private String filter;

    @Attr("type")
    public FilterType getType() {
        return type;
    }

    @Node("@type")
    public void setType(String type) {
        this.type = FilterType.getFilterType(type);
    }

    @Tag
    public String getFilter() {
        return filter;
    }

    @Node("text()")
    public void setFilter(String filter) {
        this.filter = filter;
    }
}
