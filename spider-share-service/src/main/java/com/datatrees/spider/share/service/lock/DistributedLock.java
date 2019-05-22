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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * 分布式锁
 * 
 * @author Jerry
 * @version 1.0.1
 * @since 1.0.1 [23:51, 11/2/15]
 */
public class DistributedLock {

    private static final Logger LOGGER = LoggerFactory.getLogger(DistributedLock.class);

    private static final ConcurrentHashMap<String, Thread> HOLDER = new ConcurrentHashMap<>();

    private final RedisTemplate<String, Long> redisTemplate;

    private final String lockName;

    private long leaseTime = 3 * 60 * 1000;

    DistributedLock(RedisTemplate<String, Long> redisTemplate, String key) {
        this.redisTemplate = redisTemplate;
        this.lockName = getLockName(key);
    }

    public static void main(String[] args) {
        System.out.println(Thread.currentThread() == null);
    }

    public void setLeaseTime(long leaseTime) {
        this.leaseTime = leaseTime;
    }

    public boolean tryLock(long timeout, TimeUnit unit) throws InterruptedException {
        return tryLock(leaseTime, unit.toMillis(timeout));
    }

    public boolean tryLock(long leaseTime, TimeUnit leaseTimeUnit, long timeout, TimeUnit unit) throws InterruptedException {
        return tryLock(leaseTimeUnit.toMillis(leaseTime), unit.toMillis(timeout));
    }

    public void unlock() {
        LOGGER.info("unlock within the name: {}", lockName);
        if (Thread.currentThread() == removeOwnerThread()) {
            try {
                redisTemplate.delete(lockName);
            } catch (Exception e) {
                LOGGER.warn("error unlock with the name: {}", lockName, e);
            }
        } else {
            LOGGER.warn("The current thread is not the owner of lock. lock-name: {}", lockName);
        }
    }

    private String getLockName(String name) {
        return "custom_distribution_lock:{" + name + "}";
    }

    private Thread getOwnerThread() {
        return HOLDER.get(this.lockName);
    }

    private void setOwnerThread(Thread ownerThread) {
        HOLDER.put(this.lockName, ownerThread);
    }

    private Thread removeOwnerThread() {
        return HOLDER.remove(this.lockName);
    }

    /**
     * Acquires the lock if it is free within the given key and the current thread has not been
     * {@linkplain Thread#interrupt interrupted}.
     *
     * @param leaseTime the lease time of lock
     * @param timeout the maximum time to wait for the lock
     * @return true if acquiring the lock.
     * @exception InterruptedException
     */
    private boolean tryLock(long leaseTime, long timeout) throws InterruptedException {
        if (leaseTime <= 0) {
            throw new IllegalArgumentException("Parameter 'leaseTime' must be positive.");
        }

        Thread currentThread = Thread.currentThread();
        // 判断当前线程是否已拿到锁
        if (getOwnerThread() == currentThread) {
            return true;
        }

        if (doLock(leaseTime, timeout)) {
            // 绑定锁的持有者
            setOwnerThread(currentThread);
            return true;
        }

        return false;
    }

    private boolean doLock(long leaseTime, long timeout) throws InterruptedException {
        long deadLine = System.currentTimeMillis() + Math.max(0, timeout);
        do {
            if (redisTemplate.opsForValue().setIfAbsent(lockName, System.currentTimeMillis() + leaseTime)) {
                // 成功拿到锁
                return true;
            }

            // 已被其他线程锁住
            // 通过GET命令获取锁并判断该锁是否已过期
            Long lockTm = redisTemplate.opsForValue().get(lockName);
            if (lockTm == null) {
                if (System.currentTimeMillis() >= deadLine) {
                    // 未拿到锁
                    break;
                }
                continue;
            }

            // lock已过期
            if (lockTm <= System.currentTimeMillis()) {
                // 通过GETSET命令重新获取一次，对比之前获取的时间，判断是否一致
                // 注意：可能重置了另一个锁持有者的超时时间，导致超时误差，不过误差不大，影响忽略
                Long newLockTm = redisTemplate.opsForValue().getAndSet(lockName, System.currentTimeMillis() + leaseTime);
                if (newLockTm == null || newLockTm <= lockTm) {
                    // 成功拿到锁
                    return true;
                }

                lockTm = newLockTm;
            }

            if (lockTm >= deadLine || System.currentTimeMillis() >= deadLine) {
                // 未拿到锁
                break;
            }

            long sleep = lockTm - System.currentTimeMillis();
            if (sleep > 0) {
                Thread.sleep(sleep);
            }
        } while (true);

        return false;
    }

}
