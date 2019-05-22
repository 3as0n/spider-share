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

package com.treefinance.crawler.framework.config.xml.operation;

import com.treefinance.crawler.framework.config.annotation.Attr;
import com.treefinance.crawler.framework.config.annotation.Node;
import com.treefinance.crawler.framework.config.annotation.Path;
import com.treefinance.crawler.framework.config.annotation.Tag;
import com.treefinance.crawler.framework.config.enums.operation.escape.EscapeType;
import com.treefinance.crawler.framework.config.enums.operation.escape.HandlingType;

/**
 * @author <A HREF="mailto:wangcheng@datatrees.com.cn">Cheng Wang</A>
 * @version 1.0
 * @since Mar 26, 2014 8:02:11 PM
 */
@Tag("operation")
@Path(".[@type='escape']")
public class EscapeOperation extends AbstractOperation {

    /**
     *
     */
    private static final long serialVersionUID = 2187657297214938947L;

    private EscapeType escapeType;

    private HandlingType handlingType;

    @Attr("escape-type")
    public EscapeType getEscapeType() {
        return escapeType;
    }

    @Node("@escape-type")
    public void setEscapeType(String escapeType) {
        this.escapeType = EscapeType.getEscapeType(escapeType);
    }

    @Attr("handling-type")
    public HandlingType getHandlingType() {
        return handlingType;
    }

    @Node("@handling-type")
    public void setHandlingType(String handlingType) {
        this.handlingType = HandlingType.getOperationType(handlingType);
    }

}
