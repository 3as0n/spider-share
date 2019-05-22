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
 * @author likun
 * @version $Id: DateTimeOperation.java, v 0.1 Jul 22, 2015 11:58:37 AM likun Exp $
 */
@Tag("operation")
@Path(".[@type='mapping']")
public class MappingOperation extends AbstractOperation {

    /**
     *
     */
    private static final long serialVersionUID = 904657740887574781L;

    private String groupName;

    @Attr("group-name")
    public String getGroupName() {
        return groupName;
    }

    @Node("@group-name")
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "MappingOperation [groupName=" + groupName + "]";
    }

}
