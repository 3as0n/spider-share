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

/** create by system from table t_keyword(keyword) */
public class Keyword implements Serializable {

    private static final long serialVersionUID = 1L;

    /**  */
    private Integer id;

    /** keyword */
    private String keyword;

    /** 0:false,1:true */
    private Boolean isenabled;

    /** 1:mail,2:operator,3:ecommerce，4:bank,5:internal */
    private Integer websiteType;

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

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword == null ? null : keyword.trim();
    }

    public Boolean getIsenabled() {
        return isenabled;
    }

    public void setIsenabled(Boolean isenabled) {
        this.isenabled = isenabled;
    }

    public Integer getWebsiteType() {
        return websiteType;
    }

    public void setWebsiteType(Integer websiteType) {
        this.websiteType = websiteType;
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