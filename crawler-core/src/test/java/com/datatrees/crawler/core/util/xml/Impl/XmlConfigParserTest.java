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