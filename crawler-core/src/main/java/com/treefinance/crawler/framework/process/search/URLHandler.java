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

package com.treefinance.crawler.framework.process.search;

import com.treefinance.crawler.framework.context.function.LinkNode;

/**
 * link node handler usage: collect url , extract host url from current url
 * 
 * @author <A HREF="">Cheng Wang</A>
 * @version 1.0
 * @since Mar 17, 2014 10:34:27 AM
 */
public interface URLHandler {

    /**
     * handle url from current request
     * 
     * @param current current request url , contains meta info like page title , imdb etc
     * @param fetched current fetched url
     */
    boolean handle(LinkNode current, LinkNode fetched);

}
