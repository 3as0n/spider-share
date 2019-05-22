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

package com.datatrees.spider.share.domain.directive;

import java.io.Serializable;

/**
 * redis交互指令 Created by zhouxinghai on 2017/5/19.
 */
public class DirectiveResult<T> implements Serializable {

    /**
     * 应用名称
     */
    private static final String appName = "spider";

    /**
     * 当前线程ID
     */
    private static String threadId = "t0";

    /**
     * 指令ID
     */
    private String directiveId;

    /**
     * 发送指令后,是否等待插件处理完成并返回数据
     */
    private boolean requireReturn = false;

    /**
     * 交互类别
     */
    private String type;

    /**
     * 任务ID
     */
    private long taskId;

    /**
     * 当前指令状态
     */
    private String status;

    /**
     * 错误代码
     */
    private String errorCode;

    /**
     * 错误信息
     */
    private String errorMsg;

    /**
     * 交互数据
     */
    private T data;

    @Deprecated
    public DirectiveResult() {}

    public DirectiveResult(String type, long taskId) {
        this.type = type;
        this.taskId = taskId;
    }

    public DirectiveResult(String type, long taskId, String status, T data) {
        this.type = type;
        this.taskId = taskId;
        this.status = status;
        this.data = data;
    }

    /**
     * 获取指令池:一个插件就是一个指令池
     * 
     * @return
     */
    public static String getGroupKey(String type, long taskId) {
        if (null == type || type.trim().length() == 0) {
            throw new RuntimeException("getSendRedisKey type is blank");
        }
        if (taskId <= 0) {
            throw new RuntimeException("getSendRedisKey taskId is blank");
        }
        // 暂时不用多线程,不加线程threadId
        return new StringBuilder(appName).append("_directive_").append(type).append("_").append(taskId).toString();
    }

    /**
     * 指令独立存储key
     * 
     * @return
     */
    public static String getDirectiveKey(String type, String status, long taskId) {
        if (taskId <= 0) {
            throw new RuntimeException("getSendRedisKey taskId is blank");
        }
        if (null == type || type.trim().length() == 0) {
            throw new RuntimeException("getSendRedisKey type is blank");
        }
        if (null == status || status.trim().length() == 0) {
            throw new RuntimeException("getLockKey status is blank");
        }
        StringBuilder key = new StringBuilder(getGroupKey(type, taskId)).append("_").append(status);
        return key.toString();
    }

    /**
     * 指令枷锁的key 相同命令枷锁 加锁成功:发送指令,清除结果key,进入等待 加锁失败:进入等待结果
     * 
     * @return
     */
    public static String getLockKey(String type, String status, long taskId) {
        return new StringBuilder(getDirectiveKey(type, status, taskId)).append("_directive_lock").toString();
    }

    public static String getThreadId() {
        return threadId;
    }

    public static void setThreadId(String threadId) {
        DirectiveResult.threadId = threadId;
    }

    public static String getAppName() {
        return appName;
    }

    /**
     * 获取指令池:一个插件就是一个指令池
     * 
     * @return
     */
    public String getGroupKey() {
        return getGroupKey(type, taskId);
    }

    /**
     * 指令独立存储key
     * 
     * @return
     */
    public String getDirectiveKey() {
        return getDirectiveKey(type, status, taskId);
    }

    /**
     * 指令独立存储key
     * 
     * @return
     */
    public String getDirectiveKey(String status) {
        return getDirectiveKey(type, status, taskId);
    }

    /**
     * 指令枷锁的key 相同命令枷锁 加锁成功:发送指令,清除结果key,进入等待 加锁失败:进入等待结果
     * 
     * @return
     */
    public String getLockKey() {
        return getDirectiveKey(type, status, taskId);
    }

    /**
     * 处理成功
     * 
     * @param status
     * @return
     */
    public DirectiveResult<T> fill(String status) {
        this.setStatus(status);
        return this;
    }

    /**
     * 处理成功
     * 
     * @param status
     * @param data
     * @return
     */
    public DirectiveResult<T> fill(String status, T data) {
        this.setStatus(status);
        this.setData(data);
        return this;
    }

    /**
     * 发生错误
     * 
     * @param status
     * @param errorMsg
     * @return
     */
    public DirectiveResult<T> failTure(String status, String errorMsg) {
        return failTure(status, null, errorMsg);
    }

    /**
     * 发生错误
     * 
     * @param status
     * @param errorCode
     * @param errorMsg
     * @return
     */
    public DirectiveResult<T> failTure(String status, String errorCode, String errorMsg) {
        return failTure(status, errorCode, errorMsg, null);
    }

    /**
     * 发生错误
     * 
     * @param status
     * @param errorCode
     * @param errorMsg
     * @param data
     * @return
     */
    public DirectiveResult<T> failTure(String status, String errorCode, String errorMsg, T data) {
        this.setStatus(status);
        this.setErrorCode(errorCode);
        this.setErrorMsg(errorMsg);
        this.setData(data);
        return this;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getTaskId() {
        return taskId;
    }

    public void setTaskId(long taskId) {
        this.taskId = taskId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public boolean isRequireReturn() {
        return requireReturn;
    }

    public void setRequireReturn(boolean requireReturn) {
        this.requireReturn = requireReturn;
    }

    public String getDirectiveId() {
        return directiveId;
    }

    public void setDirectiveId(String directiveId) {
        this.directiveId = directiveId;
    }
}
