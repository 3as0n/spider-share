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

package com.datatrees.spider.share.common.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.datatrees.spider.share.domain.ProcessResult;
import com.datatrees.spider.share.domain.RedisKeyPrefixEnum;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class ProcessResultUtils {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(ProcessResultUtils.class);

    private static final String KEY_PROCESS_ID = "process.id";

    public static Long createProcessId() {
        return RedisUtils.incr(KEY_PROCESS_ID);
    }

    public static <T> ProcessResult<T> createAndSaveProcessId() {
        ProcessResult result = new ProcessResult();
        result.processing(createProcessId());
        saveProcessResult(result);
        return result;
    }

    public static <T> ProcessResult<T> queryProcessResult(long processId) {
        return JSON.parseObject(RedisUtils.get(getRedisKey(processId)), new TypeReference<ProcessResult<T>>() {});
    }

    public static void saveProcessResult(ProcessResult result) {
        RedisUtils.set(getRedisKey(result.getProcessId()), JSON.toJSONString(result), (int)TimeUnit.HOURS.toSeconds(1));
    }

    public static String getRedisKey(Long processId) {
        return new StringBuilder("process.result.").append(processId).toString();
    }

    public static void setProcessExpire(long taskId, Long processId, long timeout, TimeUnit unit) {
        String redisKey = RedisKeyPrefixEnum.PROCESS_EXPIRE.getRedisKey(processId);
        long end = System.currentTimeMillis() + unit.toMillis(timeout);
        RedisUtils.setnx(redisKey, String.valueOf(end), (int)unit.toSeconds(timeout));
        logger.info("set process expire time,taskId={},processId={},end={}", taskId, processId, DateUtils.formatYmdhms(end));
    }

    public static boolean processExpire(long taskId, Long processId) {
        String redisKey = RedisKeyPrefixEnum.PROCESS_EXPIRE.getRedisKey(processId);
        String endStr = RedisUtils.get(redisKey);
        boolean expire = StringUtils.isBlank(endStr) || System.currentTimeMillis() > Long.parseLong(endStr);
        if (expire) {
            logger.warn("processId is timeout,taskId={},processId={},endStr={}", taskId, processId,
                StringUtils.isNotBlank(endStr) ? DateUtils.formatYmdhms(Long.parseLong(endStr)) : endStr);
        }
        return expire;
    }

}
