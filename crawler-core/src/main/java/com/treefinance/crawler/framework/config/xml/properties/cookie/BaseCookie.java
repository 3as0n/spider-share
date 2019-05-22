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

package com.treefinance.crawler.framework.config.xml.properties.cookie;

import com.treefinance.crawler.framework.config.annotation.Attr;
import com.treefinance.crawler.framework.config.annotation.Node;

/**
 * @author <A HREF="mailto:wangcheng@datatrees.com.cn">Cheng Wang</A>
 * @version 1.0
 * @since Mar 24, 2014 6:09:51 PM
 */
public class BaseCookie extends AbstractCookie {

    /**
     *
     */
    private static final long serialVersionUID = -1065814613979865015L;

    private Boolean coexist;

    @Attr("coexist")
    public Boolean getCoexist() {
        return coexist;
    }

    @Node("@coexist")
    public void setCoexist(Boolean coexist) {
        this.coexist = coexist;
    }

}
