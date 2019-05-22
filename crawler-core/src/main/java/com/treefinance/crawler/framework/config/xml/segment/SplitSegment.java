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
 * @since Feb 20, 2014 4:16:53 PM
 */
@Path(".[@type='split']")
public class SplitSegment extends AbstractSegment {

    /**
     *
     */
    private static final long serialVersionUID = -4391709858547716289L;

    private String splitString;

    private Boolean append;

    @Attr("append")
    public Boolean getAppend() {
        return append;
    }

    @Node("@append")
    public void setAppend(Boolean append) {
        this.append = append;
    }

    @Attr("value")
    public String getSplitString() {
        return splitString;
    }

    @Node("@value")
    public void setSplitString(String splitString) {
        this.splitString = splitString;
    }
}
