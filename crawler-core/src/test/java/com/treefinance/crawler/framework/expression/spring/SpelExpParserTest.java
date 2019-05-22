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

package com.treefinance.crawler.framework.expression.spring;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Jerry
 * @since 16:10 2018/6/8
 */
public class SpelExpParserTest {

    @Test
    public void parse() {
        boolean result = SpelExpParser.parse("40<40&&\"4300.00万元\"!=\"-\"", Boolean.TYPE);

        Assert.assertFalse(result);
    }
}