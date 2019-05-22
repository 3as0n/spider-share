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

package com.datatrees.spider.share.domain;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class CommonPluginParam implements Serializable {

    /**
     * 表单类型
     */
    private String formType;

    /**
     * 任务id 必填
     */
    private Long taskId;

    /**
     * website name 必填
     */
    private String websiteName;

    /**
     * 手机号
     */
    private Long mobile;

    /**
     * 用户名
     */
    private String username;

    /**
     * 登陆密码或者服务密码
     */
    @JSONField(serialize = false)
    private String password;

    /**
     * 图片验证码
     */
    private String picCode;

    /**
     * 短信验证码
     */
    private String smsCode;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 身份证号码
     */
    private String idCard;

    /**
     * 身份证类型
     */
    private String idCardType;

    /**
     * 分组代码
     */
    private String groupCode;

    /**
     * 分组名称
     */
    private String groupName;

    /**
     * 扩展属性
     */
    private Map<String, Object> extral = new HashMap<>();

    /**
     * 给自定义plugin方法用
     */
    private String[] args;

    /**
     * 插件名称
     */
    private String pluginName;

    /**
     * 插件服务名称
     */
    private String pluginClassName;

    /**
     * 指令id
     */
    private String directiveId;

    /**
     * 用使用代理,初始化有
     */
    private boolean proxyEnable = false;

    /**
     * 自动发送登陆成功消息
     */
    private boolean autoSendLoginSuccessMsg = true;

    /**
     * 用户IP
     */
    private String userIp;

    public CommonPluginParam() {}

    public CommonPluginParam(String formType, Long taskId, String websiteName) {
        this.formType = formType;
        this.taskId = taskId;
        this.websiteName = websiteName;
    }

    public String getFormType() {
        return formType;
    }

    public void setFormType(String formType) {
        this.formType = formType;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public String getWebsiteName() {
        return websiteName;
    }

    public void setWebsiteName(String websiteName) {
        this.websiteName = websiteName;
    }

    public Long getMobile() {
        return mobile;
    }

    public void setMobile(Long mobile) {
        this.mobile = mobile;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPicCode() {
        return picCode;
    }

    public void setPicCode(String picCode) {
        this.picCode = picCode;
    }

    public String getSmsCode() {
        return smsCode;
    }

    public void setSmsCode(String smsCode) {
        this.smsCode = smsCode;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
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

    public Map<String, Object> getExtral() {
        return extral;
    }

    public void setExtral(Map<String, Object> extral) {
        this.extral = extral;
    }

    public String[] getArgs() {
        return args;
    }

    public void setArgs(String[] args) {
        this.args = args;
    }

    public String getPluginName() {
        return pluginName;
    }

    public void setPluginName(String pluginName) {
        this.pluginName = pluginName;
    }

    public String getPluginClassName() {
        return pluginClassName;
    }

    public void setPluginClassName(String pluginClassName) {
        this.pluginClassName = pluginClassName;
    }

    public boolean isProxyEnable() {
        return proxyEnable;
    }

    public void setProxyEnable(boolean proxyEnable) {
        this.proxyEnable = proxyEnable;
    }

    public String getActionName() {
        return FormType.getName(formType);
    }

    public String getDirectiveId() {
        return directiveId;
    }

    public void setDirectiveId(String directiveId) {
        this.directiveId = directiveId;
    }

    public boolean isAutoSendLoginSuccessMsg() {
        return autoSendLoginSuccessMsg;
    }

    public void setAutoSendLoginSuccessMsg(boolean autoSendLoginSuccessMsg) {
        this.autoSendLoginSuccessMsg = autoSendLoginSuccessMsg;
    }

    public String getUserIp() {
        return userIp;
    }

    public void setUserIp(String userIp) {
        this.userIp = userIp;
    }

    public String getIdCardType() {
        return idCardType;
    }

    public void setIdCardType(String idCardType) {
        this.idCardType = idCardType;
    }
}
