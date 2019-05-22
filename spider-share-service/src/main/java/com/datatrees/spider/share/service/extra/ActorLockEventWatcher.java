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

package com.datatrees.spider.share.service.extra;

import com.datatrees.common.util.ThreadInterruptedUtil;
import com.datatrees.common.zookeeper.ZooKeeperClient;
import com.datatrees.common.zookeeper.watcher.AbstractLockerWatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author <A HREF="">Cheng Wang</A>
 * @version 1.0
 * @since 2015年9月23日 下午6:32:50
 */
public class ActorLockEventWatcher extends AbstractLockerWatcher {

    private static final Logger logger = LoggerFactory.getLogger(ActorLockEventWatcher.class);

    private Thread actorThread;

    private ZooKeeperClient zookeeperClient;

    private String root;

    /**
     * @param name
     */
    public ActorLockEventWatcher(String root, String name, Thread thread, ZooKeeperClient zookeeperClient) {
        super(root + "/" + name);
        this.root = root;
        actorThread = thread;
        this.zookeeperClient = zookeeperClient;
    }

    @Override
    public void await() {}

    public boolean unLock() {
        return releaseLeader();
    }

    public boolean cancel() {
        zookeeperClient.registerWatcher(this);
        if (this.init()) {
            zookeeperClient.unregisterWatcher(this);
            return true;
        }
        return false;
    }

    @Override
    protected void detectLeader() {
        try {
            String leader = super.getLastFollower();
            logger.debug("leader is path : " + leader);
            if (!childPath.equals(leader)) {
                logger.info(childPath + " lose lock from zookeeper with lastFollower: " + leader);
                if (actorThread != null && actorThread.isAlive()) {
                    logger.info(" interrupt thread={},threadId={},childPath={},root={}", actorThread, actorThread.getId(), childPath, root);
                    // actorThread.interrupt();
                    ThreadInterruptedUtil.setInterrupt(actorThread);
                    zookeeperClient.unregisterWatcher(this);
                }
            }
        } catch (Exception e) {
            logger.error("detectLeader error", e);
        }
    }

    @Override
    protected boolean releaseLeader() {
        try {
            String leader = super.getLastFollower();
            logger.debug("leader is path : " + leader);
            if (childPath.equals(leader)) {
                logger.info(childPath + " try to release lock from zookeeper delete path:" + path);
                this.deletePath(root, path, -1);
                return true;
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return false;
    }

}
