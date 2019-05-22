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

package com.datatrees.spider.share.service;

import com.datatrees.spider.share.domain.ErrorCode;
import com.datatrees.spider.share.domain.http.HttpResult;

import java.util.List;

/**
 * 监控 Created by zhouxinghai on 2017/9/4
 */
public interface MonitorService {

    /**
     * 初始化监控信息
     * 
     * @param taskId 任务id
     * @param websiteName 配置名称
     * @param userName 用户名
     */
    void initTask(Long taskId, String websiteName, Object userName);

    /**
     * 发送任务完成消息
     * 
     * @param taskId 任务id
     */
    void sendTaskCompleteMsg(Long taskId, String websiteName, Integer errorCode, String errorMsg);

    /**
     * 发送任务日志消息
     * 
     * @param taskId 任务id
     * @param errorCode 错误代码
     * @param errorMsg 错误信息
     * @param errorDetail 错误详细信息
     */
    void sendTaskLog(Long taskId, String websiteName, String msg, Integer errorCode, String errorMsg, String errorDetail);

    /**
     * 发送任务日志消息
     * 
     * @param taskId 任务id
     * @param result 处理结果
     */
    void sendTaskLog(Long taskId, String websiteName, String msg, HttpResult result);

    /**
     * 发送任务日志消息
     * 
     * @param taskId 任务id
     * @param errorCode 错误信息
     */
    void sendTaskLog(Long taskId, String websiteName, String msg, ErrorCode errorCode);

    /**
     * 发送任务日志消息
     * 
     * @param taskId 任务id
     * @param errorCode 错误信息
     */
    void sendTaskLog(Long taskId, String websiteName, String msg, ErrorCode errorCode, String errorDetail);

    /**
     * 发送任务日志消息
     * 
     * @param taskId 任务id
     * @param msg 操作信息
     * @return
     */
    void sendTaskLog(Long taskId, String websiteName, String msg);

    /**
     * 发送任务日志消息
     * 
     * @param taskId 任务id
     * @param msg 操作信息
     * @return
     */
    void sendTaskLog(Long taskId, String msg);

    /**
     * 发送任务日志消息
     * 
     * @param taskId 任务id
     * @param errorCode 错误信息
     */
    void sendTaskLog(Long taskId, String msg, ErrorCode errorCode, String errorDetail);

    /**
     * 发送任务日志消息
     * 
     * @param taskId 任务id
     * @param errorCode 错误信息
     */
    void sendTaskLog(Long taskId, String msg, ErrorCode errorCode);

    /**
     * 发送接口耗时
     * 
     * @param taskId
     * @param methodName 接口名称
     * @param params 参数
     * @param startTime 开始时间
     * @param finishTime 结束时间
     */
    /**
     * 发送接口耗时
     * 
     * @param taskId 任务id
     * @param websiteName 配置名称
     * @param key 关键字
     * @param className 接口名称
     * @param methodName 方法名称
     * @param param 参数
     * @param result 返回结果
     * @param startTime 开始时间
     * @param finishTime 结束时间
     */
    void sendMethodUseTime(Long taskId, String websiteName, String key, String className, String methodName, List<Object> param, Object result, long startTime, long finishTime);

}
