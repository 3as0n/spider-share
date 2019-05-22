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

package com.datatrees.crawler.core.util.xml.Impl;

import com.treefinance.crawler.framework.config.factory.xml.XmlConfigParser;
import com.treefinance.crawler.framework.config.xml.SearchConfig;
import com.treefinance.toolkit.util.io.Streams;
import org.junit.Test;

import java.io.InputStream;
import java.nio.charset.Charset;

/**
 * @author Jerry
 * @since 01:53 2018/7/10
 */
public class XmlConfigParserTest {

    @Test
    public void parse() throws Exception {
        String text;
        try (InputStream stream = getClass().getClassLoader().getResourceAsStream("searchConfig.xml")) {
            text = Streams.readToString(stream, Charset.defaultCharset());
        }

        SearchConfig searchConfig = XmlConfigParser.newParser().parse(text, SearchConfig.class);
        System.out.println(searchConfig);
    }
}