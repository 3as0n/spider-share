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
 * @since Feb 7, 2014 2:41:26 PM
 */
@Tag("operation")
@Path(".[@type='template']")
public class TemplateOperation extends AbstractOperation {

    /**
     *
     */
    private static final long serialVersionUID = 2782067152892732984L;

    private String template;

    private Boolean returnObject;

    private Boolean failover;

    @Tag
    public String getTemplate() {
        return template;
    }

    @Node("text()")
    public void setTemplate(String template) {
        this.template = template;
    }

    @Attr("return-object")
    public Boolean getReturnObject() {
        return returnObject;
    }

    @Node("@return-object")
    public void setReturnObject(Boolean returnObject) {
        this.returnObject = returnObject;
    }

    @Attr("failover")
    public Boolean getFailover() {
        return failover;
    }

    @Node("@failover")
    public void setFailover(Boolean failover) {
        this.failover = failover;
    }
}
