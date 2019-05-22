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

package com.treefinance.crawler.framework.config.xml.plugin;

import com.treefinance.crawler.framework.config.annotation.Node;
import com.treefinance.crawler.framework.config.annotation.Path;
import com.treefinance.crawler.framework.config.annotation.Tag;

/**
 * @author <A HREF="mailto:wangcheng@datatrees.com.cn">Cheng Wang</A>
 * @version 1.0
 * @since Feb 7, 2014 11:31:40 AM
 */
@Path(".[@file-type='jar']")
@Tag("plugin")
public class JavaPlugin extends AbstractPlugin {

    private static final long serialVersionUID = 1361991918844867300L;

    private String mainClass;

    private String params;

    @Tag("main-class")
    public String getMainClass() {
        return mainClass;
    }

    @Node("main-class/text()")
    public void setMainClass(String mainClass) {
        this.mainClass = mainClass;
    }

    @Tag("params")
    public String getParams() {
        return params;
    }

    @Node("params/text()")
    public void setParams(String params) {
        this.params = params;
    }

}
