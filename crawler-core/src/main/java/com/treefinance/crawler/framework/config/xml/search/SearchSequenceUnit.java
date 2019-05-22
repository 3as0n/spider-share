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

package com.treefinance.crawler.framework.config.xml.search;

import com.treefinance.crawler.framework.config.annotation.Attr;
import com.treefinance.crawler.framework.config.annotation.Node;
import com.treefinance.crawler.framework.config.annotation.Tag;
import com.treefinance.crawler.framework.config.xml.page.Page;

import java.io.Serializable;

/**
 * @author <A HREF="mailto:wangcheng@datatrees.com.cn">Cheng Wang</A>
 * @version 1.0
 * @since Feb 7, 2014 5:26:35 PM
 */
@Tag("page")
public class SearchSequenceUnit implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -3034367943949209346L;

    private Integer depth;

    private Page page;

    @Attr("depth")
    public Integer getDepth() {
        return depth;
    }

    @Node("@depth")
    public void setDepth(Integer depth) {
        this.depth = depth;
    }

    @Attr(value = "ref", referenced = true)
    public Page getPage() {
        return page;
    }

    @Node(value = "@ref", referenced = true)
    public void setPage(Page page) {
        this.page = page;
    }

}
