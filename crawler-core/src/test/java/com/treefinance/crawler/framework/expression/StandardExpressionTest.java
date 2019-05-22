package com.treefinance.crawler.framework.expression;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Jerry
 * @since 17:30 2018/6/20
 */
public class StandardExpressionTest {

    @Test
    public void eval() {
        String seedUrl =
            "https://ebill.spdbccc.com.cn/cloudbank-portal/loginController/toLogin.action?zrz4ofa8h5LB2lSpnWi3oSrbno8oyFlIskYAiQYWZY3YFTv9xDHJNlaTsxTAYCHgX9IfmtrPFyEF7LnaI/j3NN/9$Ly53BpK16nb6RmiGpYC8zK5yxZm/UsLdEV$hnu24GmCTCD/fXlXT5oB/qqnEQ==";
        Map<String, Object> map = new HashMap<>();
        map.put("seedurl", seedUrl);

        String value = StandardExpression.eval("${seedurl}", map);
        Assert.assertEquals(seedUrl, value);
    }

    @Test
    public void evalSpecial() {
        Map<String, Object> map = new HashMap<>();
        map.put("name", "Jerry");
        map.put("currentMonth", "201806");
        map.put("lastMonth", "201805");
        map.put("last2Month", "201804");
        map.put("last3Month", "201803");
        map.put("last4Month", "201802");

        String value = StandardExpression.evalSpecial(" ${name} ", map);
        Assert.assertEquals("Jerry", value);

        value = StandardExpression.evalSpecial(" ${name1} ", map);
        Assert.assertNull(value);

        value = StandardExpression.evalSpecial("${currentMonth},${lastMonth},${last2Month},${last3Month},${last4Month}", map);
        Assert.assertEquals("201806,201805,201804,201803,201802", value);

        value = StandardExpression.evalSpecial(" ${currentMonth}, ${lastMonth}, ${last2Month}, ${last3Month}, ${last4Month} ", map);
        Assert.assertEquals(" 201806, 201805, 201804, 201803, 201802 ", value);
    }

    @Test
    public void evalSpecial1() {
        String value = StandardExpression.evalSpecial("${fid}_${mail.delivered-to}", null, null, true);
        System.out.println(value);
    }
}