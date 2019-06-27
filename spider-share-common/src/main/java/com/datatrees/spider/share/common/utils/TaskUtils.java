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
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.datatrees.spider.share.common.shaded.java.net.HttpCookie;
import com.datatrees.spider.share.domain.AttributeKey;
import com.datatrees.spider.share.domain.ErrorCode;
import com.datatrees.spider.share.domain.RedisKeyPrefixEnum;
import com.datatrees.spider.share.domain.StepEnum;
import com.datatrees.spider.share.domain.http.Cookie;
import com.datatrees.spider.share.domain.http.HttpHeadKey;
import com.datatrees.spider.share.domain.http.Request;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.cookie.ClientCookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Created by zhouxinghai on 2017/7/19.
 */
public final class TaskUtils {

    private static final Logger logger = LoggerFactory.getLogger(TaskUtils.class);
    private static final String DOMAIN_SEPARATOR = ".";
    private static final String COOKIE_DOMAIN = "domain";
    private static final String COOKIE_PATH = "path";
    private static final String COOKIE_EXPIRES = "expires";
    private static final String COOKIE_HTTPONLY = "httponly";

    private TaskUtils() {}

    public static Cookie toCrawlerCookie(ClientCookie from) {
        String domain = from.getDomain();
        if (domain.startsWith(DOMAIN_SEPARATOR)) {
            domain = domain.substring(1);
        }
        Cookie to = new Cookie();
        to.setDomain(domain);
        to.setPath(from.getPath());
        to.setName(from.getName());
        to.setValue(from.getValue());
        to.setSecure(from.isSecure());
        to.setVersion(from.getVersion());
        to.setExpiryDate(from.getExpiryDate());
        if (from.containsAttribute(COOKIE_DOMAIN)) {
            to.putAttr(COOKIE_DOMAIN, domain);
        }
        if (from.containsAttribute(COOKIE_PATH)) {
            to.putAttr(COOKIE_PATH, from.getAttribute(COOKIE_PATH));
        }
        if (from.containsAttribute(COOKIE_EXPIRES)) {
            to.putAttr(COOKIE_EXPIRES, from.getAttribute(COOKIE_EXPIRES));
        }
        if (from.containsAttribute(COOKIE_HTTPONLY)) {
            to.putAttr(COOKIE_HTTPONLY, from.getAttribute(COOKIE_HTTPONLY));
        }
        return to;
    }

    public static List<Cookie> getCookies(Long taskId, String host, BasicCookieStore cookieStore, CloseableHttpResponse httpResponse) {
        CheckUtils.checkNotNull(cookieStore, "cookieStore is null");
        List<Cookie> list = new ArrayList<>();
        List<org.apache.http.cookie.Cookie> cookies = cookieStore.getCookies();
        for (org.apache.http.cookie.Cookie cookie : cookies) {
            list.add(toCrawlerCookie((BasicClientCookie)cookie));
        }
        // 更新自定义cookie
        Header[] headers = httpResponse.getHeaders(HttpHeadKey.SET_COOKIE);
        if (null != headers && headers.length > 0) {
            for (Header header : headers) {
                logger.info("处理response cookie >> {}", header);
                String headerValue = header.getValue();
                if (StringUtils.isEmpty(headerValue)) {
                    continue;
                }

                HttpCookie httpCookie;
                try {
                    httpCookie = HttpCookie.parse(headerValue).get(0);
                } catch (IllegalArgumentException e) {
                    logger.warn("更新cookie时发生IllegalArgumentException,捕获后跳过此异常。headerValue={}", headerValue);
                    continue;
                }
                Cookie orignCookie = findCookie(host, httpCookie, list);
                if (null != orignCookie) {
                    if (!StringUtils.equals(orignCookie.getValue(), httpCookie.getValue())) {
                        logger.info("更新cookie,taskId={},cookeName={},domain={},update value {}-->{}", taskId, orignCookie.getName(), orignCookie.getDomain(),
                            orignCookie.getValue(), httpCookie.getValue());
                        orignCookie.setValue(httpCookie.getValue());
                    }
                } else {
                    String domain = StringUtils.isBlank(httpCookie.getDomain()) ? host : httpCookie.getDomain();
                    if (StringUtils.startsWith(domain, DOMAIN_SEPARATOR)) {
                        domain = domain.substring(1);
                    }
                    Cookie cookie = new Cookie();
                    cookie.setName(httpCookie.getName());
                    cookie.setValue(httpCookie.getValue());
                    cookie.setDomain(domain);
                    cookie.setPath(httpCookie.getPath());
                    cookie.setVersion(httpCookie.getVersion());
                    cookie.setSecure(httpCookie.getSecure());
                    cookie.putAttr(COOKIE_DOMAIN, domain);
                    cookie.putAttr(COOKIE_PATH, httpCookie.getPath());
                    long maxAge = httpCookie.getMaxAge();
                    if (maxAge <= 0) {
                        maxAge = TimeUnit.MINUTES.toSeconds(30);
                    }
                    cookie.setExpiryDate(new Date(System.currentTimeMillis() + maxAge * 1000));
                    list.add(cookie);
                    logger.info("新增 rejected cookie, taskId={},cookeName={},value={},domain={}", taskId, cookie.getName(), cookie.getValue(), domain);
                }
            }
        }
        logger.info("更新后cookies >> {}", list);
        return list;
    }

