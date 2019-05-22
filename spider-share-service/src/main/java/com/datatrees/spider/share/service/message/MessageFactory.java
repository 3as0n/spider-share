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

package com.datatrees.spider.share.service.message;

import com.alibaba.rocketmq.common.message.Message;
import com.datatrees.common.conf.PropertiesConfiguration;
import com.datatrees.common.util.GsonUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author <A HREF="">Cheng Wang</A>
 * @version 1.0
 * @since 2015年8月11日 下午3:06:07
 */
public class MessageFactory {

    private static final Logger logger = LoggerFactory.getLogger(MessageFactory.class);

    private String defaultTopic;

    private int flag = 0;

    private String defaultTags = "";

    private String defaultKeys = "";

    private boolean waitStoreMsgOK = true;

    private String messageTags = PropertiesConfiguration.getInstance().get("core.mq.message.tags", "bankbill,taobao,operator");

    private Map<String, Set<String>> keyTagMap = null;

    {
        keyTagMap = new HashMap<String, Set<String>>();
        for (String tag : messageTags.split(",")) {
            String keys = PropertiesConfiguration.getInstance().get("core.mq.tag." + tag + ".keys");
            if (StringUtils.isNotEmpty(keys)) {
                for (String key : keys.split(",")) {
                    Set<String> set = keyTagMap.get(key);
                    if (set == null) {
                        set = new HashSet<String>();
                        keyTagMap.put(key, set);
                    }
                    set.add(tag);
                }
            } else {
                logger.warn("Get empty value with key '" + "core.mq.tag." + tag + ".keys'.");
            }
        }
        logger.info("keyTagMap init success,result:" + keyTagMap);
    }

    public List<Message> getMessage(Map<String, Object> result) {
        return this.getMessage(result, defaultKeys);
    }

    public List<Message> getMessage(Map<String, Object> result, String key) {
        return this.getMessage(defaultTopic, result, key);
    }

    public Message getMessage(String topic, String tag, String body, String keyPrefix) throws UnsupportedEncodingException {
        Message message = new Message(topic, tag, keyPrefix + "_" + tag, flag, body.getBytes(), waitStoreMsgOK);
        return message;
    }

    public List<Message> getMessage(String topic, Map<String, Object> map, String keyPrefix) {
        Map<String, Map<String, Object>> resultsMap = new HashMap<String, Map<String, Object>>();
        Map<String, Object> commonFields = new HashMap<String, Object>();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            Set<String> tags = keyTagMap.get(entry.getKey());
            if (tags != null) {
                for (String tag : tags) {
                    Map<String, Object> result = resultsMap.get(tag);
                    if (result == null) {
                        result = new HashMap<String, Object>();
                        resultsMap.put(tag, result);
                    }
                    result.put(entry.getKey(), entry.getValue());
                }
            } else {
                commonFields.put(entry.getKey(), entry.getValue());
            }
        }
        List<Message> messages = new ArrayList<Message>();
        for (Map.Entry<String, Map<String, Object>> entry : resultsMap.entrySet()) {
            Map<String, Object> value = entry.getValue();
            value.putAll(commonFields);
            String body = GsonUtils.toJson(value);
            Message message = new Message(topic, entry.getKey(), keyPrefix + "_" + entry.getKey(), flag, body.getBytes(), waitStoreMsgOK);
            messages.add(message);
        }

        return messages;
    }

    /**
     * @return the defaultTopic
     */
    public String getDefaultTopic() {
        return defaultTopic;
    }

    /**
     * @param defaultTopic the defaultTopic to set
     */
    public void setDefaultTopic(String defaultTopic) {
        this.defaultTopic = defaultTopic;
    }

    /**
     * @return the flag
     */
    public int getFlag() {
        return flag;
    }

    /**
     * @param flag the flag to set
     */
    public void setFlag(int flag) {
        this.flag = flag;
    }

    /**
     * @return the defaultTags
     */
    public String getDefaultTags() {
        return defaultTags;
    }

    /**
     * @param defaultTags the defaultTags to set
     */
    public void setDefaultTags(String defaultTags) {
        this.defaultTags = defaultTags;
    }

    /**
     * @return the defaultKeys
     */
    public String getDefaultKeys() {
        return defaultKeys;
    }

    /**
     * @param defaultKeys the defaultKeys to set
     */
    public void setDefaultKeys(String defaultKeys) {
        this.defaultKeys = defaultKeys;
    }

    /**
     * @return the waitStoreMsgOK
     */
    public boolean isWaitStoreMsgOK() {
        return waitStoreMsgOK;
    }

    /**
     * @param waitStoreMsgOK the waitStoreMsgOK to set
     */
    public void setWaitStoreMsgOK(boolean waitStoreMsgOK) {
        this.waitStoreMsgOK = waitStoreMsgOK;
    }

}
