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
 * @author <A HREF="mailto:wangcheng@datatrees.com.cn">Cheng Wang</A>
 * @version 1.0
 * @since Feb 20, 2014 4:16:27 PM
 */
@Path(".[@type='regex']")
public class RegexSegment extends AbstractSegment {

    /**
     *
     */
    private static final long serialVersionUID = 3272694924275477445L;

    private String regex;

    private Integer groupIndex;

    @Attr("value")
    public String getRegex() {
        return regex;
    }

    @Node("@value")
    public void setRegex(String regex) {
        this.regex = regex;
    }

    @Attr("group-index")
    public Integer getGroupIndex() {
        return groupIndex == null ? Integer.valueOf(0) : groupIndex;
    }

    @Node("@group-index")
    public void setGroupIndex(Integer groupIndex) {
        this.groupIndex = groupIndex;
    }

}
