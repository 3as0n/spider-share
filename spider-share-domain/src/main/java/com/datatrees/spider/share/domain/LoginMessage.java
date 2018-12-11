/*
 * Copyright © 2015 - 2018 杭州大树网络技术有限公司. All Rights Reserved
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.datatrees.spider.share.domain;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 登陆成功消息体
 * Created by zhouxinghai on 2017/5/2.
 */
public class LoginMessage implements Serializable {

    /**
     * 站点名
     */
    private String websiteName;

    /**
     * 任务ID
     */
    private Long   taskId;

    /**
     * 结束url
     */
    private String endUrl;

    /**
     * head配置:cookie
     */
    private String cookie;

    /**
     * head配置项:Set-Cookie
     */
    private String setCookie;

    /**
     * 用户账户
     */
    private String accountNo;

    /** 分组代码 */
    private String groupCode;

    /** 分组名称 */
    private String groupName;

    private Map<String, Object> extra = new HashMap<String, Object>();

    public String getWebsiteName() {
        return websiteName;
    }

    public void setWebsiteName(String websiteName) {
        this.websiteName = websiteName;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public String getEndUrl() {
        return endUrl;
    }

    public void setEndUrl(String endUrl) {
        this.endUrl = endUrl;
    }

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    public String getSetCookie() {
        return setCookie;
    }

    public void setSetCookie(String setCookie) {
        this.setCookie = setCookie;
    }

    /**
     * @return the accountNo
     */
    public String getAccountNo() {
        return accountNo;
    }

    /**
     * @param accountNo the accountNo to set
     */
    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getGroupCode() {
        return groupCode;
    }

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Map<String, Object> getExtra() {
        return extra;
    }

    public void setExtra(Map<String, Object> extra) {
        this.extra = extra;
    }

    @Override
    public String toString() {
        return "LoginMessage [websiteName=" + websiteName + ", taskId=" + taskId + ", endUrl=" + endUrl + ", accountNo=" + accountNo + "]";
    }
}
