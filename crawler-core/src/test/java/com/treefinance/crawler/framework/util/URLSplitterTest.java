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

package com.treefinance.crawler.framework.util;

import org.junit.Test;

import java.util.Collection;

/**
 * @author Jerry
 * @since 11:37 2018/8/15
 */
public class URLSplitterTest {

    @Test
    public void split() {
        Collection<String> collection = URLSplitter.split("http://www.baidu.com");
        collection.forEach(System.out::println);

        collection = URLSplitter.split("http://www.baidu.comhttps://www.taobao.com https://www.alipay.com");
        collection.forEach(System.out::println);
    }
}