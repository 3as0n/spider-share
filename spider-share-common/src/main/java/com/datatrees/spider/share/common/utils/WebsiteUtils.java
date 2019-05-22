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

import com.datatrees.spider.share.domain.AttributeKey;
import com.datatrees.spider.share.domain.RedisKeyPrefixEnum;
import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * website工具类
 */
public class WebsiteUtils {

    private static final Logger logger = LoggerFactory.getLogger(WebsiteUtils.class);

    private static Map<String, Set<String>> NICK_GROU_WEBSITE = new ConcurrentHashMap<>();

    /**
     * 是否是运营商
     * 
     * @param websiteName
     * @return
     */
    public static boolean isOperator(String websiteName) {
        return StringUtils.containsAny(websiteName, "10086", "10000", "10010");
    }

    /**
     * 是否正常： 若分组的各网站存在连续失败人数小于最大连续失败人数的，即为正常
     * 
     * @param nickGroupCode
     * @param maxFailUser
     * @return
     */
    public static boolean isNormal(String nickGroupCode, Integer maxFailUser) {
        Set<String> keySet = getWebsitesByNickGroupCode(nickGroupCode);
        if (keySet.isEmpty()) {
            logger.warn("nickGroupCode not found,nickGroupCode={}", nickGroupCode);
            return true;
        } else {
            for (String websiteName : keySet) {
                Map<String, String> result = RedisUtils.hgetAll(getRedisKeyForWebsiteGroupLastInfo(websiteName, nickGroupCode));
                String failUserCount = result.get(AttributeKey.FAIL_USER_COUNT);
                if (Integer.parseInt(failUserCount) <= maxFailUser) {
                    logger.info("nickGroupCode is normal,nickGroupCode={}", nickGroupCode);
                    return true;
                }
            }
            logger.warn("all website failUserCount more than 5,nickGroupCode is not normal,nickGroupCode={}", nickGroupCode);
            return false;
        }
    }

    /**
     * 是否稳定： 若分组的各网站存在连续失败人数小于最大连续失败人数的，且最后成功时间在30分钟以前的，即为稳定
     * 
     * @param nickGroupCode
     * @param maxFailUser
     * @return
     */
    public static boolean isSteadied(String nickGroupCode, Integer maxFailUser) {
        Set<String> keySet = getWebsitesByNickGroupCode(nickGroupCode);
        if (keySet.isEmpty()) {
            logger.warn("nickGroupCode not found,nickGroupCode={}", nickGroupCode);
            return true;
        } else {
            int duration = 30;
            for (String websiteName : keySet) {
                Map<String, String> result = RedisUtils.hgetAll(WebsiteUtils.getRedisKeyForWebsiteGroupLastInfo(websiteName, nickGroupCode));
                String failUserCount = result.get(AttributeKey.FAIL_USER_COUNT);
                if (Integer.parseInt(failUserCount) <= maxFailUser) {
                    String successTimestamp = result.get(AttributeKey.SUCCESS_TIMESTAMP);
                    if (StringUtils.isBlank(successTimestamp)) {
                        successTimestamp = "0";
                    }
                    long now = System.currentTimeMillis();
                    boolean b = (now - Long.parseLong(successTimestamp)) >= TimeUnit.MINUTES.toMillis(duration);
                    if (b) {
                        logger.info("nickGroupCode is normal,nickGroupCode={}", nickGroupCode);
                        return true;
                    }
                    logger.warn("nickGroupCode is normal not more than 30 minutes,nickGroupCode={}", nickGroupCode);
                }
            }
            logger.warn("all website failUserCount more than 5,nickGroupCode is not normal,nickGroupCode={}", nickGroupCode);
            return false;
        }
    }

    public static void updateWithTaskSuccess(@Nonnull String websiteName, long taskId, long timestamp) {
        Preconditions.checkNotNull(websiteName);
        String redisKey = getRedisKeyForWebsiteLastInfo(websiteName);
        String redisTimestamp = RedisUtils.hget(redisKey, AttributeKey.SUCCESS_TIMESTAMP);
        boolean update = StringUtils.isBlank(redisTimestamp) || timestamp > Long.parseLong(redisTimestamp);
        if (update) {
            RedisUtils.hset(redisKey, AttributeKey.SUCCESS_TIMESTAMP, String.valueOf(timestamp));
            RedisUtils.hset(redisKey, AttributeKey.SUCCESS_TASK_ID, String.valueOf(taskId));
            long successUserCount = RedisUtils.hincrBy(redisKey, AttributeKey.SUCCESS_USER_COUNT, 1);
            RedisUtils.hset(redisKey, AttributeKey.FAIL_USER_COUNT, "0");
            updateWebsiteDayList(websiteName, taskId, timestamp);
            logger.info("upate website last info with task success,websiteName={},taskId={},timestamp={},successUserCount={}", websiteName, taskId,
                DateUtils.formatYmdhms(timestamp), successUserCount);
        }
    }

