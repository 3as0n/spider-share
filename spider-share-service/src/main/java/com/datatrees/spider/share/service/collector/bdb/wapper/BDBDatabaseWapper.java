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

package com.datatrees.spider.share.service.collector.bdb.wapper;

import com.sleepycat.je.Database;
import com.sleepycat.je.SecondaryDatabase;

/**
 * @author <A HREF="">Cheng Wang</A>
 * @version 1.0
 * @since 2015年7月20日 上午12:46:33
 */
public class BDBDatabaseWapper {

    private String databaseName = null;

    private String secondaryName = null;

    private Database db = null;

    private SecondaryDatabase sdb = null;

    public BDBDatabaseWapper(Database db, SecondaryDatabase sdb) {
        super();
        this.db = db;
        this.sdb = sdb;
    }

    public Database getDb() {
        return db;
    }

    public void setDb(Database db) {
        this.db = db;
    }

    public SecondaryDatabase getSdb() {
        return sdb;
    }

    public void setSdb(SecondaryDatabase sdb) {
        this.sdb = sdb;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public String getSecondaryName() {
        return secondaryName;
    }

    public void setSecondaryName(String secondaryName) {
        this.secondaryName = secondaryName;
    }
}