    public static List<Cookie> getCookies(Long taskId) {
        List<Cookie> list = new ArrayList<>();
        String redisKey = RedisKeyPrefixEnum.TASK_COOKIE.getRedisKey(taskId);
        Map<String, String> map = RedisUtils.hgetAll(redisKey);
        if (MapUtils.isNotEmpty(map)) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                Cookie cookie = JSON.parseObject(entry.getValue(), new TypeReference<Cookie>() {});
                list.add(cookie);
            }
        }
        return list;
    }

    public static List<Cookie> getCookies(Long taskId, String host) {
        CheckUtils.checkNotPositiveNumber(taskId, ErrorCode.EMPTY_TASK_ID);
        List<Cookie> list = getCookies(taskId);
        if (CollectionUtils.isNotEmpty(list)) {
            Iterator<Cookie> iterator = list.iterator();
            while (iterator.hasNext()) {
                Cookie cookie = iterator.next();
                if (!StringUtils.endsWith(host, cookie.getDomain())) {
                    iterator.remove();
                    continue;
                }
                String domain = cookie.getDomain();
                if (domain.length() != host.length()) {
                    domain = DOMAIN_SEPARATOR + domain;
                    cookie.setDomain(domain);
                    cookie.putAttr(COOKIE_DOMAIN, domain);
                }
            }
        }
        return list;
    }

    public static List<Cookie> getCookies(Set<com.gargoylesoftware.htmlunit.util.Cookie> cookies) {
        List<Cookie> list = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(cookies)) {
            for (com.gargoylesoftware.htmlunit.util.Cookie htmlCookie : cookies) {
                list.add(toCrawlerCookie((ClientCookie)(htmlCookie.toHttpClient())));
            }
        }
        return list;
    }

    public static String getCookieString(Long taskId) {
        List<Cookie> list = getCookies(taskId);

        return getCookieString(list);
    }

    public static String getCookieString(List<Cookie> cookies) {
        if (CollectionUtils.isNotEmpty(cookies)) {
            return cookies.stream().map(cookie -> cookie.getName() + "=" + cookie.getValue()).collect(Collectors.joining("; "));
        }
        return StringUtils.EMPTY;
    }

    public static Map<String, String> getResponseCookieMap(Request request, List<Cookie> cookies) {
        Map<String, String> requestCookies = request.getRequestCookies();
        Map<String, String> receiveCookieMap = new HashMap<>();
        if (CollectionUtils.isEmpty(cookies)) {
            return receiveCookieMap;
        }
        for (Cookie cookie : cookies) {
            if (!requestCookies.containsKey(cookie.getName())) {
                receiveCookieMap.put(cookie.getName(), cookie.getValue());
            }
        }
        return receiveCookieMap;
    }

    public static Map<String, String> getCookieMap(List<Cookie> cookies) {
        Map<String, String> map = new HashMap<>();
        if (CollectionUtils.isNotEmpty(cookies)) {
            for (Cookie cookie : cookies) {
                map.put(cookie.getName(), cookie.getValue());
            }
        }
        return map;
    }

    public static void saveCookie(long taskId, Set<com.gargoylesoftware.htmlunit.util.Cookie> cookies) {
        List<Cookie> list = getCookies(cookies);
        saveCookie(taskId, list);
    }

    public static void saveCookie(long taskId, String host, BasicCookieStore cookieStore, CloseableHttpResponse httpResponse) {
        List<Cookie> list = getCookies(taskId, host, cookieStore, httpResponse);
        saveCookie(taskId, list);
    }

    public static void saveCookie(long taskId, List<Cookie> cookies) {
        if (CollectionUtils.isEmpty(cookies)) {
            return;
        }
        String redisKey = RedisKeyPrefixEnum.TASK_COOKIE.getRedisKey(taskId);
        for (Cookie cookie : cookies) {
            String name = "[" + cookie.getName() + "][" + cookie.getDomain() + "][" + cookie.getPath() + "]";
            RedisUtils.hset(redisKey, name, JSON.toJSONString(cookie, SerializerFeature.DisableCircularReferenceDetect), RedisKeyPrefixEnum.TASK_COOKIE.toSeconds());

        }
    }

    public static BasicClientCookie getBasicClientCookie(Cookie cookie) {
        BasicClientCookie basicClientCookie = new BasicClientCookie(cookie.getName(), cookie.getValue());
        basicClientCookie.setDomain(cookie.getDomain());
        basicClientCookie.setPath(cookie.getPath());
        basicClientCookie.setSecure(cookie.isSecure());
        basicClientCookie.setVersion(cookie.getVersion());
        basicClientCookie.setExpiryDate(cookie.getExpiryDate());
        cookie.foreachAttribs(basicClientCookie::setAttribute);
        return basicClientCookie;
    }

    public static BasicCookieStore buildBasicCookieStore(List<Cookie> list) {
        BasicCookieStore cookieStore = new BasicCookieStore();
        if (CollectionUtils.isNotEmpty(list)) {
            for (Cookie cookie : list) {
                cookieStore.addCookie(getBasicClientCookie(cookie));
            }
        }
        return cookieStore;
    }

    public static String getCookieValue(Long taskId, String cookieName) {
        List<Cookie> list = getCookies(taskId);
        if (CollectionUtils.isNotEmpty(list)) {
            for (Cookie cookie : list) {
                if (StringUtils.equals(cookieName, cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        logger.warn("not found cookieName={}", cookieName);
        return null;
    }

    /**
     * 添加共享属性
     * 
     * @param taskId
     * @param name
     * @param value
     */
    public static void addTaskShare(Long taskId, String name, String value) {
        if (null == taskId || StringUtils.isBlank(name) || StringUtils.isBlank(value)) {
            logger.error("invalid param taskId={},name={},value={}", taskId, name, value);
            return;
        }
        String redisKey = RedisKeyPrefixEnum.TASK_SHARE.getRedisKey(taskId);
        RedisUtils.hset(redisKey, name, value, RedisKeyPrefixEnum.TASK_SHARE.toSeconds());
        logger.info("addTaskShare success taskId={},name={}", taskId, name);
    }

    /**
     * 获取共享属性
     * 
     * @param taskId
     * @param name
     */
    public static String getTaskShare(Long taskId, String name) {
        String redisKey = RedisKeyPrefixEnum.TASK_SHARE.getRedisKey(taskId);
        if (!RedisUtils.hexists(redisKey, name)) {
            logger.warn("property not found, redisKey={},name={}", redisKey, name);
            return null;
        }
        return RedisUtils.hget(redisKey, name);
    }

    /**
     * 删除共享属性
     * 
     * @param taskId
     * @param name
     */
    public static void removeTaskShare(Long taskId, String name) {
        String redisKey = RedisKeyPrefixEnum.TASK_SHARE.getRedisKey(taskId);
        if (!RedisUtils.hexists(redisKey, name)) {
            logger.warn("property not found, redisKey={},name={}", redisKey, name);
            return;
        }
        RedisUtils.hdel(redisKey, name);
        logger.info("removeTaskShare success taskId={},name={}", taskId, name);
    }

    /**
     * 获取共享属性
     * 
     * @param taskId
     */
    public static Map<String, String> getTaskShares(Long taskId) {
        String redisKey = RedisKeyPrefixEnum.TASK_SHARE.getRedisKey(taskId);
        return RedisUtils.hgetAll(redisKey);
    }

    /**
     * 初始化共享信息
     * 
     * @param taskId
     * @param websiteName
     */
    public static void initTaskShare(Long taskId, String websiteName) {
        String redisKey = RedisKeyPrefixEnum.TASK_SHARE.getRedisKey(taskId);

        RedisUtils.hset(redisKey, AttributeKey.WEBSITE_NAME, websiteName);

        RedisUtils.expire(redisKey, RedisKeyPrefixEnum.TASK_SHARE.toSeconds());
        logger.info("initTaskShare success taskId={},websiteName={}", taskId, websiteName);
    }

    public static void initTaskContext(Long taskId, Map<String, Object> context) {
        String redisKey = RedisKeyPrefixEnum.TASK_CONTEXT.getRedisKey(taskId);
        if (null == context || context.isEmpty()) {
            logger.warn("initTaskContext fail,context is empty,taskId={}", taskId);
            return;
        }
        for (Map.Entry<String, Object> entry : context.entrySet()) {
            if (entry.getValue() instanceof String) {
                RedisUtils.hset(redisKey, entry.getKey(), entry.getValue().toString());
            } else {
                RedisUtils.hset(redisKey, entry.getKey(), JSON.toJSONString(entry.getValue()));
            }
            RedisUtils.expire(redisKey, RedisKeyPrefixEnum.TASK_CONTEXT.toSeconds());
        }
    }

    public static String getTaskContext(Long taskId, String name) {
        String redisKey = RedisKeyPrefixEnum.TASK_CONTEXT.getRedisKey(taskId);
        Map<String, String> map = RedisUtils.hgetAll(redisKey);
        if (null == map || !map.containsKey(name)) {
            return null;
        }
        return map.get(name);
    }

    public static Cookie findCookie(String host, HttpCookie httpCookie, List<Cookie> list) {
        String domain = StringUtils.isBlank(httpCookie.getDomain()) ? host : httpCookie.getDomain();
        if (StringUtils.startsWith(domain, DOMAIN_SEPARATOR)) {
            domain = domain.substring(1);
        }
        for (Cookie cookie : list) {
            if (StringUtils.equals(httpCookie.getName(), cookie.getName()) && StringUtils.endsWith(domain, cookie.getDomain())) {
                return cookie;
            }
        }
        return null;
    }

    public static void updateCookies(Long taskId, Map<String, String> newCookieMap) {
        List<Cookie> cookies;
        if (MapUtils.isNotEmpty(newCookieMap) && CollectionUtils.isNotEmpty(cookies = getCookies(taskId))) {
            for (Map.Entry<String, String> entry : newCookieMap.entrySet()) {
                Cookie find = null;
                for (Cookie cookie : cookies) {
                    if (StringUtils.equals(entry.getKey(), cookie.getName())) {
                        find = cookie;
                        break;
                    }
                }
                if (null == find) {
                    logger.info("新增了cookie,but没有处理,taskId={},name={},value={}", taskId, entry.getKey(), entry.getValue());
                    continue;
                }
                if (!StringUtils.equals(entry.getKey(), find.getValue())) {
                    logger.info("变更了cookie,taskId={},name={},value:{}-->{}", taskId, entry.getKey(), find.getValue(), entry.getValue());
                    find.setValue(entry.getValue());
                }
            }
            saveCookie(taskId, cookies);
        }
    }

    /**
     * 记录任务阶段
     * 
     * @param taskId
     * @param stepEnum
     */
    public static void addStep(Long taskId, StepEnum stepEnum) {
        addTaskShare(taskId, AttributeKey.STEP_CODE, stepEnum.getStepCode() + "");
        addTaskShare(taskId, AttributeKey.STEP_NAME, stepEnum.getStepName() + "");
    }

    /**
     * 获取环境变量
     * 
     * @return
     */
    public static String getSassEnv() {
        return System.getProperty(AttributeKey.SAAS_ENV, "none");
    }

    public static boolean isDev() {
        return "dev".equalsIgnoreCase(getSassEnv());
    }

    public static String getSassEnv(String postfix) {
        return getSassEnv() + "." + postfix;
    }

    public static boolean stepCheck(Long taskId, StepEnum stepEnum) {
        String stepCode = getTaskShare(taskId, AttributeKey.STEP_CODE);
        return StringUtils.equals(stepCode, stepEnum.getStepCode() + "");
    }

    public static boolean isSuccess(int crawlerStatus, String checkStatus) {
        return crawlerStatus == 100 && StringUtils.equalsAny(checkStatus, "正常", "没有配置检查项目");
    }

    public static boolean isFail(int crawlerStatus, String checkStatus) {
        return crawlerStatus < 0 || StringUtils.equals(checkStatus, "严重");
    }

    public static long getCoreSize(long taskId) {
        String redisKey = RedisKeyPrefixEnum.TASK_RESULT.getRedisKey(taskId);
        Map<String, String> map = BackRedisUtils.hgetAll(redisKey);
        String coreRedisKey = null;
        if (map.containsKey("callDetails")) {
            coreRedisKey = map.get("callDetails");
        } else if (map.containsKey("trades")) {
            coreRedisKey = map.get("trades");
        } else if (map.containsKey("bankBills")) {
            coreRedisKey = map.get("bankBills");
        }
        if (StringUtils.isNotBlank(coreRedisKey)) {
            return BackRedisUtils.llen(coreRedisKey);
        }
        return 0;

    }

    public static boolean isLastLoginProcessId(long taskId, Long processId) {
        String lastProcessId = getTaskShare(taskId, AttributeKey.CURRENT_LOGIN_PROCESS_ID);
        boolean b = StringUtils.equals(lastProcessId, processId.toString());
        if (!b) {
            logger.error("this thread is not the last login thread,taskId={},processId={},lastProcessId={}", taskId, processId, lastProcessId);
        }
        return b;
    }

    public static HttpCookie checkFormat(String header) {
        try {
            StringTokenizer tokenizer = new StringTokenizer(header, ";");
            String namevaluePair = tokenizer.nextToken();
            int index = namevaluePair.indexOf('=');
            if (index == -1) {
                return new HttpCookie(namevaluePair, StringUtils.EMPTY);
            }
            return null;
        } catch (NoSuchElementException ignored) {
            throw new IllegalArgumentException("Empty cookie header string");
        }
    }

}
