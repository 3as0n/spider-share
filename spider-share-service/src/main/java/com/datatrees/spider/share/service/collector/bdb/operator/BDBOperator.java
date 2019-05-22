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

package com.datatrees.spider.share.service.collector.bdb.operator;

import com.datatrees.spider.share.service.collector.bdb.manger.BDBFactory;
import com.datatrees.spider.share.service.collector.bdb.wapper.BDBWapper;
import com.datatrees.spider.share.service.collector.common.LinkNodeTupleBinding;
import com.sleepycat.je.Cursor;
import com.sleepycat.je.CursorConfig;
import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.OperationStatus;
import com.sleepycat.je.SecondaryCursor;
import com.sleepycat.je.SecondaryDatabase;
import com.treefinance.crawler.framework.context.function.LinkNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;

/**
 * @author <A HREF="">Cheng Wang</A>
 * @version 1.0
 * @since 2015年7月20日 上午12:42:45
 */
public class BDBOperator implements Operator {

    private static final Logger log = LoggerFactory.getLogger(BDBOperator.class);

    private static LinkNodeTupleBinding linkNodeBinding = new LinkNodeTupleBinding();

    private Database linkDB = null;

    private SecondaryDatabase slinkDB = null;

    private Cursor queueFront = null;

    private BDBWapper bdbWapper = null;

    private String databaseName = null;

    private long currentId = 1;

    private long lastFetchId = 0;

    public BDBOperator() throws Exception {
        bdbWapper = BDBFactory.INSTANCE.createDB();
        this.linkDB = bdbWapper.getDatabaseWapper().getDb();
        this.slinkDB = bdbWapper.getDatabaseWapper().getSdb();
        this.databaseName = bdbWapper.getDatabaseWapper().getDatabaseName();
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.datatrees.colander.taskworker.util.Operator#select(com.datatrees.vt.core.processer.bean
     * .LinkNode)
     */
    @Override
    public LinkedList<LinkNode> fetchNewLinks(int size) {
        DatabaseEntry key = new DatabaseEntry();
        DatabaseEntry value = new DatabaseEntry();
        LinkedList<LinkNode> newLinks = new LinkedList<LinkNode>();
        try {
            OperationStatus status = OperationStatus.SUCCESS;
            int num = 1;
            while (num <= size && lastFetchId < (currentId - 1)) {
                if (queueFront == null) {
                    queueFront = linkDB.openCursor(null, null);
                    status = queueFront.getFirst(key, value, null);
                } else {
                    status = queueFront.getNext(key, value, null);
                }

                if (status == OperationStatus.NOTFOUND) {
                    log.error("fetch one link from link queue failed, autoId = [" + currentId + "], lastFetchId = [" + lastFetchId + "]");
                    break;
                }

                LinkNode node = (LinkNode)linkNodeBinding.entryToObject(value);
                newLinks.add(node);
                lastFetchId++;
                num++;
            }
        } catch (Exception ex) {
            log.error("Catch exception when fetch new links from bdb ");
            log.error(ex.toString(), ex);
        }

        log.info("Fetch [" + newLinks.size() + "] link from link queue");
        return newLinks;
    }

    /**
     * @return the currentId
     */
    public long getCurrentId() {
        return currentId;
    }

    /**
     * @param currentId the currentId to set
     */
    public void setCurrentId(long currentId) {
        this.currentId = currentId;
    }

    /**
     * @return the lastFetchId
     */
    public long getLastFetchId() {
        return lastFetchId;
    }

    /**
     * @param lastFetchId the lastFetchId to set
     */
    public void setLastFetchId(long lastFetchId) {
        this.lastFetchId = lastFetchId;
    }

