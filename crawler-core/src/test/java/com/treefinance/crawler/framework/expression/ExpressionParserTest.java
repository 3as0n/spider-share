package com.treefinance.crawler.framework.expression;

import com.google.common.collect.ImmutableList;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Jerry
 * @since 15:05 2018/5/18
 */
public class ExpressionParserTest {

    @Test
    public void evalExp() {
        Map<String, Object> map = new HashMap<>();
        map.put("name", "Jerry");
        map.put("place", "place1");
        map.put("age", 1);

        Map<String, Object> map1 = new HashMap<>();
        map1.put("name", "test");
        map1.put("place", "place2");

        ExpressionParser parser = ExpressionParser.parse("hello, ${name}. Go to ${place}. ${age}");

        String value = parser.evalExp(ImmutableList.of(map1, map));

        Assert.assertEquals("hello, test. Go to place2. 1", value);

        Object result = parser.evalExp(map);

        Assert.assertEquals("hello, Jerry. Go to place1. 1", result);

        map.put("name", "Kitty");

        result = parser.evalExp(map);

        Assert.assertEquals("hello, Kitty. Go to place1. 1", result);

        parser.reset(" ${name} ");

        result = parser.evalExp(map);

        Assert.assertEquals(" Kitty ", result);

        map.put("name", "Joy");

        result = parser.evalExp(map);

        Assert.assertEquals(" Joy ", result);

        parser.reset("  -");

        result = parser.evalExp(map);

        Assert.assertEquals("  -", result);

        parser.reset(null);

        result = parser.evalExp(map);

        Assert.assertEquals("", result);

        parser.reset(" ${name} ");

        result = parser.evalExpWithObject(map);

        Assert.assertEquals("Joy", result);

        parser.reset("${name}");

        result = parser.evalExpWithObject(map);
        Assert.assertEquals(String.class, result.getClass());
        Assert.assertEquals("Joy", result);

        map.put("date", 10);

        parser.reset(" ${date} ");

        result = parser.evalExpWithObject(map);

        Assert.assertEquals(Integer.class, result.getClass());
        Assert.assertEquals(10, result);

        parser.reset("${date}");

        result = parser.evalExpWithObject(map);

        Assert.assertEquals(Integer.class, result.getClass());
        Assert.assertEquals(10, result);

        parser.reset("-${date}_");

        result = parser.evalExpWithObject(map);

        Assert.assertEquals(String.class, result.getClass());
        Assert.assertEquals("-10_", result);

        parser.reset("- ${date} _");

        result = parser.evalExpWithObject(map);

        Assert.assertEquals(String.class, result.getClass());
        Assert.assertEquals("- 10 _", result);
    }
}