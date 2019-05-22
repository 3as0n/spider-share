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

package com.treefinance.crawler.framework.config.xml.parser;

import com.treefinance.crawler.framework.config.annotation.Node;
import com.treefinance.crawler.framework.config.annotation.Tag;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author <A HREF="mailto:wangcheng@datatrees.com.cn">Cheng Wang</A>
 * @version 1.0
 * @since Jan 9, 2014 9:24:57 PM
 */
@Tag("pattern")
public class ParserPattern implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 2300222575458432909L;

    private String regex;

    private List<IndexMapping> indexMappings = new ArrayList<IndexMapping>();// not necessary

    public ParserPattern() {
        super();
    }

    @Tag("regex")
    public String getRegex() {
        return regex;
    }

    @Node("regex/text()")
    public void setRegex(String regex) {
        this.regex = regex;
    }

    @Tag("mappings")
    public List<IndexMapping> getIndexMappings() {
        return Collections.unmodifiableList(indexMappings);
    }

    @Node("mappings/map")
    public void setIndexMappings(IndexMapping indexMapping) {
        this.indexMappings.add(indexMapping);
    }

    public void setIndexMappings(List<IndexMapping> indexMappings) {
        this.indexMappings = indexMappings;
    }

}
