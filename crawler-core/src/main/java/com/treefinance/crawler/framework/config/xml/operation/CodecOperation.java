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
import com.treefinance.crawler.framework.config.enums.operation.codec.CodecType;
import com.treefinance.crawler.framework.config.enums.operation.codec.HandlingType;

/**
 * @author <A HREF="mailto:wangcheng@datatrees.com.cn">Cheng Wang</A>
 * @version 1.0
 * @since Mar 26, 2014 8:02:11 PM
 */
@Tag("operation")
@Path(".[@type='codec']")
public class CodecOperation extends AbstractOperation {

    /**
     *
     */
    private static final long serialVersionUID = -8553272059353645739L;

    private CodecType codecType;

    private HandlingType handlingType;

    @Attr("codec-type")
    public CodecType getCodecType() {
        return codecType;
    }

    @Node("@codec-type")
    public void setCodecType(String codecType) {
        this.codecType = CodecType.getOperationType(codecType);
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
