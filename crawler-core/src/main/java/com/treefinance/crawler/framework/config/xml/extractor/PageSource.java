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

package com.treefinance.crawler.framework.config.xml.extractor;

import com.treefinance.crawler.framework.config.annotation.Attr;
import com.treefinance.crawler.framework.config.annotation.Node;
import com.treefinance.crawler.framework.config.annotation.Tag;
import com.treefinance.crawler.framework.config.xml.page.Regexp;
import com.treefinance.crawler.framework.config.xml.page.Replacement;
import com.treefinance.crawler.framework.config.xml.plugin.AbstractPlugin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author <A HREF="mailto:wangcheng@datatrees.com.cn">Cheng Wang</A>
 * @version 1.0
 * @since 2015年7月14日 上午11:54:09
 */
@Tag("source")
public class PageSource {

    private String field;

    private AbstractPlugin plugin;

    private List<Replacement> replacements = new ArrayList<>();

    private Regexp regexp;

    private String separator;

    public PageSource() {
        super();
    }

    @Attr(value = "plugin-ref", referenced = true)
    public AbstractPlugin getPlugin() {
        return plugin;
    }

    @Node(value = "@plugin-ref", referenced = true)
    public void setPlugin(AbstractPlugin plugin) {
        this.plugin = plugin;
    }

    @Attr("separator")
    public String getSeparator() {
        return separator;
    }

    @Node("@separator")
    public void setSeparator(String separator) {
        this.separator = separator;
    }

    @Attr("split")
    public String getSplit() {
        return getSeparator();
    }

    @Node("@split")
    public void setSplit(String split) {
        setSeparator(split);
    }

    @Attr("field")
    public String getField() {
        return field;
    }

    @Node("@field")
    public void setField(String field) {
        this.field = field;
    }

    @Tag("replaces")
    public List<Replacement> getReplacements() {
        return Collections.unmodifiableList(replacements);
    }

    @Node("replaces/replace")
    public void setReplacements(Replacement replacement) {
        this.replacements.add(replacement);
    }

    @Tag("regex")
    public Regexp getRegexp() {
        return regexp;
    }

    @Node("regex")
    public void setRegexp(Regexp regexp) {
        this.regexp = regexp;
    }
}
