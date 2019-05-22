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

package com.datatrees.spider.share.domain.param;

import com.alibaba.fastjson.JSON;

import java.util.List;

/**
 * User: yand Date: 2018/4/18
 */
public class CrawlerProjectParam {

    /**
     * 业务类型 {WebsiteType}
     */
    private Byte websiteType;

    /**
     * 具体业务
     */
    private List<ProjectParam> projects;

    public CrawlerProjectParam() {}

    public CrawlerProjectParam(Byte websiteType, List<ProjectParam> projects) {
        this.websiteType = websiteType;
        this.projects = projects;
    }

    public Byte getWebsiteType() {
        return websiteType;
    }

    public void setWebsiteType(Byte websiteType) {
        this.websiteType = websiteType;
    }

    public List<ProjectParam> getProjects() {
        return projects;
    }

    public void setProjects(List<ProjectParam> projects) {
        this.projects = projects;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
