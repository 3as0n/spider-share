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

package com.treefinance.crawler.framework.expression;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Jerry
 * @since 20:16 2018/8/27
 */
public class PlaceholderTest {

    @Test
    public void parse() {
        Placeholder placeholder = Placeholder.parse("cookie.Coremail\\.sid");
        System.out.println(placeholder);

        Assert.assertEquals("cookie", placeholder.getName());
        Assert.assertEquals("Coremail\\.sid", placeholder.getSubname());

        placeholder = Placeholder.parse("cookie.Coremail");
        System.out.println(placeholder);

        Assert.assertEquals("cookie", placeholder.getName());
        Assert.assertEquals("Coremail", placeholder.getSubname());

        placeholder = Placeholder.parse("cookie.Coremail.sid");
        System.out.println(placeholder);

        Assert.assertEquals("cookie", placeholder.getName());
        Assert.assertEquals("Coremail.sid", placeholder.getSubname());

        placeholder = Placeholder.parse("cookie\\.Coremail.sid");
        System.out.println(placeholder);

        Assert.assertEquals("cookie.Coremail", placeholder.getName());
        Assert.assertEquals("sid", placeholder.getSubname());

        placeholder = Placeholder.parse("\\.Coremail.sid");
        System.out.println(placeholder);

        Assert.assertEquals(".Coremail", placeholder.getName());
        Assert.assertEquals("sid", placeholder.getSubname());

        placeholder = Placeholder.parse("Coremail.sid\\.");
        System.out.println(placeholder);

        Assert.assertEquals("Coremail", placeholder.getName());
        Assert.assertEquals("sid\\.", placeholder.getSubname());

        placeholder = Placeholder.parse("\\.Coremail.sid\\.");
        System.out.println(placeholder);

        Assert.assertEquals(".Coremail", placeholder.getName());
        Assert.assertEquals("sid\\.", placeholder.getSubname());

        placeholder = Placeholder.parse("Coremail\\.sid\\.");
        System.out.println(placeholder);

        Assert.assertEquals("Coremail.sid.", placeholder.getName());
        Assert.assertNull(placeholder.getSubname());

        placeholder = Placeholder.parse("Coremail\\.sid\\");
        System.out.println(placeholder);

        Assert.assertEquals("Coremail.sid\\", placeholder.getName());
        Assert.assertNull(placeholder.getSubname());

        placeholder = Placeholder.parse("\\.Coremail\\.sid\\");
        System.out.println(placeholder);

        Assert.assertEquals(".Coremail.sid\\", placeholder.getName());
        Assert.assertNull(placeholder.getSubname());

        placeholder = Placeholder.parse("\\.Coremail.sid\\");
        System.out.println(placeholder);

        Assert.assertEquals(".Coremail", placeholder.getName());
        Assert.assertEquals("sid\\", placeholder.getSubname());

        placeholder = Placeholder.parse("sid\\");
        System.out.println(placeholder);

        Assert.assertEquals("sid\\", placeholder.getName());
        Assert.assertNull(placeholder.getSubname());

        try {
            Placeholder.parse(".sid\\");
        } catch (Exception e) {
            Assert.assertTrue(e instanceof IllegalArgumentException);
            return;
        }

        Assert.fail();
    }
}