    public static void updateNickGroupWithTaskSuccess(@Nonnull String nickGroupCode, long taskId, long timestamp) {
        Preconditions.checkNotNull(nickGroupCode);
        String redisKey = getRedisKeyForNickGroupLastInfo(nickGroupCode);
        String redisTimestamp = RedisUtils.hget(redisKey, AttributeKey.SUCCESS_TIMESTAMP);
        boolean update = StringUtils.isBlank(redisTimestamp) || timestamp > Long.parseLong(redisTimestamp);
        if (update) {
            RedisUtils.hset(redisKey, AttributeKey.SUCCESS_TIMESTAMP, String.valueOf(timestamp));
            RedisUtils.hset(redisKey, AttributeKey.SUCCESS_TASK_ID, String.valueOf(taskId));
            long successUserCount = RedisUtils.hincrBy(redisKey, AttributeKey.SUCCESS_USER_COUNT, 1);
            RedisUtils.hset(redisKey, AttributeKey.FAIL_USER_COUNT, "0");
            updateNickGroupDayList(nickGroupCode, taskId, timestamp);
            logger.info("upate nickGroup last info with task success,nickGroupCode={},taskId={},timestamp={},successUserCount={}", nickGroupCode, taskId,
                DateUtils.formatYmdhms(timestamp), successUserCount);
        }
    }

    public static void updateWebsiteGroupWithTaskSuccess(@Nonnull String websiteName, @Nonnull String nickGroupCode, long taskId, long timestamp) {
        Preconditions.checkNotNull(websiteName);
        Preconditions.checkNotNull(nickGroupCode);
        String redisKey = getRedisKeyForWebsiteGroupLastInfo(websiteName, nickGroupCode);
        String redisTimestamp = RedisUtils.hget(redisKey, AttributeKey.SUCCESS_TIMESTAMP);
        boolean update = StringUtils.isBlank(redisTimestamp) || timestamp > Long.parseLong(redisTimestamp);
        if (update) {
            RedisUtils.hset(redisKey, AttributeKey.SUCCESS_TIMESTAMP, String.valueOf(timestamp));
            RedisUtils.hset(redisKey, AttributeKey.SUCCESS_TASK_ID, String.valueOf(taskId));
            long successUserCount = RedisUtils.hincrBy(redisKey, AttributeKey.SUCCESS_USER_COUNT, 1);
            RedisUtils.hset(redisKey, AttributeKey.FAIL_USER_COUNT, "0");
            updateWebsiteGroupDayList(websiteName, nickGroupCode, taskId, timestamp);
            logger.info("upate website group last info with task success,websiteName={},nickGroupCode={},taskId={},timestamp={},successUserCount={}", websiteName, nickGroupCode,
                taskId, DateUtils.formatYmdhms(timestamp), successUserCount);
        }
    }

    public static void updateWithTaskFail(@Nonnull String websiteName, long taskId, long timestamp) {
        Preconditions.checkNotNull(websiteName);
        String redisKey = getRedisKeyForWebsiteLastInfo(websiteName);
        String redisTimestamp = RedisUtils.hget(redisKey, AttributeKey.FAIL_TIMESTAMP);
        boolean update = StringUtils.isBlank(redisTimestamp) || timestamp > Long.parseLong(redisTimestamp);
        if (update) {
            RedisUtils.hset(redisKey, AttributeKey.FAIL_TIMESTAMP, String.valueOf(timestamp));
            RedisUtils.hset(redisKey, AttributeKey.FAIL_TASK_ID, String.valueOf(taskId));
            long failUserCount = RedisUtils.hincrBy(redisKey, AttributeKey.FAIL_USER_COUNT, 1);
            RedisUtils.hset(redisKey, AttributeKey.SUCCESS_USER_COUNT, "0");
            updateWebsiteDayList(websiteName, taskId, timestamp);
            logger.info("upate website last info with task fail,websiteName={},taskId={},timestamp={},failUserCount={}", websiteName, taskId, DateUtils.formatYmdhms(timestamp),
                failUserCount);
        }
    }

