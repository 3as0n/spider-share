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

package com.datatrees.spider.share.service.submitter;

import com.datatrees.common.util.GsonUtils;
import com.datatrees.spider.share.common.share.service.RedisService;
import com.datatrees.spider.share.common.utils.BackRedisUtils;
import com.datatrees.spider.share.domain.RedisKeyPrefixEnum;
import com.datatrees.spider.share.service.domain.ExtractMessage;
import com.datatrees.spider.share.service.domain.SpiderTask;
import com.datatrees.spider.share.service.domain.SubSeed;
import com.datatrees.spider.share.service.domain.SubTask;
import com.datatrees.spider.share.service.domain.SubmitMessage;
import com.datatrees.spider.share.service.extra.SubTaskManager;
import com.datatrees.spider.share.service.normalizers.SubmitNormalizerFactory;
import com.datatrees.spider.share.service.util.RedisKeyUtils;
import com.treefinance.crawler.framework.process.domain.PageExtractObject;
import com.treefinance.crawler.framework.proxy.Proxy;
import org.apache.commons.lang.BooleanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import javax.annotation.Resource;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

@Component
public class DefaultSubmitProcessor implements SubmitProcessor {

    private static final Logger logger = LoggerFactory.getLogger(DefaultSubmitProcessor.class);

    @Resource
    private RedisService redisService;

    @Resource
    private SubmitNormalizerFactory submitNormalizerFactory;

    @Resource
    private SubTaskManager subTaskManager;

    @Override
    public boolean process(@Nonnull SubmitMessage submitMessage) {
        logger.info("start submit task processing: {}", submitMessage);
        try {
            PageExtractObject pageExtractObject = submitMessage.getPageExtractObject();
            if (pageExtractObject == null || pageExtractObject.isEmpty()) {
                logger.warn("Empty extracted page object and skip redis. task: {}", submitMessage);
                return true;
            }

            submitNormalizerFactory.normalize(submitMessage);

            ExtractMessage extractMessage = submitMessage.getExtractMessage();
            for (Entry<String, Object> entry : pageExtractObject.entrySet()) {
                String key = entry.getKey();
                if ("subSeed".equals(key)) {
                    startSubTask(extractMessage, entry);
                    continue;
                }

                String redisKey = saveExtractedObject(extractMessage, entry);

                logger.info("add storage path: {}, key: {}", redisKey, key);
                submitMessage.addSubmitKey(key + "Key", redisKey);
            }

            return true;
        } catch (Exception e) {
            logger.error("unknown exception!", e);
            return false;
        }
    }

    private String saveExtractedObject(ExtractMessage extractMessage, Entry<String, Object> entry) {
        Long taskId = extractMessage.getTaskId();

        String redisKey = RedisKeyUtils.genRedisKey(taskId, extractMessage.getProcessId(), entry.getKey());

        doMonitor(taskId, redisKey, entry, value -> {
            if (value instanceof Collection) {
                List<String> jsonStringList = new ArrayList<String>();
                for (Object obj : (Collection)value) {
                    if (obj != null) {
                        jsonStringList.add(GsonUtils.toJson(obj));
                    }
                }

                redisService.saveToList(redisKey, jsonStringList, 30, TimeUnit.MINUTES);

                return jsonStringList;
            } else if (value != null) {
                String result = GsonUtils.toJson(value);
                redisService.saveString(redisKey, result, 30, TimeUnit.MINUTES);
                return result;
            }

            return null;
        });

        return redisKey;
    }

    private void doMonitor(long taskId, String redisKey, Entry<String, Object> entry, @Nonnull Function<Object, Object> function) {
        String backKey = "monitor.back." + redisKey;
        try {
            BackRedisUtils.hset(RedisKeyPrefixEnum.TASK_RESULT.getRedisKey(taskId), entry.getKey(), backKey, RedisKeyPrefixEnum.TASK_RESULT.toSeconds());
        } catch (Throwable e) {
            logger.warn("save to back redis error ", e);
        }

        Object result = function.apply(entry.getValue());
        if (result instanceof List) {
            List<String> jsonStringList = (List<String>)result;
            try {
                if (!jsonStringList.isEmpty()) {
                    BackRedisUtils.rpush(backKey, jsonStringList.toArray(new String[0]));
                }
            } catch (Throwable e) {
                logger.warn("save to back redis error ", e);
            }
        } else if (result instanceof String) {
            try {
                BackRedisUtils.set(backKey, (String)result);
            } catch (Throwable e) {
                logger.warn("save to back redis error ", e);
            }
        }

        try {
            BackRedisUtils.expire(backKey, RedisKeyPrefixEnum.TASK_RESULT.toSeconds());
        } catch (Throwable e) {
            logger.warn("save to back redis error ", e);
        }
    }

    private void startSubTask(ExtractMessage extractMessage, Entry<String, Object> entry) {
        SpiderTask task = extractMessage.getTask();

        if (entry.getValue() instanceof Collection) {
            for (Object obj : (Collection)entry.getValue()) {
                this.doSubTask(task, obj);
            }
        } else {
            this.doSubTask(task, entry.getValue());
        }
    }

    private void doSubTask(SpiderTask task, Object seed) {
        if (seed instanceof Map) {
            SubSeed subSeed = new SubSeed((Map)seed);

            logger.info("submit sub-seed {}", subSeed);
            subTaskManager.submitSubTask(new SubTask(task, subSeed));
            if (BooleanUtils.isTrue(subSeed.getProxyShared())) {
                try {
                    Proxy parentProxy = task.getProcessorContext().getProxy();
                    if (parentProxy != null) {
                        subSeed.setProxy(parentProxy);
                        parentProxy.getShareCount().incrementAndGet();
                    }
                } catch (Exception e) {
                    logger.error("proxy Shared error " + e.getMessage(), e);
                }
            }
        }
    }

}
