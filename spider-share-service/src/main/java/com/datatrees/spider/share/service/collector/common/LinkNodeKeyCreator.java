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

package com.datatrees.spider.share.service.collector.common;

import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.SecondaryDatabase;
import com.sleepycat.je.SecondaryKeyCreator;
import com.treefinance.crawler.framework.context.function.LinkNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author <A HREF="">Cheng Wang</A>
 * @version 1.0
 * @since 2015年7月20日 上午12:55:21
 */
public class LinkNodeKeyCreator implements SecondaryKeyCreator {

    private static final Logger log = LoggerFactory.getLogger(LinkNodeKeyCreator.class);

    /*
     * (non-Javadoc)
     *
     * @see
     * com.sleepycat.je.SecondaryMultiKeyCreator#createSecondaryKeys(com.sleepycat.je.SecondaryDatabase
     * , com.sleepycat.je.DatabaseEntry, com.sleepycat.je.DatabaseEntry, java.util.Set)
     */
    @Override
    public boolean createSecondaryKey(SecondaryDatabase secondaryDb, DatabaseEntry key, DatabaseEntry data, DatabaseEntry result) {
        LinkNodeTupleBinding linkNodeBinding = new LinkNodeTupleBinding();
        LinkNode linkNode = (LinkNode)linkNodeBinding.entryToObject(data);
        try {

            String keys = (linkNode.getUrl() + "#" + linkNode.getRetryCount());
            result.setData(keys.getBytes("UTF-8"));
        } catch (Exception ex) {
            log.error("Error occurs when set key data");
            log.error(ex.toString(), ex);
        }
        return true;
    }

}
