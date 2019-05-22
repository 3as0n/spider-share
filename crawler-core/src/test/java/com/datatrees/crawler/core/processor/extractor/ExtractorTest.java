package com.datatrees.crawler.core.processor.extractor;

import com.ProcessorContextFactory;
import com.TestHelper;
import com.treefinance.crawler.framework.boot.Extractor;
import com.treefinance.crawler.framework.context.ExtractorProcessorContext;
import com.treefinance.crawler.framework.context.function.ExtractRequest;
import com.treefinance.crawler.framework.context.function.SpiderResponse;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Jerry
 * @since 11:50 2018/7/12
 */
public class ExtractorTest {
    private ExtractorProcessorContext context;

    @Before
    public void setUp() {
        this.context = ProcessorContextFactory.createExtractorProcessorContext("10086_app", "example/operator/10086_app/ExtractorConfig.xml");
    }

    @Test
    public void extract() {
        Map<String, Object> map = new HashMap<>();
        map.put("url", "billInfo");
        map.put("pageContent", TestHelper.getFileContent("example/operator/10086_app/PageContent.json"));
        map.put("billPhone", "123");
        map.put("realname", "test");

        ExtractRequest request = ExtractRequest.newBuilder().setInput(map).setExtractContext(context).build();
        SpiderResponse response = Extractor.extract(request);
        System.out.println(response);
    }

    @Test
    public void name() {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyyMMdd");
        Date date = formatter.parseLocalDateTime("2018-03-01").toDate();

        System.out.println(date);
    }
}