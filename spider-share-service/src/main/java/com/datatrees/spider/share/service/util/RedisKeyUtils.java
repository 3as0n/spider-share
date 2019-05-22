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

package com.datatrees.spider.share.service.util;

import com.datatrees.common.conf.PropertiesConfiguration;
import com.datatrees.spider.share.domain.CollectorMessage;
import com.datatrees.spider.share.service.constants.SubmitConstant;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Deprecated
public class RedisKeyUtils {

    private static final Logger log = LoggerFactory.getLogger(RedisKeyUtils.class);

    private static String REDIS_PREFIX = PropertiesConfiguration.getInstance().get("core.redis.redis.prefix", "raw_res_");

    public static String genRedisKey(long taskId, long taskLogId, String resultClass) {
        if (StringUtils.isEmpty(resultClass)) {
            return null;
        }
        StringBuilder sb = new StringBuilder(resultClass).append(".").append(taskLogId).append(".").append(taskId);
        String redisKey = sb.toString();
        log.debug("generate redis key " + redisKey);
        return redisKey;
    }

    public static String genCollectorMessageRedisKey(CollectorMessage collectorMessage) {
        if (collectorMessage == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(REDIS_PREFIX).append(collectorMessage.getTaskId()).append(SubmitConstant.REDIS_KEY_SEPARATOR).append(collectorMessage.getMsgId());
        String redisKey = sb.toString();
        return redisKey;
    }
}
