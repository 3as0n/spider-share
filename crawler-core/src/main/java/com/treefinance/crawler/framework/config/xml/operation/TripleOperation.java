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
import com.treefinance.crawler.framework.config.enums.operation.triple.TripleType;

@Tag("operation")
@Path(".[@type='triple']")
public class TripleOperation extends AbstractOperation {

    /**
     *
     */
    private static final long serialVersionUID = 393008470332561298L;

    private TripleType tripleType;

    private String value;

    @Attr("triple-type")
    public TripleType getTripleType() {
        return tripleType;
    }

    @Node("@triple-type")
    public void setTripleType(String tripleType) {
        this.tripleType = TripleType.getOperationType(tripleType);
    }

    @Tag
    public String getValue() {
        return value;
    }

    @Node("text()")
    public void setValue(String value) {
        this.value = value;
    }
}
