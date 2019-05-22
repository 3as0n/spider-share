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

package com.datatrees.spider.share.service.util.operator;

import com.alibaba.fastjson.JSON;
import com.datatrees.common.conf.PropertiesConfiguration;
import com.datatrees.spider.share.common.utils.RedisUtils;
import com.datatrees.spider.share.common.utils.WebsiteUtils;
import com.datatrees.spider.share.domain.AttributeKey;
import com.datatrees.spider.share.domain.ErrorCode;
import com.datatrees.spider.share.domain.RedisKeyPrefixEnum;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * 运营商辅助工具 Created by guimeichao on 18/2/8.
 */
public class OperatorUtils {

    private static final Logger logger = LoggerFactory.getLogger(OperatorUtils.class);

    private static final String OPERATOR_FAIL_USER_MAX = "operator.fail.usercount.max";

    public static String getRemarkForTaskFail(long taskId) {
        try {
            String redisKey = RedisKeyPrefixEnum.TASK_INIT_NICK_GROUP_CODE.getRedisKey(taskId);
            if (RedisUtils.exists(redisKey)) {
                String nickGroupCode = RedisUtils.get(redisKey);
                String property = PropertiesConfiguration.getInstance().get(OPERATOR_FAIL_USER_MAX);
                int maxFailUser = 5;
                if (StringUtils.isNotBlank(property)) {
                    maxFailUser = Integer.parseInt(property);
                }
                boolean b = WebsiteUtils.isNormal(nickGroupCode, maxFailUser);
                if (!b) {
                    Map<String, String> map = new HashMap<>();
                    map.put(AttributeKey.ERROR_MSG, ErrorCode.UNDER_MAINTENANCE.getErrorMsg());
                    return JSON.toJSONString(map);
                }
                return null;
            }
            return null;
        } catch (Exception e) {
            logger.error("任务失败时获取remark失败,taskId={}", taskId, e);
            return null;
        }
    }
}
