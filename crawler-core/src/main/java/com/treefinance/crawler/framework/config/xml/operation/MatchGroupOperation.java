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
 * @author <A HREF="mailto:wangcheng@datatrees.com.cn">Cheng Wang</A>
 * @version 1.0
 * @since Feb 7, 2014 2:45:34 PM
 */
@Tag("operation")
@Path(".[@type='matchgroup']")
public class MatchGroupOperation extends AbstractOperation {

    /**
     *
     */
    private static final long serialVersionUID = -8222059832401343348L;

    private Integer groupIndex;

    private String sourceId;

    @Attr("index")
    public Integer getGroupIndex() {
        return groupIndex;
    }

    @Node("@index")
    public void setGroupIndex(Integer groupIndex) {
        this.groupIndex = groupIndex;
    }

    @Attr("source")
    public String getSourceId() {
        return sourceId;
    }

    @Node("@source")
    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "MatchGroupOperation [groupIndex=" + groupIndex + ", sourceId=" + sourceId + "]";
    }
}
