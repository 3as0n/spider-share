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

import com.treefinance.crawler.framework.config.annotation.Node;
import com.treefinance.crawler.framework.config.annotation.Path;
import com.treefinance.crawler.framework.config.annotation.Tag;

/**
 * @author <A HREF="mailto:wangcheng@datatrees.com.cn">Cheng Wang</A>
 * @version 1.0
 * @since Mar 26, 2014 8:02:11 PM
 */
@Tag("operation")
@Path(".[@type='set']")
public class SetOperation extends AbstractOperation {

    /**
     *
     */
    private static final long serialVersionUID = 8770876253603267778L;

    private String value;

    /**
     * @return the value
     */
    @Tag
    public String getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    @Node("text()")
    public void setValue(String value) {
        this.value = value;
    }

}
