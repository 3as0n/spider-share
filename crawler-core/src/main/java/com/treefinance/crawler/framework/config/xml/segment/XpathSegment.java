/*
 * Copyright © 2015 - 2018 杭州大树网络技术有限公司. All Rights Reserved
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.treefinance.crawler.framework.config.xml.segment;

import com.treefinance.crawler.framework.config.xml.segment.AbstractSegment;
import com.treefinance.crawler.framework.config.annotation.Attr;
import com.treefinance.crawler.framework.config.annotation.Node;
import com.treefinance.crawler.framework.config.annotation.Path;

/**
 * @author <A HREF="mailto:wangcheng@datatrees.com.cn">Cheng Wang</A>
 * @version 1.0
 * @since Feb 20, 2014 4:11:19 PM
 */
@Path(".[@type='xpath']")
public class XpathSegment extends AbstractSegment {

    private static final long   serialVersionUID = -8540951198783881443L;

    private              String xpath;

    @Attr("value")
    public String getXpath() {
        return xpath;
    }

    @Node("@value")
    public void setXpath(String xpath) {
        this.xpath = xpath;
    }
}