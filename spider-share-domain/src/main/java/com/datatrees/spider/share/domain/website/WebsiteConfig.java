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

package com.datatrees.spider.share.domain.website;

import java.util.Date;

/**
 * @author <A HREF="">Cheng Wang</A>
 * @version 1.0
 * @since 2016年11月7日 下午3:54:23
 */
public class WebsiteConfig {

    private static final long serialVersionUID = 1L;

    /**  */
    private Integer websiteId;

    /** 1:mail,2:operator,3:ecommerce，4:bank,5:internal */
    private String websiteType;

    /** website name */
    private String websiteName;

    /** 0:false,1:true */
    private Boolean isenabled;

    /** 登录过程中自定义提示 */
    private String loginTip;

    /** 验证码提示 */
    private String verifyTip;

    /** 重置类型：SMS|WEB */
    private String resetType;

    /** 短信模板 */
    private String smsTemplate;

    /** 短信接收方 */
    private String smsReceiver;

    /** 网页方式重置的url */
    private String resetURL;

    /** 密码重置提示 */
    private String resetTip;

    /**  */
    private Date createdAt;

    /**  */
    private Date updatedAt;

    /**  */
    private Boolean simulate;

    /**  */
    private Integer templateId;

    /**  */
    private Integer websiteConfId;

    /** extractor config */
    private String extractorConfig;

    /** search config */
    private String searchConfig;

    /**  */
    private String initSetting;

    /** 配置标题 */
    private String websiteTitle;

    /** 分组 */
    private String groupCode;

    /** 分组 */
    private String groupName;

    public static long getSerialVersionUID() {
        return serialVersionUID;
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

    public String getWebsiteTitle() {
        return websiteTitle;
    }

    public void setWebsiteTitle(String websiteTitle) {
        this.websiteTitle = websiteTitle;
    }

    public Integer getWebsiteId() {
        return websiteId;
    }

    public void setWebsiteId(Integer websiteId) {
        this.websiteId = websiteId;
    }

    public String getWebsiteType() {
        return websiteType;
    }

    public void setWebsiteType(String websiteType) {
        this.websiteType = websiteType;
    }

    public String getWebsiteName() {
        return websiteName;
    }

    public void setWebsiteName(String websiteName) {
        this.websiteName = websiteName;
    }

    public Boolean getIsenabled() {
        return isenabled;
    }

    public void setIsenabled(Boolean isenabled) {
        this.isenabled = isenabled;
    }

    public String getLoginTip() {
        return loginTip;
    }

    public void setLoginTip(String loginTip) {
        this.loginTip = loginTip;
    }

    public String getVerifyTip() {
        return verifyTip;
    }

    public void setVerifyTip(String verifyTip) {
        this.verifyTip = verifyTip;
    }

    public String getResetType() {
        return resetType;
    }

    public void setResetType(String resetType) {
        this.resetType = resetType;
    }

    public String getSmsTemplate() {
        return smsTemplate;
    }

    public void setSmsTemplate(String smsTemplate) {
        this.smsTemplate = smsTemplate;
    }

    public String getSmsReceiver() {
        return smsReceiver;
    }

    public void setSmsReceiver(String smsReceiver) {
        this.smsReceiver = smsReceiver;
    }

    public String getResetURL() {
        return resetURL;
    }

    public void setResetURL(String resetURL) {
        this.resetURL = resetURL;
    }

    public String getResetTip() {
        return resetTip;
    }

    public void setResetTip(String resetTip) {
        this.resetTip = resetTip;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Boolean getSimulate() {
        return simulate;
    }

    public void setSimulate(Boolean simulate) {
        this.simulate = simulate;
    }

    public Integer getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Integer templateId) {
        this.templateId = templateId;
    }

    public Integer getWebsiteConfId() {
        return websiteConfId;
    }

    public void setWebsiteConfId(Integer websiteConfId) {
        this.websiteConfId = websiteConfId;
    }

    public String getExtractorConfig() {
        return extractorConfig;
    }

    public void setExtractorConfig(String extractorConfig) {
        this.extractorConfig = extractorConfig;
    }

    public String getSearchConfig() {
        return searchConfig;
    }

    public void setSearchConfig(String searchConfig) {
        this.searchConfig = searchConfig;
    }

    public String getInitSetting() {
        return initSetting;
    }

    public void setInitSetting(String initSetting) {
        this.initSetting = initSetting;
    }
}
