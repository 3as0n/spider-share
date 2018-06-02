package com.treefinance.crawler.framework.expression.special;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.treefinance.crawler.framework.expression.spring.SpelExpParser;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Jerry
 * @since 01:29 2018/5/31
 */
public class PageExpParser {

    private static final Logger  LOGGER  = LoggerFactory.getLogger(PageExpParser.class);
    private static final String  REGEX   = "#\\{\\s*page\\s*,([\\d\\s.*/+-]+),([\\d\\s.*/+-]+),\\s*([\\d.]+)\\s*([+-]?)\\s*}";
    private static final Pattern PATTERN = Pattern.compile(REGEX);

    private PageExpParser() {
    }

    public static String eval(String text, int page) {
        LOGGER.debug("Eval page expression. input: {}", text);
        if (StringUtils.isEmpty(text)) return text;

        Matcher matcher = PATTERN.matcher(text);
        if (matcher.find()) {
            int pageNum = page;
            if (pageNum <= 0) {
                pageNum = 1;
            }

            StringBuffer buffer = new StringBuffer();

            do {
                int val = calculate(matcher, pageNum);
                matcher.appendReplacement(buffer, Integer.toString(val));
            } while (matcher.find());

            matcher.appendTail(buffer);

            String result = buffer.toString();
            LOGGER.debug("Page expression's eval-result: {}", result);
            return result;
        }

        return text;
    }

    private static int calculate(Matcher matcher, int page) {
        int start = SpelExpParser.parse(matcher.group(1), Integer.TYPE);
        int end = SpelExpParser.parse(matcher.group(2), Integer.TYPE);
        int offset = SpelExpParser.parse(matcher.group(3), Integer.TYPE);
        String offsetSign = matcher.group(4);
        if ("-".equals(offsetSign)) {
            offset = 0 - offset;
        }

        int pageNum = page;
        if (pageNum > end) {
            pageNum = end;
        }

        return start + offset * (pageNum - 1);
    }

}
