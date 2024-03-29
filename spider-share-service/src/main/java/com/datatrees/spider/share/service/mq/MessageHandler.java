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

package com.datatrees.spider.share.service.mq;

import com.alibaba.rocketmq.common.message.MessageExt;

/**
 * 通用topic处理
 */
public interface MessageHandler {

    /**
     * 业务类型
     * 
     * @return
     */
    String getTitle();

    /**
     * 处理消息
     * 
     * @return
     */
    boolean consumeMessage(MessageExt messageExt, String msg);

    /**
     * 消费失败最大重试次数
     * 
     * @return
     */
    int getMaxRetry();

    /**
     * topic
     * 
     * @return
     */
    String getTopic();

    /**
     * 消息tag
     */
    String getTag();

    /**
     * 消息过期时间
     * 
     * @return
     */
    long getExpireTime();

}