    public long getQueueSize() {
        long count = 0;
        try {
            count = linkDB.count();
        } catch (DatabaseException e) {
            log.error("getBdbSize error ", e);
        }
        return count;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.datatrees.colander.taskworker.util.Operator#select(com.datatrees.vt.core.processer.bean
     * .LinkNode)
     */
    @Override
    public int addLink(LinkNode link) {
        int newLinkNum = 0;
        log.debug("Add link:[" + link.getUrl() + "]");
        // primary key : currentId index
        byte[] keyArray = ("" + currentId).getBytes();
        DatabaseEntry key = new DatabaseEntry(keyArray);
        DatabaseEntry value = new DatabaseEntry();
        // secondary key : url#retryCount
        DatabaseEntry sKey = getSecondKey(link);
        DatabaseEntry foundValue = new DatabaseEntry();
        DatabaseEntry foundKey = new DatabaseEntry();
        SecondaryCursor secCursor = null;

        try {
            CursorConfig config = new CursorConfig();
            config.setReadUncommitted(true);

            log.debug("find skey.." + new String(sKey.getData()));

            secCursor = slinkDB.openSecondaryCursor(null, config);
            if (secCursor.getSearchKey(sKey, foundKey, foundValue, null) == OperationStatus.NOTFOUND) {
                // write to second db
                linkNodeBinding.objectToEntry(link, value);
                // write to db
                if (OperationStatus.SUCCESS != linkDB.put(null, key, value)) {
                    log.error("ERROR when put data to primary db  ");
                } else {
                    if (link.getRetryCount() > 0) {
                        log.info("add retry linknode success " + link);
                    }
                    newLinkNum++;
                    currentId = currentId + 1;
                    log.info("url add to linkQueue, url: " + link.getUrl() + " depth : " + link.getDepth() + ", currentId " + currentId + ", lastFetchedId : " + lastFetchId);
                }
            } else {
                log.debug("already exists in queue: {}", foundValue.getData());
            }
        } catch (Exception ex) {
            log.error("Catch exception when add link [" + link.getUrl() + "] to BDB ");
            log.error(ex.toString(), ex);
            if (slinkDB != null) {
                try {
                    // However, as a convenience, you can delete SecondaryDatabase records directly.
                    // Doing so causes the associated primary key/data pair to be deleted.
                    // This in turn causes JE to delete all SecondaryDatabase records that
                    // reference the primary record.
                    slinkDB.delete(null, sKey);
                } catch (DatabaseException e) {
                    log.error("slinkDB delete  sKey [" + sKey + "] failed ", e);
                }
            }

        } finally {
            if (secCursor != null) {
                try {
                    secCursor.close();
                } catch (DatabaseException e) {
                    log.error(e.toString(), e);
                }
                secCursor = null;
            }
        }
        return newLinkNum;
    }

    public DatabaseEntry getSecondKey(LinkNode link) {
        DatabaseEntry sKey = null;
        try {
            sKey = new DatabaseEntry((link.getUrl() + "#" + link.getRetryCount()).getBytes("UTF-8"));
        } catch (Exception ex) {
            log.error("Catch exception where set secondary search key for link db ");
            log.error(ex.toString(), ex);
        }
        return sKey;
    }

    public void closeDatabase() {
        if (null == bdbWapper) {
            log.error("bdbWapper cannot be  null !");
            return;
        }
        synchronized (bdbWapper.getEnvWapper()) {
            try {
                if (null != queueFront) {
                    try {
                        queueFront.close();
                    } catch (Exception e) {
                        log.error("queueFront close failed", e);
                    }
                }
                if (slinkDB != null) {
                    try {
                        slinkDB.close();
                    } catch (Exception e) {
                        log.error("slinkDB close failed", e);
                    }
                }
                if (linkDB != null) {
                    try {
                        linkDB.close();
                    } catch (Exception e) {
                        log.error("linkDB close failed", e);
                    }
                }
            } catch (Exception e) {
                log.error("closeDatabase error  " + e.getMessage());
            } finally {
                BDBFactory.INSTANCE.deleteDB(bdbWapper.getEnvWapper(), databaseName);
            }
        }
    }
}