    public static void updateNickGroupWithTaskFail(@Nonnull String nickGroupCode, long taskId, long timestamp) {
        Preconditions.checkNotNull(nickGroupCode);
        String redisKey = getRedisKeyForNickGroupLastInfo(nickGroupCode);
        String redisTimestamp = RedisUtils.hget(redisKey, AttributeKey.FAIL_TIMESTAMP);
        boolean update = StringUtils.isBlank(redisTimestamp) || timestamp > Long.parseLong(redisTimestamp);
        if (update) {
            RedisUtils.hset(redisKey, AttributeKey.FAIL_TIMESTAMP, String.valueOf(timestamp));
            RedisUtils.hset(redisKey, AttributeKey.FAIL_TASK_ID, String.valueOf(taskId));
            long failUserCount = RedisUtils.hincrBy(redisKey, AttributeKey.FAIL_USER_COUNT, 1);
            RedisUtils.hset(redisKey, AttributeKey.SUCCESS_USER_COUNT, "0");
            updateNickGroupDayList(nickGroupCode, taskId, timestamp);
            logger.info("upate nickGroup last info with task fail,nickGroupCode={},taskId={},timestamp={},failUserCount={}", nickGroupCode, taskId,
                DateUtils.formatYmdhms(timestamp), failUserCount);
        }
    }

    public static void updateWebsiteGroupWithTaskFail(@Nonnull String websiteName, @Nonnull String nickGroupCode, long taskId, long timestamp) {
        Preconditions.checkNotNull(websiteName);
        Preconditions.checkNotNull(nickGroupCode);
        String redisKey = getRedisKeyForWebsiteGroupLastInfo(websiteName, nickGroupCode);
        String redisTimestamp = RedisUtils.hget(redisKey, AttributeKey.FAIL_TIMESTAMP);
        boolean update = StringUtils.isBlank(redisTimestamp) || timestamp > Long.parseLong(redisTimestamp);
        if (update) {
            RedisUtils.hset(redisKey, AttributeKey.FAIL_TIMESTAMP, String.valueOf(timestamp));
            RedisUtils.hset(redisKey, AttributeKey.FAIL_TASK_ID, String.valueOf(taskId));
            long failUserCount = RedisUtils.hincrBy(redisKey, AttributeKey.FAIL_USER_COUNT, 1);
            RedisUtils.hset(redisKey, AttributeKey.SUCCESS_USER_COUNT, "0");
            updateWebsiteGroupDayList(websiteName, nickGroupCode, taskId, timestamp);
            logger.info("upate website group last info with task fail,websiteName={},taskId={},timestamp={},failUserCount={}", websiteName, taskId,
                DateUtils.formatYmdhms(timestamp), failUserCount);
        }
    }

    public static void updateWarnTime(@Nonnull String websiteName, long timestamp) {
        Preconditions.checkNotNull(websiteName);
        String redisKey = getRedisKeyForWebsiteLastInfo(websiteName);
        String redisTimestamp = RedisUtils.hget(redisKey, AttributeKey.WARN_TIMESTAMP);
        boolean update = StringUtils.isBlank(redisTimestamp) || timestamp > Long.parseLong(redisTimestamp);
        if (update) {
            RedisUtils.hset(redisKey, AttributeKey.WARN_TIMESTAMP, String.valueOf(timestamp));
            logger.info("upate website last info with warnTimestamp,websiteName={},taskId={},timestamp={}", websiteName, DateUtils.formatYmdhms(timestamp));
        }
    }

    public static long getWarnTimestamp(@Nonnull String websiteName) {
        Preconditions.checkNotNull(websiteName);
        String redisKey = getRedisKeyForWebsiteLastInfo(websiteName);
        String value = RedisUtils.hget(redisKey, AttributeKey.WARN_TIMESTAMP);
        if (StringUtils.isBlank(value)) {
            return 0L;
        }
        return Long.parseLong(value);
    }

    public static int getSuccessUserCount(@Nonnull String websiteName) {
        Preconditions.checkNotNull(websiteName);
        String redisKey = getRedisKeyForWebsiteLastInfo(websiteName);
        String value = RedisUtils.hget(redisKey, AttributeKey.SUCCESS_USER_COUNT);
        if (StringUtils.isBlank(value)) {
            return 0;
        }
        return Integer.parseInt(value);
    }

