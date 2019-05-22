/*
 * Copyright © 2015 - 2017 杭州大树网络技术有限公司. All Rights Reserved
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

package com.treefinance.crawler.framework.util.xpath;

import com.BaseConfigTest;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class XPathUtilTest extends BaseConfigTest {

    @Test
    public void test() {
        String content = getContent("xpath/table.html");
        String select = "//input[@id='i0327']";
        List<String> list = XPathUtil.getXpath(select, content);
        System.out.println(list);
        Assert.assertEquals(3, list.size());

        // multi xpath
        select = "//tr[@class='red']\"//tr[@class='red green']";
        list = XPathUtil.getXpath(select, content);
        Assert.assertEquals(3, list.size());

        select = "//tr[@id='r2']/td[2]/text()";
        list = XPathUtil.getXpath(select, content);
        Assert.assertEquals("22", list.get(0));

        select = "//tr[@id='r3']/td[2]/@id";
        list = XPathUtil.getXpath(select, content);
        Assert.assertEquals(1, list.size());
        Assert.assertEquals("td32", list.get(0));
    }

    @Test
    public void testText() {

        String select = "p:contains(联系地址):not(:has(p))/text(###)";
        // String select = tr:contains(Min.Payment):contains(Post
        // Date):not(:has(table:contains(Tran Date):contains(Post Date)))
        // "p:contains(费用返还明细):not(:has(p)) + table tr:not(:has(tr)):matches([\\d/]{8,})";
        String content = getContent("xpath/table.html");
        // System.out.println(content);
        // "tr:has(tbody div[id~=groupHeader]:contains(5491)) + tr div[id=fixBand7]>table>tbody>tr";
        // String select =
        // "tr:contains(卡号：6258********8487（大额账户）) + tr:not(:contains(卡号：)) div[id=fixBand7]>table>tbody>tr";

        // String select = "tr:not(:contains(卡号：)):has(table div tr:matches([\\d\\-]{8,})) + tr";

        // String select =
        // "tr:has(td>table div[id~=groupHeader]:contains(卡号：)):not(matches([\\d\\-]{8,}))";
        // List<String> list = new LinkedList<String>();
        //
        // String split = "尊敬的.{2,10}您好";
        // String[] regexResult = content.split(split);
        // if (regexResult != null && regexResult.length > 0) {
        // Matcher m = PatternUtils.matcher(split, content);
        // int count = 0;
        // while (count < regexResult.length) {
        // if (BooleanUtils.isTrue(null)) {
        // if (m.find()) {
        // regexResult[count] = regexResult[count] + m.group();
        // }
        // } else {
        // if (count > 0 && m.find()) {
        // regexResult[count] = m.group() + regexResult[count];
        // }
        // }
        // count++;
        // }
        // list.addAll(Arrays.asList(regexResult));
        // }

        List<String> list = XPathUtil.getXpath(select, content);
        for (String string : list) {
            System.out.println(string);
        }
        System.out.println(list.size());

        // content = getContent("comm/201507_fenqi.html");
        // //
        // "tr:has(tbody div[id~=groupHeader]:contains(5491)) + tr div[id=fixBand7]>table>tbody>tr";
        // // String select =
        // //
        // "tr:contains(卡号：6258********8487（大额账户）) + tr:not(:contains(卡号：)) div[id=fixBand7]>table>tbody>tr";
        //
        // // String select = "tr:not(:contains(卡号：)):has(table div tr:matches([\\d\\-]{8,})) + tr";
        //
        // // String select =
        // // "tr:has(td>table div[id~=groupHeader]:contains(卡号：)):not(matches([\\d\\-]{8,}))";
        //
        // list = XPathUtil.getXpath(select, content);
        // for (String string : list) {
        // System.out.println(string);
        // }
    }
}
