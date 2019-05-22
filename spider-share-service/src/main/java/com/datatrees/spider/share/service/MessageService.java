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

package com.datatrees.spider.share.service;

/**
 * 消息服务 Created by zhouxinghai on 2017/5/11.
 */
public interface MessageService {

    /**
     * 向网关发送任务日志消息
     * 
     * @param taskId 任务id
     * @param msg 操作信息
     * @return
     */
    boolean sendTaskLog(Long taskId, String msg);

    /**
     * 向网关发送任务日志消息
     * 
     * @param taskId 任务id
     * @param msg 操作信息
     * @param errorDetail 错误信息
     * @return
     */
    boolean sendTaskLog(Long taskId, String msg, String errorDetail);

    /**
     * 发送交互指令
     * 
     * @param taskId 任务id
     * @param directive 指令
     * @param remark 指令内容
     * @return 指令ID
     */
    String sendDirective(Long taskId, String directive, String remark);

    /**
     * 发送交互指令
     * 
     * @param taskId 任务id
     * @param directive 指令
     * @param remark 指令内容
     * @return 指令ID
     */
    String sendDirective(Long taskId, String directive, String remark, String formType);

    /**
     * 发送消息
     * 
     * @param topic 订阅主题
     * @param msg 消息
     * @return
     */
    boolean sendMessage(String topic, Object msg);

    /**
     * 发送消息
     * 
     * @param topic 订阅主题
     * @param msg 消息
     * @param charsetName JSON个时候发送编码类型
     * @return
     */
    boolean sendMessage(String topic, Object msg, String charsetName);

    /**
     * 发送消息
     * 
     * @param topic 订阅主题
     * @param tags tags
     * @param msg 消息
     * @return
     */
    boolean sendMessage(String topic, String tags, Object msg);

    /**
     * 发送消息
     * 
     * @param topic 订阅主题
     * @param tags tags
     * @param msg 消息
     * @param charsetName JSON个时候发送编码类型
     * @return
     */
    boolean sendMessage(String topic, String tags, Object msg, String charsetName);

    /**
     * 发送登陆成功消息
     * 
     * @param topic
     * @param tag
     * @param taskId
     * @return
     */
    void sendLoginSuccessMessage(String topic, String tag, long taskId);

}
