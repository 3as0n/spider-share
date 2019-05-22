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

package com.treefinance.crawler.framework.config.xml.page;

import com.treefinance.crawler.framework.config.annotation.Attr;
import com.treefinance.crawler.framework.config.annotation.Node;
import com.treefinance.crawler.framework.config.annotation.Tag;

import java.io.Serializable;

/**
 * @author <A HREF="mailto:wangcheng@datatrees.com.cn">Cheng Wang</A>
 * @version 1.0
 * @since Feb 7, 2014 2:06:47 PM
 */
@Tag("replace")
public class Replacement implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 6456178618030598235L;

    private String from;

    private String to;

    @Attr("from")
    public String getFrom() {
        return from;
    }

    @Node("@from")
    public void setFrom(String from) {
        this.from = from;
    }

    @Attr("to")
    public String getTo() {
        return to;
    }

    @Node("@to")
    public void setTo(String to) {
        this.to = to;
    }

}
