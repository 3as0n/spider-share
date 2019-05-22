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
import org.apache.commons.lang3.StringUtils;

@Tag("operation")
@Path(".[@type='append']")
public class AppendOperation extends AbstractOperation {

    private static final long serialVersionUID = -7536995227560319224L;

    private Integer index;

    private String value;

    @Tag
    public String getValue() {
        return StringUtils.defaultString(value);
    }

    @Node("text()")
    public void setValue(String value) {
        this.value = value;
    }

    @Attr("index")
    public Integer getIndex() {
        return index == null ? Integer.valueOf(-1) : index;
    }

    @Node("@index")
    public void setIndex(Integer index) {
        this.index = index;
    }
}