    public static int getSuccessUserCountForNickGroup(@Nonnull String nickGroupCode) {
        Preconditions.checkNotNull(nickGroupCode);
        String redisKey = getRedisKeyForNickGroupLastInfo(nickGroupCode);
        String value = RedisUtils.hget(redisKey, AttributeKey.SUCCESS_USER_COUNT);
        if (StringUtils.isBlank(value)) {
            return 0;
        }
        return Integer.parseInt(value);
    }

    public static int getSuccessUserCountForWebsiteGroup(@Nonnull String websiteName, @Nonnull String nickGroupCode) {
        Preconditions.checkNotNull(websiteName);
        Preconditions.checkNotNull(nickGroupCode);
        String redisKey = getRedisKeyForWebsiteGroupLastInfo(websiteName, nickGroupCode);
        String value = RedisUtils.hget(redisKey, AttributeKey.SUCCESS_USER_COUNT);
        if (StringUtils.isBlank(value)) {
            return 0;
        }
        return Integer.parseInt(value);
    }

    public static int getFailUserCount(@Nonnull String websiteName) {
        Preconditions.checkNotNull(websiteName);
        String redisKey = getRedisKeyForWebsiteLastInfo(websiteName);
        String value = RedisUtils.hget(redisKey, AttributeKey.FAIL_USER_COUNT);
        if (StringUtils.isBlank(value)) {
            return 0;
        }
        return Integer.parseInt(value);
    }

    public static int getFailUserCountForNickGroup(@Nonnull String nickGroupCode) {
        Preconditions.checkNotNull(nickGroupCode);
        String redisKey = getRedisKeyForNickGroupLastInfo(nickGroupCode);
        String value = RedisUtils.hget(redisKey, AttributeKey.FAIL_USER_COUNT);
        if (StringUtils.isBlank(value)) {
            return 0;
        }
        return Integer.parseInt(value);
    }

    public static int getFailUserCountForWebsiteGroup(@Nonnull String websiteName, @Nonnull String nickGroupCode) {
        Preconditions.checkNotNull(websiteName);
        Preconditions.checkNotNull(nickGroupCode);
        String redisKey = getRedisKeyForWebsiteGroupLastInfo(websiteName, nickGroupCode);
        String value = RedisUtils.hget(redisKey, AttributeKey.FAIL_USER_COUNT);
        if (StringUtils.isBlank(value)) {
            return 0;
        }
        return Integer.parseInt(value);
    }

    public static long getStatisticsTimestamp(@Nonnull String websiteName) {
        Preconditions.checkNotNull(websiteName);
        String redisKey = getRedisKeyForWebsiteLastInfo(websiteName);
        String value = RedisUtils.hget(redisKey, AttributeKey.STATISTICS_TIMESTAMP);
        if (StringUtils.isBlank(value)) {
            return 0;
        }
        return Long.parseLong(value);
    }

    public static long getSuccessTimestamp(String websiteName) {
        Preconditions.checkNotNull(websiteName);
        String redisKey = getRedisKeyForWebsiteLastInfo(websiteName);
        String value = RedisUtils.hget(redisKey, AttributeKey.SUCCESS_TIMESTAMP);
        if (StringUtils.isBlank(value)) {
            return 0;
        }
        return Long.parseLong(value);
    }

    public static long getSuccessTimestampForNickGroup(@Nonnull String nickGroupCode) {
        Preconditions.checkNotNull(nickGroupCode);
        String redisKey = getRedisKeyForNickGroupLastInfo(nickGroupCode);
        String value = RedisUtils.hget(redisKey, AttributeKey.SUCCESS_TIMESTAMP);
        if (StringUtils.isBlank(value)) {
            return 0;
        }
        return Long.parseLong(value);
    }

    public static long getSuccessTimestampForWebsiteGroup(@Nonnull String websiteName, @Nonnull String nickGroupCode) {
        Preconditions.checkNotNull(websiteName);
        Preconditions.checkNotNull(nickGroupCode);
        String redisKey = getRedisKeyForWebsiteGroupLastInfo(websiteName, nickGroupCode);
        String value = RedisUtils.hget(redisKey, AttributeKey.SUCCESS_TIMESTAMP);
        if (StringUtils.isBlank(value)) {
            return 0;
        }
        return Long.parseLong(value);
    }

    public static Long getWebisteMonitorId(@Nonnull String websiteName, @Nonnull Long preId, @Nonnull Date orderDate) {
        return getWebisteMonitorId(websiteName, preId, DateUtils.formatYmd(orderDate));
    }

