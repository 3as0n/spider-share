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

package com.treefinance.crawler.framework.extension.spider.page;

import java.util.Map;

/**
 * @author Jerry
 * @since 11:54 15/01/2018
 */
public class SimplePage extends Page {

    private final String resultType;

    public SimplePage(String url, String content, String resultType) {
        super(url, content);
        this.resultType = resultType;
    }

    public SimplePage(String url, String content, Map<String, Object> extra, String resultType) {
        super(url, content, extra);
        this.resultType = resultType;
    }

    public String getResultType() {
        return resultType;
    }

}
