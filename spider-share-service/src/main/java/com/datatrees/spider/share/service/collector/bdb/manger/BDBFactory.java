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

package com.datatrees.spider.share.service.collector.bdb.manger;

import com.datatrees.spider.share.service.collector.bdb.env.Environment;
import com.datatrees.spider.share.service.collector.bdb.wapper.BDBEnvironmentWapper;
import com.datatrees.spider.share.service.collector.bdb.wapper.BDBWapper;

/**
 * @author <A HREF="">Cheng Wang</A>
 * @version 1.0
 * @since 2015年7月20日 上午12:43:39
 */
public enum BDBFactory implements Environment {
    INSTANCE;

    private BDBEnvironmentManager manager = BDBEnvironmentManager.getInstance();

    @Override
    public synchronized BDBWapper createDB() throws Exception {
        BDBEnvironmentWapper bdbEnvironmentWapper = manager.takeEnv();
        BDBWapper wapper = bdbEnvironmentWapper.createDB();
        return wapper;
    }

    public void deleteDB(BDBEnvironmentWapper bdbEnvironmentWapper, String databaseName) {
        bdbEnvironmentWapper.deleteDB(databaseName);
        manager.checkWapperDestory(bdbEnvironmentWapper);
    }

    @Override
    @Deprecated
    public void deleteDB(String databaseName) {}
}
