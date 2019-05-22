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

package com.treefinance.crawler.framework.config.xml.properties;

import com.treefinance.crawler.framework.config.annotation.Attr;
import com.treefinance.crawler.framework.config.annotation.Node;
import com.treefinance.crawler.framework.config.annotation.Tag;
import com.treefinance.crawler.framework.config.enums.properties.Mode;
import com.treefinance.crawler.framework.config.enums.properties.Scope;

import java.io.Serializable;

/**
 * @author <A HREF="mailto:wangcheng@datatrees.com.cn">Cheng Wang</A>
 * @version 1.0
 * @since Feb 7, 2014 10:33:45 AM
 */
public class Proxy implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 4167867516504535277L;

    private String proxy;

    private Scope scope;

    private String pattern;

    private Mode mode;

    @Tag
    public String getProxy() {
        return proxy;
    }

    @Node("text()")
    public void setProxy(String proxy) {
        this.proxy = proxy;
    }

    @Attr("scope")
    public Scope getScope() {
        return scope;
    }

    @Node("@scope")
    public void setScope(String scope) {
        this.scope = Scope.getScope(scope);
    }

    @Attr("pattern")
    public String getPattern() {
        return pattern;
    }

    @Node("@pattern")
    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    @Attr("mode")
    public Mode getMode() {
        return mode;
    }

    @Node("@mode")
    public void setMode(String mode) {
        this.mode = Mode.getMode(mode);
    }

}
