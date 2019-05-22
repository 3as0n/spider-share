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

package com.datatrees.spider.share.common.utils;

import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RLock;
import org.redisson.api.RQueue;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class WeightUtils {

    private static final Logger logger = LoggerFactory.getLogger(WeightUtils.class);

    /**
     * 队列redis前缀
     */
    private static final String QUEUE_PRE = "spider:queue:weight:";

    private RedissonClient client;

    private WeightQueueConfig queueConfig;

    public WeightUtils(String redisIP, String redisPassword, WeightQueueConfig queueConfig) {
        this.client = RedissonUtils.getRedisson(redisIP, "6379", redisPassword);
        this.queueConfig = queueConfig;
    }

    public List<String> createQueue(Map<String, Integer> map, int queueSize) {
        List<String> list = new ArrayList<>();
        int total = 0;
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            total += entry.getValue();
        }
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            String name = entry.getKey();
            Integer weight = entry.getValue();
            if (StringUtils.isBlank(name)) {
                throw new IllegalArgumentException("blank key");
            }
            if (null == weight || 0 > weight) {
                throw new IllegalArgumentException("invalid value");
            }
            if (weight == 0) {
                continue;
            }
            int count = weight * queueSize / total;
            for (int i = 0; i < count; i++) {
                list.add(name);
            }
        }
        Collections.shuffle(list);
        return list;
    }

    public String poll(String group) {
        String key = QUEUE_PRE + group;
        RQueue<Object> rQueue = client.getQueue(key);
        try {
            if (!rQueue.isExists() || rQueue.isEmpty()) {
                RLock lock = client.getLock("spider.locked." + key);
                boolean lockStatus = lock.tryLock(0, 3, TimeUnit.SECONDS);
                if (lockStatus) {
                    logger.info("lock weight queue success,wait create weight queue : {}", group);
                    List<String> queue = createQueue(queueConfig.getWeights(group), queueConfig.getQueueSize());
                    rQueue.addAll(queue);
                    lock.unlock();
                    logger.info("create weight queue success,group={}", group);
                } else {
                    logger.info("wait create weight queue,group={}", group);
                    TimeUnit.SECONDS.sleep(3);
                }
            }
            Object o = rQueue.poll();
            if (null == o) {
                return poll(group);
            }
            logger.info("poll from queue {} success,result={}", group, o);
            return o.toString();
        } catch (Exception e) {
            logger.error("poll weight queue error : group={}", group, e);
            return poll(group);
        }

    }

    public void clear(String group) {
        String key = QUEUE_PRE + group;
        RQueue<Object> rQueue = client.getQueue(key);
        if (!rQueue.isExists() || rQueue.isEmpty()) {
            return;
        }
        rQueue.clear();
        logger.warn("clear weight queue : {}", group);
    }

    /**
     * 队列配置
     * 
     * @author zhouxinghai
     * @date 2018/4/23
     */
    public interface WeightQueueConfig {

        /**
         * 获取权重
         * 
         * @return
         */
        Map<String, Integer> getWeights(String group);

        /**
         * 获取队列大小
         * 
         * @return
         */
        int getQueueSize();
    }
}
