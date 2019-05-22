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

package com.treefinance.crawler.framework.config.xml.extractor;

import com.treefinance.crawler.framework.config.annotation.Attr;
import com.treefinance.crawler.framework.config.annotation.Node;
import com.treefinance.crawler.framework.config.annotation.Tag;
import com.treefinance.crawler.framework.config.xml.page.PageExtractor;

import java.io.Serializable;

/**
 * @author <A HREF="mailto:wangcheng@datatrees.com.cn">Cheng Wang</A>
 * @version 1.0
 * @since 2015年7月14日 上午10:41:44
 */
@Tag("selector")
public class ExtractorSelector implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 8089954857564467379L;

    private String field;

    private String containRegex;

    private String disContainRegex;

    private PageExtractor pageExtractor;

    @Attr("field")
    public String getField() {
        return field;
    }

    @Node("@field")
    public void setField(String field) {
        this.field = field;
    }

    @Attr("contain")
    public String getContainRegex() {
        return containRegex;
    }

    @Node("@contain")
    public void setContainRegex(String containRegex) {
        this.containRegex = containRegex;
    }

    @Attr("dis-contain")
    public String getDisContainRegex() {
        return disContainRegex;
    }

    @Node("@dis-contain")
    public void setDisContainRegex(String disContainRegex) {
        this.disContainRegex = disContainRegex;
    }

    @Attr(value = "ref", referenced = true)
    public PageExtractor getPageExtractor() {
        return pageExtractor;
    }

    @Node(value = "@ref", referenced = true)
    public void setPageExtractor(PageExtractor pageExtractor) {
        this.pageExtractor = pageExtractor;
    }

}
