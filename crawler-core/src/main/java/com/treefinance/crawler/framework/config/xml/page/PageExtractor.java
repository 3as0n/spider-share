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

package com.treefinance.crawler.framework.config.xml.page;

import com.treefinance.crawler.framework.config.annotation.Attr;
import com.treefinance.crawler.framework.config.annotation.Node;
import com.treefinance.crawler.framework.config.annotation.Tag;
import com.treefinance.crawler.framework.config.xml.extractor.PageSource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author <A HREF="mailto:wangcheng@datatrees.com.cn">Cheng Wang</A>
 * @version 1.0
 * @since Feb 7, 2014 2:16:10 PM
 */
@Tag("page-extractor")
public class PageExtractor extends AbstractPage {

    private List<PageSource> pageSourceList;

    private Boolean disAlternative;

    public PageExtractor() {
        super();
        pageSourceList = new ArrayList<PageSource>();
    }

    @Attr("dis-alternative")
    public Boolean getDisAlternative() {
        return disAlternative;
    }

    @Node("@dis-alternative")
    public void setDisAlternative(Boolean disAlternative) {
        this.disAlternative = disAlternative;
    }

    @Tag("page-sources")
    public List<PageSource> getPageSourceList() {
        return Collections.unmodifiableList(pageSourceList);
    }

    @Node("page-sources/source")
    public void setPageSourceList(PageSource pageSource) {
        this.pageSourceList.add(pageSource);
    }

}
