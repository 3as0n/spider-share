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

/**
 * User: yand Date: 2018/4/18
 */
public class ProjectParam {

    /**
     * 爬取业务标识 例"huabei"
     */
    private String code;

    /**
     * 爬取业务名称 例"花呗"
     */
    private String name;

    /**
     * 爬取状态
     */
    private Byte crawlerStatus;

    /**
     * 排序
     */
    private int order;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Byte getCrawlerStatus() {
        return crawlerStatus;
    }

    public void setCrawlerStatus(Byte crawlerStatus) {
        this.crawlerStatus = crawlerStatus;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
