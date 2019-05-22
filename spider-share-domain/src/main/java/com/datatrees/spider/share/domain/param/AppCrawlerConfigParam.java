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

package com.datatrees.spider.share.domain.param;

import com.alibaba.fastjson.JSON;

import java.util.List;

/**
 * User: yand Date: 2018/4/18
 */
public class AppCrawlerConfigParam {

    /**
     * 商户appid
     */
    private String appId;

    /**
     * 商户appName
     */
    private String appName;

    /**
     * 业务名称
     */
    private List<String> projectNames;

    /**
     * 具体业务info
     */
    private List<CrawlerProjectParam> projectConfigInfos;

    public AppCrawlerConfigParam() {}

    public AppCrawlerConfigParam(String appId, String appName) {
        this.appId = appId;
        this.appName = appName;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public List<String> getProjectNames() {
        return projectNames;
    }

    public void setProjectNames(List<String> projectNames) {
        this.projectNames = projectNames;
    }

    public List<CrawlerProjectParam> getProjectConfigInfos() {
        return projectConfigInfos;
    }

    public void setProjectConfigInfos(List<CrawlerProjectParam> projectConfigInfos) {
        this.projectConfigInfos = projectConfigInfos;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
