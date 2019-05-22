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

package com.datatrees.spider.share.domain.model;

import java.io.Serializable;
import java.util.Date;

/** create by system from table t_ecommerce(ecommerce basic info) */
public class Ecommerce implements Serializable {

    private static final long serialVersionUID = 1L;

    /** bank Id */
    private Integer id;

    /** not null if ecommerce support search */
    private Integer websiteId;

    /** ecommerce name */
    private String ecommerceName;

    /** 0:false,1:true */
    private Boolean isenabled;

    /**  */
    private Date createdAt;

    /**  */
    private Date updatedAt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getWebsiteId() {
        return websiteId;
    }

    public void setWebsiteId(Integer websiteId) {
        this.websiteId = websiteId;
    }

    public String getEcommerceName() {
        return ecommerceName;
    }

    public void setEcommerceName(String ecommerceName) {
        this.ecommerceName = ecommerceName == null ? null : ecommerceName.trim();
    }

    public Boolean getIsenabled() {
        return isenabled;
    }

    public void setIsenabled(Boolean isenabled) {
        this.isenabled = isenabled;
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
}