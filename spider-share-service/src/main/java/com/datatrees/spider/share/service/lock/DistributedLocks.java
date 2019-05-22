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

package com.datatrees.spider.share.service.lock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * 分布式锁工厂
 * 
 * @author Jerry
 * @version 1.0.1
 * @since 1.0.1 [14:39, 11/3/15]
 */
@Component
public class DistributedLocks {

    private final RedisTemplate<String, Long> redisTemplate;

    @Autowired
    public DistributedLocks(RedisTemplate<String, Long> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public DistributedLock newLock(String key) {
        return new DistributedLock(redisTemplate, key);
    }

    /**
     * running <code>runnable</code> in lock state.
     * 
     * @param lockName the lock name
     * @param timeout the maximum time to wait for the lock
     * @param unit the unit used for timeout
     * @param runnable the function running in lock.
     * @exception InterruptedException
     * @exception LockingFailureException
     */
    public void doInLock(String lockName, long timeout, TimeUnit unit, Runnable runnable) throws InterruptedException, LockingFailureException {
        DistributedLock lock = newLock(lockName);
        if (!lock.tryLock(timeout, unit)) {
            throw new LockingFailureException("Can not acquire the lock. lockName: " + lockName);
        }

        try {
            runnable.run();
        } finally {
            lock.unlock();
        }
    }
}
