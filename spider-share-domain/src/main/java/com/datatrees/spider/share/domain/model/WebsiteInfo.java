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

package com.datatrees.spider.share.domain.model;

import java.io.Serializable;
import java.util.Date;

/** create by system from table website_info(非运营商配置) */
public class WebsiteInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    // 主键(website_id)
    private Integer websiteId;
    // 区分不同环境(env)
    private String env;
    // 1:mail,3:ecommerce,4:bank,5:internal,6:education(website_type)
    private Integer websiteType;
    // 配置名称(website_name)
    private String websiteName;
    // 配置标题(website_title)
    private String websiteTitle;
    // 是否启用(0:不启用,1:启用)(enable)
    private Boolean enable;
    // 代理(0:不启用,1:启用)(proxy_enable)
    private Boolean proxyEnable;
    // 搜索配置(search_config)
    private String searchConfig;
    // 解析配置(extractor_config)
    private String extractorConfig;
    // 分组(group_code)
    private String groupCode;
    // 修改时间(updated_at)
    private Date updatedAt;
    // 插件启动阶段(在***表单提交成功之后发送登录成功消息)(start_stage)
    private String startStage;
    // 登录地址(login_url)
    private String loginUrl;
    // 插件地址(plugin_class)
    private String pluginClass;
    // 登录配置(login_config)
    private String loginConfig;
    // 短信重发间隔时间(全局)(sms_interval)
    private Integer smsInterval;
    // 描述(remark)
    private String remark;
    // 登录过程中自定义提示(login_tip)
    private String loginTip;
    // 爬过过程中校验,短信输入框提示(verify_tip)
    private String verifyTip;
    // 重置密码方式(SMS:短信, TEL:拨打电话, WEB:url重置)(reset_type)
    private String resetType;
    // 重置密码短信模板(sms_template)
    private String smsTemplate;
    // 重置密码接收手机号(sms_receiver)
    private String smsReceiver;
    // 重置密码url(reset_url)
    private String resetUrl;
    // 重置密码提示(reset_tip)
    private String resetTip;
    // (created_at)
    private Date createdAt;
    // 扩展信息(attribute)
    private String attribute;

    public Integer getWebsiteId() {
        return websiteId;
    }

    public void setWebsiteId(Integer websiteId) {
        this.websiteId = websiteId;
    }

    public String getEnv() {
        return env;
    }

    public void setEnv(String env) {
        this.env = env == null ? null : env.trim();
    }

    public Integer getWebsiteType() {
        return websiteType;
    }

    public void setWebsiteType(Integer websiteType) {
        this.websiteType = websiteType;
    }

    public String getWebsiteName() {
        return websiteName;
    }

    public void setWebsiteName(String websiteName) {
        this.websiteName = websiteName == null ? null : websiteName.trim();
    }

    public String getWebsiteTitle() {
        return websiteTitle;
    }

    public void setWebsiteTitle(String websiteTitle) {
        this.websiteTitle = websiteTitle == null ? null : websiteTitle.trim();
    }

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public Boolean getProxyEnable() {
        return proxyEnable;
    }

    public void setProxyEnable(Boolean proxyEnable) {
        this.proxyEnable = proxyEnable;
    }

    public String getSearchConfig() {
        return searchConfig;
    }

    public void setSearchConfig(String searchConfig) {
        this.searchConfig = searchConfig == null ? null : searchConfig.trim();
    }

    public String getExtractorConfig() {
        return extractorConfig;
    }

    public void setExtractorConfig(String extractorConfig) {
        this.extractorConfig = extractorConfig == null ? null : extractorConfig.trim();
    }

    public String getGroupCode() {
        return groupCode;
    }

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode == null ? null : groupCode.trim();
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getStartStage() {
        return startStage;
    }

    public void setStartStage(String startStage) {
        this.startStage = startStage == null ? null : startStage.trim();
    }

    public String getLoginUrl() {
        return loginUrl;
    }

    public void setLoginUrl(String loginUrl) {
        this.loginUrl = loginUrl == null ? null : loginUrl.trim();
    }

    public String getPluginClass() {
        return pluginClass;
    }

    public void setPluginClass(String pluginClass) {
        this.pluginClass = pluginClass == null ? null : pluginClass.trim();
    }

    public String getLoginConfig() {
        return loginConfig;
    }

    public void setLoginConfig(String loginConfig) {
        this.loginConfig = loginConfig == null ? null : loginConfig.trim();
    }

    public Integer getSmsInterval() {
        return smsInterval;
    }

    public void setSmsInterval(Integer smsInterval) {
        this.smsInterval = smsInterval;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public String getLoginTip() {
        return loginTip;
    }

    public void setLoginTip(String loginTip) {
        this.loginTip = loginTip == null ? null : loginTip.trim();
    }

    public String getVerifyTip() {
        return verifyTip;
    }

    public void setVerifyTip(String verifyTip) {
        this.verifyTip = verifyTip == null ? null : verifyTip.trim();
    }

    public String getResetType() {
        return resetType;
    }

    public void setResetType(String resetType) {
        this.resetType = resetType == null ? null : resetType.trim();
    }

    public String getSmsTemplate() {
        return smsTemplate;
    }

    public void setSmsTemplate(String smsTemplate) {
        this.smsTemplate = smsTemplate == null ? null : smsTemplate.trim();
    }

    public String getSmsReceiver() {
        return smsReceiver;
    }

    public void setSmsReceiver(String smsReceiver) {
        this.smsReceiver = smsReceiver == null ? null : smsReceiver.trim();
    }

    public String getResetUrl() {
        return resetUrl;
    }

    public void setResetUrl(String resetUrl) {
        this.resetUrl = resetUrl == null ? null : resetUrl.trim();
    }

    public String getResetTip() {
        return resetTip;
    }

    public void setResetTip(String resetTip) {
        this.resetTip = resetTip == null ? null : resetTip.trim();
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute == null ? null : attribute.trim();
    }
}