    public static Long getWebisteMonitorId(@Nonnull String websiteName, @Nonnull Long preId, @Nonnull String monitorDay) {
        String postfix = TaskUtils.getSassEnv(websiteName + "." + monitorDay);
        String redisKey = RedisKeyPrefixEnum.WEBSITE_MONITOR_ID.getRedisKey(postfix);
        if (RedisUtils.exists(redisKey)) {
            String id = RedisUtils.get(redisKey);
            logger.info("get website_monitor_id success websiteName={},preId={},id={}", websiteName, preId, id);
            return Long.valueOf(id);
        }
        boolean b = RedisUtils.setnx(redisKey, preId.toString(), RedisKeyPrefixEnum.WEBSITE_MONITOR_ID.toSeconds());
        logger.info("pre set website_monitor_id websiteName={},preId={},b={}", websiteName, preId, b);
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            logger.error("getWebisteMonitorId error redisKey={}", redisKey, e);
        }
        String id = RedisUtils.get(redisKey);
        logger.info("get website_monitor_id success websiteName={},preId={},id={}", websiteName, preId, id);
        return Long.valueOf(id);
    }

    public static Long getNickGroupMonitorId(@Nonnull String nickGroupCode, @Nonnull Long preId, @Nonnull Date orderDate) {
        return getNickGroupMonitorId(nickGroupCode, preId, DateUtils.formatYmd(orderDate));
    }

    public static Long getNickGroupMonitorId(@Nonnull String nickGroupCode, @Nonnull Long preId, @Nonnull String monitorDay) {
        String postfix = TaskUtils.getSassEnv(nickGroupCode + "." + monitorDay);
        String redisKey = RedisKeyPrefixEnum.NICK_GROUP_MONITOR_ID.getRedisKey(postfix);
        if (RedisUtils.exists(redisKey)) {
            String id = RedisUtils.get(redisKey);
            logger.info("get group_monitor_id success nickGroupCode={},preId={},id={}", nickGroupCode, preId, id);
            return Long.valueOf(id);
        }
        boolean b = RedisUtils.setnx(redisKey, preId.toString(), RedisKeyPrefixEnum.NICK_GROUP_MONITOR_ID.toSeconds());
        logger.info("pre set group_monitor_id nickGroupCode={},preId={},b={}", nickGroupCode, preId, b);
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            logger.error("getNickGroupMonitorId error redisKey={}", redisKey, e);
        }
        String id = RedisUtils.get(redisKey);
        logger.info("get group_monitor_id success nickGroupCode={},preId={},id={}", nickGroupCode, preId, id);
        return Long.valueOf(id);
    }

    public static Long getWebisteGroupMonitorId(@Nonnull String websiteName, @Nonnull String nickGroupCode, @Nonnull Long preId, @Nonnull Date orderDate) {
        return getWebisteGroupMonitorId(websiteName, nickGroupCode, preId, DateUtils.formatYmd(orderDate));
    }

    public static Long getWebisteGroupMonitorId(@Nonnull String websiteName, @Nonnull String nickGroupCode, @Nonnull Long preId, @Nonnull String monitorDay) {
        String postfix = TaskUtils.getSassEnv(websiteName + "." + nickGroupCode + "." + monitorDay);
        String redisKey = RedisKeyPrefixEnum.WEBSITE_GROUP_MONITOR_ID.getRedisKey(postfix);
        if (RedisUtils.exists(redisKey)) {
            String id = RedisUtils.get(redisKey);
            logger.info("get website_group_monitor_id success websiteName={},nickGroupCode={},preId={},id={}", websiteName, nickGroupCode, preId, id);
            return Long.valueOf(id);
        }
        boolean b = RedisUtils.setnx(redisKey, preId.toString(), RedisKeyPrefixEnum.WEBSITE_GROUP_MONITOR_ID.toSeconds());
        logger.info("pre set website_group_monitor_id websiteName={},nickGroupCode={},preId={},b={}", websiteName, nickGroupCode, preId, b);
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            logger.error("getWebisteGroupMonitorId error redisKey={}", redisKey, e);
        }
        String id = RedisUtils.get(redisKey);
        logger.info("get website_group_monitor_id success websiteName={},nickGroupCode={},preId={},id={}", websiteName, nickGroupCode, preId, id);
        return Long.valueOf(id);
    }

    public static void updateWebsiteDayList(@Nonnull String websiteName, @Nonnull Long taskId, long timestamp) {
        String postfix = TaskUtils.getSassEnv(DateUtils.formatYmd(new Date(timestamp)));
        String redisKey = RedisKeyPrefixEnum.WEBSITE_DAY_LIST.getRedisKey(postfix);
        RedisUtils.hset(redisKey, websiteName, taskId.toString());
    }

    public static void updateWebsiteGroupDayList(@Nonnull String websiteName, @Nonnull String nickGroupCode, @Nonnull Long taskId, long timestamp) {
        String postfix = TaskUtils.getSassEnv(DateUtils.formatYmd(new Date(timestamp)));
        String redisKey = RedisKeyPrefixEnum.WEBSITE_GROUP_DAY_LIST.getRedisKey(postfix);
        RedisUtils.hset(redisKey, websiteName + "-" + nickGroupCode, taskId.toString());
    }

    public static void updateNickGroupDayList(@Nonnull String nickGroupCode, Long taskId, long timestamp) {
        String postfix = TaskUtils.getSassEnv(DateUtils.formatYmd(new Date(timestamp)));
        String redisKey = RedisKeyPrefixEnum.NICK_GROUP_DAY_LIST.getRedisKey(postfix);
        RedisUtils.hset(redisKey, nickGroupCode, taskId.toString());
    }

    public static void updateWithGroupSuccess(@Nonnull String groupCode, long taskId, long timestamp) {
        Preconditions.checkNotNull(groupCode);
        String redisKey = RedisKeyPrefixEnum.GROUP_LAST_INFO.getRedisKey(TaskUtils.getSassEnv(groupCode));
        String redisTimestamp = RedisUtils.hget(redisKey, AttributeKey.SUCCESS_TIMESTAMP);
        boolean update = StringUtils.isBlank(redisTimestamp) || timestamp > Long.parseLong(redisTimestamp);
        if (update) {
            RedisUtils.hset(redisKey, AttributeKey.SUCCESS_TIMESTAMP, String.valueOf(timestamp));
            RedisUtils.hset(redisKey, AttributeKey.SUCCESS_TASK_ID, String.valueOf(taskId));
            logger.info("upate group last info with task success,groupCode={},taskId={},timestamp={}", groupCode, taskId, DateUtils.formatYmdhms(timestamp));
        }
    }

    public static long getGroupSuccessTimestamp(@Nonnull String groupCode) {
        Preconditions.checkNotNull(groupCode);
        String redisKey = RedisKeyPrefixEnum.GROUP_LAST_INFO.getRedisKey(TaskUtils.getSassEnv(groupCode));
        String value = RedisUtils.hget(redisKey, AttributeKey.SUCCESS_TIMESTAMP);
        if (StringUtils.isBlank(value)) {
            return 0;
        }
        return Long.parseLong(value);
    }

    public static String getRedisKeyForWebsiteLastInfo(@Nonnull String websiteName) {
        return RedisKeyPrefixEnum.WEBSITE_LAST_INFO.getRedisKey(TaskUtils.getSassEnv(websiteName));
    }

    public static String getRedisKeyForWebsiteGroupLastInfo(@Nonnull String websiteName, @Nonnull String nickGroupCode) {
        return RedisKeyPrefixEnum.WEBSITE_GROUP_LAST_INFO.getRedisKey(TaskUtils.getSassEnv(nickGroupCode), websiteName);
    }

    public static String getRedisKeyForNickGroupLastInfo(@Nonnull String nickGroupCode) {
        return RedisKeyPrefixEnum.NICK_GROUP_LAST_INFO.getRedisKey(TaskUtils.getSassEnv(nickGroupCode));
    }

    public static Set<String> getWebsitesByNickGroupCode(String nickGroupCode) {
        if (StringUtils.isEmpty(nickGroupCode)) {
            return Collections.emptySet();
        }

        return NICK_GROU_WEBSITE.computeIfAbsent(nickGroupCode, k -> new HashSet<>());
    }

    public static void cacheNickGroupCodeWebsites(String nickGroupCode, String websiteName) {
        if (StringUtils.isEmpty(nickGroupCode) || StringUtils.isEmpty(websiteName)) {
            return;
        }

        Set<String> websites = getWebsitesByNickGroupCode(nickGroupCode);
        if (!websites.contains(websiteName)) {
            logger.info("add new website : {} for nick group code : {}", websiteName, nickGroupCode);
            websites.add(websiteName);
        }
    }
}
