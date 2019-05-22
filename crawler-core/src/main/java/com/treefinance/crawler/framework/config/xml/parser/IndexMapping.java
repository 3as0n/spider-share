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

package com.treefinance.crawler.framework.config.xml.parser;

import com.treefinance.crawler.framework.config.annotation.Attr;
import com.treefinance.crawler.framework.config.annotation.Node;
import com.treefinance.crawler.framework.config.annotation.Tag;

import java.io.Serializable;

/**
 * @author <A HREF="mailto:wangcheng@datatrees.com.cn">Cheng Wang</A>
 * @version 1.0
 * @since Jan 9, 2014 9:25:00 PM
 */
@Tag("map")
public class IndexMapping implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 6418187744014381993L;

    private Integer groupIndex;

    private String placeholder;

    @Attr("group-index")
    public Integer getGroupIndex() {
        return groupIndex;
    }

    @Node("@group-index")
    public void setGroupIndex(Integer groupIndex) {
        this.groupIndex = groupIndex;
    }

    @Attr("placeholder")
    public String getPlaceholder() {
        return placeholder;
    }

    @Node("@placeholder")
    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
    }

}
