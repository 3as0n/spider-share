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

import com.datatrees.common.util.json.annotation.Description;
import com.treefinance.crawler.framework.config.annotation.Attr;
import com.treefinance.crawler.framework.config.annotation.Node;
import com.treefinance.crawler.framework.config.enums.properties.CookieScope;
import org.apache.commons.lang.BooleanUtils;

import java.io.Serializable;

/**
 * @author <A HREF="mailto:wangcheng@datatrees.com.cn">Cheng Wang</A>
 * @version 1.0
 * @since Mar 31, 2014 4:24:13 PM
 */
@Description(value = "scope", keys = {"REQUEST", "USER_SESSION", "SESSION", "CUSTOM"}, types = {BaseCookie.class, BaseCookie.class, BaseCookie.class, CustomCookie.class})
public abstract class AbstractCookie implements Serializable, Cloneable {

    /**
     *
     */
    private static final long serialVersionUID = 2174790445783602115L;

    private CookieScope scope;

    private Boolean retainQuote;

    @Attr("scope")
    public CookieScope getScope() {
        return scope;
    }

    @Node("@scope")
    public void setScope(String scope) {
        this.scope = CookieScope.getCookieScope(scope);
    }

    @Attr("retain-quote")
    public Boolean getRetainQuote() {
        return BooleanUtils.isTrue(retainQuote);
    }

    @Node("@retain-quote")
    public void setRetainQuote(Boolean retainQuote) {
        this.retainQuote = retainQuote;
    }

    @Override
    public AbstractCookie clone() throws CloneNotSupportedException {
        return (AbstractCookie)super.clone();
    }
}
