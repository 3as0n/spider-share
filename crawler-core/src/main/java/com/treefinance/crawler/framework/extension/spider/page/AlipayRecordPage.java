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
 * @since 16:56 28/12/2017
 */
public class AlipayRecordPage extends SimplePage {

    private boolean success;

    // 判断是是否是最后一页标识
    private boolean end;

    public AlipayRecordPage(String url, String content, Map<String, Object> extra, String resultType, boolean success, boolean end) {
        super(url, content, extra, resultType);
        this.success = success;
        this.end = end;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public boolean isEnd() {
        return end;
    }

    public void setEnd(boolean end) {
        this.end = end;
    }

}
