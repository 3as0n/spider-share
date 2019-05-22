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

package com.datatrees.spider.share.domain.model.example;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class WebsiteInfoExample implements Serializable {
    private static final long serialVersionUID = 1L;

    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    /** 当前页 */
    protected int pageNum;

    /** 每页数据条数 */
    protected int pageSize;

    public WebsiteInfoExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    public void initPageInfo(Integer pageNum, Integer pageSize) {
        if (null == pageNum || pageNum <= 0) {
            this.pageNum = 1;
        } else {
            this.pageNum = pageNum.intValue();
        }
        if (null == pageSize || pageSize <= 0) {
            this.pageSize = 10;
        } else {
            this.pageSize = pageSize.intValue();
        }
    }

    public int getPageNum() {
        return pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public int getLimitStart() {
        if (pageNum > 0 && pageSize > 0) {
            return (pageNum - 1) * pageSize;
        }
        return 0;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        public Criteria andWebsiteIdIsNull() {
            addCriterion("website_id is null");
            return (Criteria)this;
        }

        public Criteria andWebsiteIdIsNotNull() {
            addCriterion("website_id is not null");
            return (Criteria)this;
        }

        public Criteria andWebsiteIdEqualTo(Integer value) {
            addCriterion("website_id =", value, "websiteId");
            return (Criteria)this;
        }

        public Criteria andWebsiteIdNotEqualTo(Integer value) {
            addCriterion("website_id <>", value, "websiteId");
            return (Criteria)this;
        }

        public Criteria andWebsiteIdGreaterThan(Integer value) {
            addCriterion("website_id >", value, "websiteId");
            return (Criteria)this;
        }

        public Criteria andWebsiteIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("website_id >=", value, "websiteId");
            return (Criteria)this;
        }

        public Criteria andWebsiteIdLessThan(Integer value) {
            addCriterion("website_id <", value, "websiteId");
            return (Criteria)this;
        }

        public Criteria andWebsiteIdLessThanOrEqualTo(Integer value) {
            addCriterion("website_id <=", value, "websiteId");
            return (Criteria)this;
        }

        public Criteria andWebsiteIdIn(List<Integer> values) {
            addCriterion("website_id in", values, "websiteId");
            return (Criteria)this;
        }

        public Criteria andWebsiteIdNotIn(List<Integer> values) {
            addCriterion("website_id not in", values, "websiteId");
            return (Criteria)this;
        }

        public Criteria andWebsiteIdBetween(Integer value1, Integer value2) {
            addCriterion("website_id between", value1, value2, "websiteId");
            return (Criteria)this;
        }

        public Criteria andWebsiteIdNotBetween(Integer value1, Integer value2) {
            addCriterion("website_id not between", value1, value2, "websiteId");
            return (Criteria)this;
        }

        public Criteria andEnvIsNull() {
            addCriterion("env is null");
            return (Criteria)this;
        }

        public Criteria andEnvIsNotNull() {
            addCriterion("env is not null");
            return (Criteria)this;
        }

        public Criteria andEnvEqualTo(String value) {
            addCriterion("env =", value, "env");
            return (Criteria)this;
        }

        public Criteria andEnvNotEqualTo(String value) {
            addCriterion("env <>", value, "env");
            return (Criteria)this;
        }

        public Criteria andEnvGreaterThan(String value) {
            addCriterion("env >", value, "env");
            return (Criteria)this;
        }

        public Criteria andEnvGreaterThanOrEqualTo(String value) {
            addCriterion("env >=", value, "env");
            return (Criteria)this;
        }

        public Criteria andEnvLessThan(String value) {
            addCriterion("env <", value, "env");
            return (Criteria)this;
        }

        public Criteria andEnvLessThanOrEqualTo(String value) {
            addCriterion("env <=", value, "env");
            return (Criteria)this;
        }

        public Criteria andEnvLike(String value) {
            addCriterion("env like", value, "env");
            return (Criteria)this;
        }

        public Criteria andEnvNotLike(String value) {
            addCriterion("env not like", value, "env");
            return (Criteria)this;
        }

        public Criteria andEnvIn(List<String> values) {
            addCriterion("env in", values, "env");
            return (Criteria)this;
        }

        public Criteria andEnvNotIn(List<String> values) {
            addCriterion("env not in", values, "env");
            return (Criteria)this;
        }

        public Criteria andEnvBetween(String value1, String value2) {
            addCriterion("env between", value1, value2, "env");
            return (Criteria)this;
        }

        public Criteria andEnvNotBetween(String value1, String value2) {
            addCriterion("env not between", value1, value2, "env");
            return (Criteria)this;
        }

        public Criteria andWebsiteTypeIsNull() {
            addCriterion("website_type is null");
            return (Criteria)this;
        }

        public Criteria andWebsiteTypeIsNotNull() {
            addCriterion("website_type is not null");
            return (Criteria)this;
        }

        public Criteria andWebsiteTypeEqualTo(Integer value) {
            addCriterion("website_type =", value, "websiteType");
            return (Criteria)this;
        }

        public Criteria andWebsiteTypeNotEqualTo(Integer value) {
            addCriterion("website_type <>", value, "websiteType");
            return (Criteria)this;
        }

        public Criteria andWebsiteTypeGreaterThan(Integer value) {
            addCriterion("website_type >", value, "websiteType");
            return (Criteria)this;
        }

        public Criteria andWebsiteTypeGreaterThanOrEqualTo(Integer value) {
            addCriterion("website_type >=", value, "websiteType");
            return (Criteria)this;
        }

        public Criteria andWebsiteTypeLessThan(Integer value) {
            addCriterion("website_type <", value, "websiteType");
            return (Criteria)this;
        }

        public Criteria andWebsiteTypeLessThanOrEqualTo(Integer value) {
            addCriterion("website_type <=", value, "websiteType");
            return (Criteria)this;
        }

        public Criteria andWebsiteTypeIn(List<Integer> values) {
            addCriterion("website_type in", values, "websiteType");
            return (Criteria)this;
        }

        public Criteria andWebsiteTypeNotIn(List<Integer> values) {
            addCriterion("website_type not in", values, "websiteType");
            return (Criteria)this;
        }

        public Criteria andWebsiteTypeBetween(Integer value1, Integer value2) {
            addCriterion("website_type between", value1, value2, "websiteType");
            return (Criteria)this;
        }

        public Criteria andWebsiteTypeNotBetween(Integer value1, Integer value2) {
            addCriterion("website_type not between", value1, value2, "websiteType");
            return (Criteria)this;
        }

        public Criteria andWebsiteNameIsNull() {
            addCriterion("website_name is null");
            return (Criteria)this;
        }

        public Criteria andWebsiteNameIsNotNull() {
            addCriterion("website_name is not null");
            return (Criteria)this;
        }

        public Criteria andWebsiteNameEqualTo(String value) {
            addCriterion("website_name =", value, "websiteName");
            return (Criteria)this;
        }

        public Criteria andWebsiteNameNotEqualTo(String value) {
            addCriterion("website_name <>", value, "websiteName");
            return (Criteria)this;
        }

        public Criteria andWebsiteNameGreaterThan(String value) {
            addCriterion("website_name >", value, "websiteName");
            return (Criteria)this;
        }

        public Criteria andWebsiteNameGreaterThanOrEqualTo(String value) {
            addCriterion("website_name >=", value, "websiteName");
            return (Criteria)this;
        }

        public Criteria andWebsiteNameLessThan(String value) {
            addCriterion("website_name <", value, "websiteName");
            return (Criteria)this;
        }

        public Criteria andWebsiteNameLessThanOrEqualTo(String value) {
            addCriterion("website_name <=", value, "websiteName");
            return (Criteria)this;
        }

        public Criteria andWebsiteNameLike(String value) {
            addCriterion("website_name like", value, "websiteName");
            return (Criteria)this;
        }

        public Criteria andWebsiteNameNotLike(String value) {
            addCriterion("website_name not like", value, "websiteName");
            return (Criteria)this;
        }

        public Criteria andWebsiteNameIn(List<String> values) {
            addCriterion("website_name in", values, "websiteName");
            return (Criteria)this;
        }

        public Criteria andWebsiteNameNotIn(List<String> values) {
            addCriterion("website_name not in", values, "websiteName");
            return (Criteria)this;
        }

        public Criteria andWebsiteNameBetween(String value1, String value2) {
            addCriterion("website_name between", value1, value2, "websiteName");
            return (Criteria)this;
        }

        public Criteria andWebsiteNameNotBetween(String value1, String value2) {
            addCriterion("website_name not between", value1, value2, "websiteName");
            return (Criteria)this;
        }

        public Criteria andWebsiteTitleIsNull() {
            addCriterion("website_title is null");
            return (Criteria)this;
        }

        public Criteria andWebsiteTitleIsNotNull() {
            addCriterion("website_title is not null");
            return (Criteria)this;
        }

        public Criteria andWebsiteTitleEqualTo(String value) {
            addCriterion("website_title =", value, "websiteTitle");
            return (Criteria)this;
        }

        public Criteria andWebsiteTitleNotEqualTo(String value) {
            addCriterion("website_title <>", value, "websiteTitle");
            return (Criteria)this;
        }

        public Criteria andWebsiteTitleGreaterThan(String value) {
            addCriterion("website_title >", value, "websiteTitle");
            return (Criteria)this;
        }

        public Criteria andWebsiteTitleGreaterThanOrEqualTo(String value) {
            addCriterion("website_title >=", value, "websiteTitle");
            return (Criteria)this;
        }

        public Criteria andWebsiteTitleLessThan(String value) {
            addCriterion("website_title <", value, "websiteTitle");
            return (Criteria)this;
        }

        public Criteria andWebsiteTitleLessThanOrEqualTo(String value) {
            addCriterion("website_title <=", value, "websiteTitle");
            return (Criteria)this;
        }

        public Criteria andWebsiteTitleLike(String value) {
            addCriterion("website_title like", value, "websiteTitle");
            return (Criteria)this;
        }

        public Criteria andWebsiteTitleNotLike(String value) {
            addCriterion("website_title not like", value, "websiteTitle");
            return (Criteria)this;
        }

        public Criteria andWebsiteTitleIn(List<String> values) {
            addCriterion("website_title in", values, "websiteTitle");
            return (Criteria)this;
        }

        public Criteria andWebsiteTitleNotIn(List<String> values) {
            addCriterion("website_title not in", values, "websiteTitle");
            return (Criteria)this;
        }

        public Criteria andWebsiteTitleBetween(String value1, String value2) {
            addCriterion("website_title between", value1, value2, "websiteTitle");
            return (Criteria)this;
        }

        public Criteria andWebsiteTitleNotBetween(String value1, String value2) {
            addCriterion("website_title not between", value1, value2, "websiteTitle");
            return (Criteria)this;
        }

        public Criteria andEnableIsNull() {
            addCriterion("enable is null");
            return (Criteria)this;
        }

        public Criteria andEnableIsNotNull() {
            addCriterion("enable is not null");
            return (Criteria)this;
        }

        public Criteria andEnableEqualTo(Boolean value) {
            addCriterion("enable =", value, "enable");
            return (Criteria)this;
        }

        public Criteria andEnableNotEqualTo(Boolean value) {
            addCriterion("enable <>", value, "enable");
            return (Criteria)this;
        }

        public Criteria andEnableGreaterThan(Boolean value) {
            addCriterion("enable >", value, "enable");
            return (Criteria)this;
        }

        public Criteria andEnableGreaterThanOrEqualTo(Boolean value) {
            addCriterion("enable >=", value, "enable");
            return (Criteria)this;
        }

        public Criteria andEnableLessThan(Boolean value) {
            addCriterion("enable <", value, "enable");
            return (Criteria)this;
        }

        public Criteria andEnableLessThanOrEqualTo(Boolean value) {
            addCriterion("enable <=", value, "enable");
            return (Criteria)this;
        }

        public Criteria andEnableIn(List<Boolean> values) {
            addCriterion("enable in", values, "enable");
            return (Criteria)this;
        }

        public Criteria andEnableNotIn(List<Boolean> values) {
            addCriterion("enable not in", values, "enable");
            return (Criteria)this;
        }

        public Criteria andEnableBetween(Boolean value1, Boolean value2) {
            addCriterion("enable between", value1, value2, "enable");
            return (Criteria)this;
        }

        public Criteria andEnableNotBetween(Boolean value1, Boolean value2) {
            addCriterion("enable not between", value1, value2, "enable");
            return (Criteria)this;
        }

        public Criteria andProxyEnableIsNull() {
            addCriterion("proxy_enable is null");
            return (Criteria)this;
        }

        public Criteria andProxyEnableIsNotNull() {
            addCriterion("proxy_enable is not null");
            return (Criteria)this;
        }

        public Criteria andProxyEnableEqualTo(Boolean value) {
            addCriterion("proxy_enable =", value, "proxyEnable");
            return (Criteria)this;
        }

        public Criteria andProxyEnableNotEqualTo(Boolean value) {
            addCriterion("proxy_enable <>", value, "proxyEnable");
            return (Criteria)this;
        }

        public Criteria andProxyEnableGreaterThan(Boolean value) {
            addCriterion("proxy_enable >", value, "proxyEnable");
            return (Criteria)this;
        }

        public Criteria andProxyEnableGreaterThanOrEqualTo(Boolean value) {
            addCriterion("proxy_enable >=", value, "proxyEnable");
            return (Criteria)this;
        }

        public Criteria andProxyEnableLessThan(Boolean value) {
            addCriterion("proxy_enable <", value, "proxyEnable");
            return (Criteria)this;
        }

        public Criteria andProxyEnableLessThanOrEqualTo(Boolean value) {
            addCriterion("proxy_enable <=", value, "proxyEnable");
            return (Criteria)this;
        }

        public Criteria andProxyEnableIn(List<Boolean> values) {
            addCriterion("proxy_enable in", values, "proxyEnable");
            return (Criteria)this;
        }

        public Criteria andProxyEnableNotIn(List<Boolean> values) {
            addCriterion("proxy_enable not in", values, "proxyEnable");
            return (Criteria)this;
        }

        public Criteria andProxyEnableBetween(Boolean value1, Boolean value2) {
            addCriterion("proxy_enable between", value1, value2, "proxyEnable");
            return (Criteria)this;
        }

        public Criteria andProxyEnableNotBetween(Boolean value1, Boolean value2) {
            addCriterion("proxy_enable not between", value1, value2, "proxyEnable");
            return (Criteria)this;
        }

        public Criteria andSearchConfigIsNull() {
            addCriterion("search_config is null");
            return (Criteria)this;
        }

        public Criteria andSearchConfigIsNotNull() {
            addCriterion("search_config is not null");
            return (Criteria)this;
        }

        public Criteria andSearchConfigEqualTo(String value) {
            addCriterion("search_config =", value, "searchConfig");
            return (Criteria)this;
        }

        public Criteria andSearchConfigNotEqualTo(String value) {
            addCriterion("search_config <>", value, "searchConfig");
            return (Criteria)this;
        }

        public Criteria andSearchConfigGreaterThan(String value) {
            addCriterion("search_config >", value, "searchConfig");
            return (Criteria)this;
        }

        public Criteria andSearchConfigGreaterThanOrEqualTo(String value) {
            addCriterion("search_config >=", value, "searchConfig");
            return (Criteria)this;
        }

        public Criteria andSearchConfigLessThan(String value) {
            addCriterion("search_config <", value, "searchConfig");
            return (Criteria)this;
        }

        public Criteria andSearchConfigLessThanOrEqualTo(String value) {
            addCriterion("search_config <=", value, "searchConfig");
            return (Criteria)this;
        }

        public Criteria andSearchConfigLike(String value) {
            addCriterion("search_config like", value, "searchConfig");
            return (Criteria)this;
        }

        public Criteria andSearchConfigNotLike(String value) {
            addCriterion("search_config not like", value, "searchConfig");
            return (Criteria)this;
        }

        public Criteria andSearchConfigIn(List<String> values) {
            addCriterion("search_config in", values, "searchConfig");
            return (Criteria)this;
        }

        public Criteria andSearchConfigNotIn(List<String> values) {
            addCriterion("search_config not in", values, "searchConfig");
            return (Criteria)this;
        }

        public Criteria andSearchConfigBetween(String value1, String value2) {
            addCriterion("search_config between", value1, value2, "searchConfig");
            return (Criteria)this;
        }

        public Criteria andSearchConfigNotBetween(String value1, String value2) {
            addCriterion("search_config not between", value1, value2, "searchConfig");
            return (Criteria)this;
        }

        public Criteria andExtractorConfigIsNull() {
            addCriterion("extractor_config is null");
            return (Criteria)this;
        }

        public Criteria andExtractorConfigIsNotNull() {
            addCriterion("extractor_config is not null");
            return (Criteria)this;
        }

        public Criteria andExtractorConfigEqualTo(String value) {
            addCriterion("extractor_config =", value, "extractorConfig");
            return (Criteria)this;
        }

        public Criteria andExtractorConfigNotEqualTo(String value) {
            addCriterion("extractor_config <>", value, "extractorConfig");
            return (Criteria)this;
        }

        public Criteria andExtractorConfigGreaterThan(String value) {
            addCriterion("extractor_config >", value, "extractorConfig");
            return (Criteria)this;
        }

        public Criteria andExtractorConfigGreaterThanOrEqualTo(String value) {
            addCriterion("extractor_config >=", value, "extractorConfig");
            return (Criteria)this;
        }

        public Criteria andExtractorConfigLessThan(String value) {
            addCriterion("extractor_config <", value, "extractorConfig");
            return (Criteria)this;
        }

        public Criteria andExtractorConfigLessThanOrEqualTo(String value) {
            addCriterion("extractor_config <=", value, "extractorConfig");
            return (Criteria)this;
        }

        public Criteria andExtractorConfigLike(String value) {
            addCriterion("extractor_config like", value, "extractorConfig");
            return (Criteria)this;
        }

        public Criteria andExtractorConfigNotLike(String value) {
            addCriterion("extractor_config not like", value, "extractorConfig");
            return (Criteria)this;
        }

        public Criteria andExtractorConfigIn(List<String> values) {
            addCriterion("extractor_config in", values, "extractorConfig");
            return (Criteria)this;
        }

        public Criteria andExtractorConfigNotIn(List<String> values) {
            addCriterion("extractor_config not in", values, "extractorConfig");
            return (Criteria)this;
        }

        public Criteria andExtractorConfigBetween(String value1, String value2) {
            addCriterion("extractor_config between", value1, value2, "extractorConfig");
            return (Criteria)this;
        }

        public Criteria andExtractorConfigNotBetween(String value1, String value2) {
            addCriterion("extractor_config not between", value1, value2, "extractorConfig");
            return (Criteria)this;
        }

        public Criteria andGroupCodeIsNull() {
            addCriterion("group_code is null");
            return (Criteria)this;
        }

        public Criteria andGroupCodeIsNotNull() {
            addCriterion("group_code is not null");
            return (Criteria)this;
        }

        public Criteria andGroupCodeEqualTo(String value) {
            addCriterion("group_code =", value, "groupCode");
            return (Criteria)this;
        }

        public Criteria andGroupCodeNotEqualTo(String value) {
            addCriterion("group_code <>", value, "groupCode");
            return (Criteria)this;
        }

        public Criteria andGroupCodeGreaterThan(String value) {
            addCriterion("group_code >", value, "groupCode");
            return (Criteria)this;
        }

        public Criteria andGroupCodeGreaterThanOrEqualTo(String value) {
            addCriterion("group_code >=", value, "groupCode");
            return (Criteria)this;
        }

        public Criteria andGroupCodeLessThan(String value) {
            addCriterion("group_code <", value, "groupCode");
            return (Criteria)this;
        }

        public Criteria andGroupCodeLessThanOrEqualTo(String value) {
            addCriterion("group_code <=", value, "groupCode");
            return (Criteria)this;
        }

        public Criteria andGroupCodeLike(String value) {
            addCriterion("group_code like", value, "groupCode");
            return (Criteria)this;
        }

        public Criteria andGroupCodeNotLike(String value) {
            addCriterion("group_code not like", value, "groupCode");
            return (Criteria)this;
        }

        public Criteria andGroupCodeIn(List<String> values) {
            addCriterion("group_code in", values, "groupCode");
            return (Criteria)this;
        }

        public Criteria andGroupCodeNotIn(List<String> values) {
            addCriterion("group_code not in", values, "groupCode");
            return (Criteria)this;
        }

        public Criteria andGroupCodeBetween(String value1, String value2) {
            addCriterion("group_code between", value1, value2, "groupCode");
            return (Criteria)this;
        }

        public Criteria andGroupCodeNotBetween(String value1, String value2) {
            addCriterion("group_code not between", value1, value2, "groupCode");
            return (Criteria)this;
        }

        public Criteria andUpdatedAtIsNull() {
            addCriterion("updated_at is null");
            return (Criteria)this;
        }

        public Criteria andUpdatedAtIsNotNull() {
            addCriterion("updated_at is not null");
            return (Criteria)this;
        }

        public Criteria andUpdatedAtEqualTo(Date value) {
            addCriterion("updated_at =", value, "updatedAt");
            return (Criteria)this;
        }

        public Criteria andUpdatedAtNotEqualTo(Date value) {
            addCriterion("updated_at <>", value, "updatedAt");
            return (Criteria)this;
        }

        public Criteria andUpdatedAtGreaterThan(Date value) {
            addCriterion("updated_at >", value, "updatedAt");
            return (Criteria)this;
        }

        public Criteria andUpdatedAtGreaterThanOrEqualTo(Date value) {
            addCriterion("updated_at >=", value, "updatedAt");
            return (Criteria)this;
        }

        public Criteria andUpdatedAtLessThan(Date value) {
            addCriterion("updated_at <", value, "updatedAt");
            return (Criteria)this;
        }

        public Criteria andUpdatedAtLessThanOrEqualTo(Date value) {
            addCriterion("updated_at <=", value, "updatedAt");
            return (Criteria)this;
        }

        public Criteria andUpdatedAtIn(List<Date> values) {
            addCriterion("updated_at in", values, "updatedAt");
            return (Criteria)this;
        }

        public Criteria andUpdatedAtNotIn(List<Date> values) {
            addCriterion("updated_at not in", values, "updatedAt");
            return (Criteria)this;
        }

        public Criteria andUpdatedAtBetween(Date value1, Date value2) {
            addCriterion("updated_at between", value1, value2, "updatedAt");
            return (Criteria)this;
        }

        public Criteria andUpdatedAtNotBetween(Date value1, Date value2) {
            addCriterion("updated_at not between", value1, value2, "updatedAt");
            return (Criteria)this;
        }

        public Criteria andStartStageIsNull() {
            addCriterion("start_stage is null");
            return (Criteria)this;
        }

        public Criteria andStartStageIsNotNull() {
            addCriterion("start_stage is not null");
            return (Criteria)this;
        }

        public Criteria andStartStageEqualTo(String value) {
            addCriterion("start_stage =", value, "startStage");
            return (Criteria)this;
        }

        public Criteria andStartStageNotEqualTo(String value) {
            addCriterion("start_stage <>", value, "startStage");
            return (Criteria)this;
        }

        public Criteria andStartStageGreaterThan(String value) {
            addCriterion("start_stage >", value, "startStage");
            return (Criteria)this;
        }

        public Criteria andStartStageGreaterThanOrEqualTo(String value) {
            addCriterion("start_stage >=", value, "startStage");
            return (Criteria)this;
        }

        public Criteria andStartStageLessThan(String value) {
            addCriterion("start_stage <", value, "startStage");
            return (Criteria)this;
        }

        public Criteria andStartStageLessThanOrEqualTo(String value) {
            addCriterion("start_stage <=", value, "startStage");
            return (Criteria)this;
        }

        public Criteria andStartStageLike(String value) {
            addCriterion("start_stage like", value, "startStage");
            return (Criteria)this;
        }

        public Criteria andStartStageNotLike(String value) {
            addCriterion("start_stage not like", value, "startStage");
            return (Criteria)this;
        }

        public Criteria andStartStageIn(List<String> values) {
            addCriterion("start_stage in", values, "startStage");
            return (Criteria)this;
        }

        public Criteria andStartStageNotIn(List<String> values) {
            addCriterion("start_stage not in", values, "startStage");
            return (Criteria)this;
        }

        public Criteria andStartStageBetween(String value1, String value2) {
            addCriterion("start_stage between", value1, value2, "startStage");
            return (Criteria)this;
        }

        public Criteria andStartStageNotBetween(String value1, String value2) {
            addCriterion("start_stage not between", value1, value2, "startStage");
            return (Criteria)this;
        }

        public Criteria andLoginUrlIsNull() {
            addCriterion("login_url is null");
            return (Criteria)this;
        }

        public Criteria andLoginUrlIsNotNull() {
            addCriterion("login_url is not null");
            return (Criteria)this;
        }

        public Criteria andLoginUrlEqualTo(String value) {
            addCriterion("login_url =", value, "loginUrl");
            return (Criteria)this;
        }

        public Criteria andLoginUrlNotEqualTo(String value) {
            addCriterion("login_url <>", value, "loginUrl");
            return (Criteria)this;
        }

        public Criteria andLoginUrlGreaterThan(String value) {
            addCriterion("login_url >", value, "loginUrl");
            return (Criteria)this;
        }

        public Criteria andLoginUrlGreaterThanOrEqualTo(String value) {
            addCriterion("login_url >=", value, "loginUrl");
            return (Criteria)this;
        }

        public Criteria andLoginUrlLessThan(String value) {
            addCriterion("login_url <", value, "loginUrl");
            return (Criteria)this;
        }

        public Criteria andLoginUrlLessThanOrEqualTo(String value) {
            addCriterion("login_url <=", value, "loginUrl");
            return (Criteria)this;
        }

        public Criteria andLoginUrlLike(String value) {
            addCriterion("login_url like", value, "loginUrl");
            return (Criteria)this;
        }

        public Criteria andLoginUrlNotLike(String value) {
            addCriterion("login_url not like", value, "loginUrl");
            return (Criteria)this;
        }

        public Criteria andLoginUrlIn(List<String> values) {
            addCriterion("login_url in", values, "loginUrl");
            return (Criteria)this;
        }

        public Criteria andLoginUrlNotIn(List<String> values) {
            addCriterion("login_url not in", values, "loginUrl");
            return (Criteria)this;
        }

        public Criteria andLoginUrlBetween(String value1, String value2) {
            addCriterion("login_url between", value1, value2, "loginUrl");
            return (Criteria)this;
        }

        public Criteria andLoginUrlNotBetween(String value1, String value2) {
            addCriterion("login_url not between", value1, value2, "loginUrl");
            return (Criteria)this;
        }

        public Criteria andPluginClassIsNull() {
            addCriterion("plugin_class is null");
            return (Criteria)this;
        }

        public Criteria andPluginClassIsNotNull() {
            addCriterion("plugin_class is not null");
            return (Criteria)this;
        }

        public Criteria andPluginClassEqualTo(String value) {
            addCriterion("plugin_class =", value, "pluginClass");
            return (Criteria)this;
        }

        public Criteria andPluginClassNotEqualTo(String value) {
            addCriterion("plugin_class <>", value, "pluginClass");
            return (Criteria)this;
        }

        public Criteria andPluginClassGreaterThan(String value) {
            addCriterion("plugin_class >", value, "pluginClass");
            return (Criteria)this;
        }

        public Criteria andPluginClassGreaterThanOrEqualTo(String value) {
            addCriterion("plugin_class >=", value, "pluginClass");
            return (Criteria)this;
        }

        public Criteria andPluginClassLessThan(String value) {
            addCriterion("plugin_class <", value, "pluginClass");
            return (Criteria)this;
        }

        public Criteria andPluginClassLessThanOrEqualTo(String value) {
            addCriterion("plugin_class <=", value, "pluginClass");
            return (Criteria)this;
        }

        public Criteria andPluginClassLike(String value) {
            addCriterion("plugin_class like", value, "pluginClass");
            return (Criteria)this;
        }

        public Criteria andPluginClassNotLike(String value) {
            addCriterion("plugin_class not like", value, "pluginClass");
            return (Criteria)this;
        }

        public Criteria andPluginClassIn(List<String> values) {
            addCriterion("plugin_class in", values, "pluginClass");
            return (Criteria)this;
        }

        public Criteria andPluginClassNotIn(List<String> values) {
            addCriterion("plugin_class not in", values, "pluginClass");
            return (Criteria)this;
        }

        public Criteria andPluginClassBetween(String value1, String value2) {
            addCriterion("plugin_class between", value1, value2, "pluginClass");
            return (Criteria)this;
        }

        public Criteria andPluginClassNotBetween(String value1, String value2) {
            addCriterion("plugin_class not between", value1, value2, "pluginClass");
            return (Criteria)this;
        }

        public Criteria andLoginConfigIsNull() {
            addCriterion("login_config is null");
            return (Criteria)this;
        }

        public Criteria andLoginConfigIsNotNull() {
            addCriterion("login_config is not null");
            return (Criteria)this;
        }

        public Criteria andLoginConfigEqualTo(String value) {
            addCriterion("login_config =", value, "loginConfig");
            return (Criteria)this;
        }

        public Criteria andLoginConfigNotEqualTo(String value) {
            addCriterion("login_config <>", value, "loginConfig");
            return (Criteria)this;
        }

        public Criteria andLoginConfigGreaterThan(String value) {
            addCriterion("login_config >", value, "loginConfig");
            return (Criteria)this;
        }

        public Criteria andLoginConfigGreaterThanOrEqualTo(String value) {
            addCriterion("login_config >=", value, "loginConfig");
            return (Criteria)this;
        }

        public Criteria andLoginConfigLessThan(String value) {
            addCriterion("login_config <", value, "loginConfig");
            return (Criteria)this;
        }

        public Criteria andLoginConfigLessThanOrEqualTo(String value) {
            addCriterion("login_config <=", value, "loginConfig");
            return (Criteria)this;
        }

        public Criteria andLoginConfigLike(String value) {
            addCriterion("login_config like", value, "loginConfig");
            return (Criteria)this;
        }

        public Criteria andLoginConfigNotLike(String value) {
            addCriterion("login_config not like", value, "loginConfig");
            return (Criteria)this;
        }

        public Criteria andLoginConfigIn(List<String> values) {
            addCriterion("login_config in", values, "loginConfig");
            return (Criteria)this;
        }

        public Criteria andLoginConfigNotIn(List<String> values) {
            addCriterion("login_config not in", values, "loginConfig");
            return (Criteria)this;
        }

        public Criteria andLoginConfigBetween(String value1, String value2) {
            addCriterion("login_config between", value1, value2, "loginConfig");
            return (Criteria)this;
        }

        public Criteria andLoginConfigNotBetween(String value1, String value2) {
            addCriterion("login_config not between", value1, value2, "loginConfig");
            return (Criteria)this;
        }

        public Criteria andSmsIntervalIsNull() {
            addCriterion("sms_interval is null");
            return (Criteria)this;
        }

        public Criteria andSmsIntervalIsNotNull() {
            addCriterion("sms_interval is not null");
            return (Criteria)this;
        }

        public Criteria andSmsIntervalEqualTo(Integer value) {
            addCriterion("sms_interval =", value, "smsInterval");
            return (Criteria)this;
        }

        public Criteria andSmsIntervalNotEqualTo(Integer value) {
            addCriterion("sms_interval <>", value, "smsInterval");
            return (Criteria)this;
        }

        public Criteria andSmsIntervalGreaterThan(Integer value) {
            addCriterion("sms_interval >", value, "smsInterval");
            return (Criteria)this;
        }

        public Criteria andSmsIntervalGreaterThanOrEqualTo(Integer value) {
            addCriterion("sms_interval >=", value, "smsInterval");
            return (Criteria)this;
        }

        public Criteria andSmsIntervalLessThan(Integer value) {
            addCriterion("sms_interval <", value, "smsInterval");
            return (Criteria)this;
        }

        public Criteria andSmsIntervalLessThanOrEqualTo(Integer value) {
            addCriterion("sms_interval <=", value, "smsInterval");
            return (Criteria)this;
        }

        public Criteria andSmsIntervalIn(List<Integer> values) {
            addCriterion("sms_interval in", values, "smsInterval");
            return (Criteria)this;
        }

        public Criteria andSmsIntervalNotIn(List<Integer> values) {
            addCriterion("sms_interval not in", values, "smsInterval");
            return (Criteria)this;
        }

        public Criteria andSmsIntervalBetween(Integer value1, Integer value2) {
            addCriterion("sms_interval between", value1, value2, "smsInterval");
            return (Criteria)this;
        }

        public Criteria andSmsIntervalNotBetween(Integer value1, Integer value2) {
            addCriterion("sms_interval not between", value1, value2, "smsInterval");
            return (Criteria)this;
        }

        public Criteria andRemarkIsNull() {
            addCriterion("remark is null");
            return (Criteria)this;
        }

        public Criteria andRemarkIsNotNull() {
            addCriterion("remark is not null");
            return (Criteria)this;
        }

        public Criteria andRemarkEqualTo(String value) {
            addCriterion("remark =", value, "remark");
            return (Criteria)this;
        }

        public Criteria andRemarkNotEqualTo(String value) {
            addCriterion("remark <>", value, "remark");
            return (Criteria)this;
        }

        public Criteria andRemarkGreaterThan(String value) {
            addCriterion("remark >", value, "remark");
            return (Criteria)this;
        }

        public Criteria andRemarkGreaterThanOrEqualTo(String value) {
            addCriterion("remark >=", value, "remark");
            return (Criteria)this;
        }

        public Criteria andRemarkLessThan(String value) {
            addCriterion("remark <", value, "remark");
            return (Criteria)this;
        }

        public Criteria andRemarkLessThanOrEqualTo(String value) {
            addCriterion("remark <=", value, "remark");
            return (Criteria)this;
        }

        public Criteria andRemarkLike(String value) {
            addCriterion("remark like", value, "remark");
            return (Criteria)this;
        }

        public Criteria andRemarkNotLike(String value) {
            addCriterion("remark not like", value, "remark");
            return (Criteria)this;
        }

        public Criteria andRemarkIn(List<String> values) {
            addCriterion("remark in", values, "remark");
            return (Criteria)this;
        }

        public Criteria andRemarkNotIn(List<String> values) {
            addCriterion("remark not in", values, "remark");
            return (Criteria)this;
        }

        public Criteria andRemarkBetween(String value1, String value2) {
            addCriterion("remark between", value1, value2, "remark");
            return (Criteria)this;
        }

        public Criteria andRemarkNotBetween(String value1, String value2) {
            addCriterion("remark not between", value1, value2, "remark");
            return (Criteria)this;
        }

        public Criteria andLoginTipIsNull() {
            addCriterion("login_tip is null");
            return (Criteria)this;
        }

        public Criteria andLoginTipIsNotNull() {
            addCriterion("login_tip is not null");
            return (Criteria)this;
        }

        public Criteria andLoginTipEqualTo(String value) {
            addCriterion("login_tip =", value, "loginTip");
            return (Criteria)this;
        }

        public Criteria andLoginTipNotEqualTo(String value) {
            addCriterion("login_tip <>", value, "loginTip");
            return (Criteria)this;
        }

        public Criteria andLoginTipGreaterThan(String value) {
            addCriterion("login_tip >", value, "loginTip");
            return (Criteria)this;
        }

        public Criteria andLoginTipGreaterThanOrEqualTo(String value) {
            addCriterion("login_tip >=", value, "loginTip");
            return (Criteria)this;
        }

        public Criteria andLoginTipLessThan(String value) {
            addCriterion("login_tip <", value, "loginTip");
            return (Criteria)this;
        }

        public Criteria andLoginTipLessThanOrEqualTo(String value) {
            addCriterion("login_tip <=", value, "loginTip");
            return (Criteria)this;
        }

        public Criteria andLoginTipLike(String value) {
            addCriterion("login_tip like", value, "loginTip");
            return (Criteria)this;
        }

        public Criteria andLoginTipNotLike(String value) {
            addCriterion("login_tip not like", value, "loginTip");
            return (Criteria)this;
        }

        public Criteria andLoginTipIn(List<String> values) {
            addCriterion("login_tip in", values, "loginTip");
            return (Criteria)this;
        }

        public Criteria andLoginTipNotIn(List<String> values) {
            addCriterion("login_tip not in", values, "loginTip");
            return (Criteria)this;
        }

        public Criteria andLoginTipBetween(String value1, String value2) {
            addCriterion("login_tip between", value1, value2, "loginTip");
            return (Criteria)this;
        }

        public Criteria andLoginTipNotBetween(String value1, String value2) {
            addCriterion("login_tip not between", value1, value2, "loginTip");
            return (Criteria)this;
        }

        public Criteria andVerifyTipIsNull() {
            addCriterion("verify_tip is null");
            return (Criteria)this;
        }

        public Criteria andVerifyTipIsNotNull() {
            addCriterion("verify_tip is not null");
            return (Criteria)this;
        }

        public Criteria andVerifyTipEqualTo(String value) {
            addCriterion("verify_tip =", value, "verifyTip");
            return (Criteria)this;
        }

        public Criteria andVerifyTipNotEqualTo(String value) {
            addCriterion("verify_tip <>", value, "verifyTip");
            return (Criteria)this;
        }

        public Criteria andVerifyTipGreaterThan(String value) {
            addCriterion("verify_tip >", value, "verifyTip");
            return (Criteria)this;
        }

        public Criteria andVerifyTipGreaterThanOrEqualTo(String value) {
            addCriterion("verify_tip >=", value, "verifyTip");
            return (Criteria)this;
        }

        public Criteria andVerifyTipLessThan(String value) {
            addCriterion("verify_tip <", value, "verifyTip");
            return (Criteria)this;
        }

        public Criteria andVerifyTipLessThanOrEqualTo(String value) {
            addCriterion("verify_tip <=", value, "verifyTip");
            return (Criteria)this;
        }

        public Criteria andVerifyTipLike(String value) {
            addCriterion("verify_tip like", value, "verifyTip");
            return (Criteria)this;
        }

        public Criteria andVerifyTipNotLike(String value) {
            addCriterion("verify_tip not like", value, "verifyTip");
            return (Criteria)this;
        }

        public Criteria andVerifyTipIn(List<String> values) {
            addCriterion("verify_tip in", values, "verifyTip");
            return (Criteria)this;
        }

        public Criteria andVerifyTipNotIn(List<String> values) {
            addCriterion("verify_tip not in", values, "verifyTip");
            return (Criteria)this;
        }

        public Criteria andVerifyTipBetween(String value1, String value2) {
            addCriterion("verify_tip between", value1, value2, "verifyTip");
            return (Criteria)this;
        }

        public Criteria andVerifyTipNotBetween(String value1, String value2) {
            addCriterion("verify_tip not between", value1, value2, "verifyTip");
            return (Criteria)this;
        }

        public Criteria andResetTypeIsNull() {
            addCriterion("reset_type is null");
            return (Criteria)this;
        }

        public Criteria andResetTypeIsNotNull() {
            addCriterion("reset_type is not null");
            return (Criteria)this;
        }

        public Criteria andResetTypeEqualTo(String value) {
            addCriterion("reset_type =", value, "resetType");
            return (Criteria)this;
        }

        public Criteria andResetTypeNotEqualTo(String value) {
            addCriterion("reset_type <>", value, "resetType");
            return (Criteria)this;
        }

        public Criteria andResetTypeGreaterThan(String value) {
            addCriterion("reset_type >", value, "resetType");
            return (Criteria)this;
        }

        public Criteria andResetTypeGreaterThanOrEqualTo(String value) {
            addCriterion("reset_type >=", value, "resetType");
            return (Criteria)this;
        }

        public Criteria andResetTypeLessThan(String value) {
            addCriterion("reset_type <", value, "resetType");
            return (Criteria)this;
        }

        public Criteria andResetTypeLessThanOrEqualTo(String value) {
            addCriterion("reset_type <=", value, "resetType");
            return (Criteria)this;
        }

        public Criteria andResetTypeLike(String value) {
            addCriterion("reset_type like", value, "resetType");
            return (Criteria)this;
        }

        public Criteria andResetTypeNotLike(String value) {
            addCriterion("reset_type not like", value, "resetType");
            return (Criteria)this;
        }

        public Criteria andResetTypeIn(List<String> values) {
            addCriterion("reset_type in", values, "resetType");
            return (Criteria)this;
        }

        public Criteria andResetTypeNotIn(List<String> values) {
            addCriterion("reset_type not in", values, "resetType");
            return (Criteria)this;
        }

        public Criteria andResetTypeBetween(String value1, String value2) {
            addCriterion("reset_type between", value1, value2, "resetType");
            return (Criteria)this;
        }

        public Criteria andResetTypeNotBetween(String value1, String value2) {
            addCriterion("reset_type not between", value1, value2, "resetType");
            return (Criteria)this;
        }

        public Criteria andSmsTemplateIsNull() {
            addCriterion("sms_template is null");
            return (Criteria)this;
        }

        public Criteria andSmsTemplateIsNotNull() {
            addCriterion("sms_template is not null");
            return (Criteria)this;
        }

        public Criteria andSmsTemplateEqualTo(String value) {
            addCriterion("sms_template =", value, "smsTemplate");
            return (Criteria)this;
        }

        public Criteria andSmsTemplateNotEqualTo(String value) {
            addCriterion("sms_template <>", value, "smsTemplate");
            return (Criteria)this;
        }

        public Criteria andSmsTemplateGreaterThan(String value) {
            addCriterion("sms_template >", value, "smsTemplate");
            return (Criteria)this;
        }

        public Criteria andSmsTemplateGreaterThanOrEqualTo(String value) {
            addCriterion("sms_template >=", value, "smsTemplate");
            return (Criteria)this;
        }

        public Criteria andSmsTemplateLessThan(String value) {
            addCriterion("sms_template <", value, "smsTemplate");
            return (Criteria)this;
        }

        public Criteria andSmsTemplateLessThanOrEqualTo(String value) {
            addCriterion("sms_template <=", value, "smsTemplate");
            return (Criteria)this;
        }

        public Criteria andSmsTemplateLike(String value) {
            addCriterion("sms_template like", value, "smsTemplate");
            return (Criteria)this;
        }

        public Criteria andSmsTemplateNotLike(String value) {
            addCriterion("sms_template not like", value, "smsTemplate");
            return (Criteria)this;
        }

        public Criteria andSmsTemplateIn(List<String> values) {
            addCriterion("sms_template in", values, "smsTemplate");
            return (Criteria)this;
        }

        public Criteria andSmsTemplateNotIn(List<String> values) {
            addCriterion("sms_template not in", values, "smsTemplate");
            return (Criteria)this;
        }

        public Criteria andSmsTemplateBetween(String value1, String value2) {
            addCriterion("sms_template between", value1, value2, "smsTemplate");
            return (Criteria)this;
        }

        public Criteria andSmsTemplateNotBetween(String value1, String value2) {
            addCriterion("sms_template not between", value1, value2, "smsTemplate");
            return (Criteria)this;
        }

        public Criteria andSmsReceiverIsNull() {
            addCriterion("sms_receiver is null");
            return (Criteria)this;
        }

        public Criteria andSmsReceiverIsNotNull() {
            addCriterion("sms_receiver is not null");
            return (Criteria)this;
        }

        public Criteria andSmsReceiverEqualTo(String value) {
            addCriterion("sms_receiver =", value, "smsReceiver");
            return (Criteria)this;
        }

        public Criteria andSmsReceiverNotEqualTo(String value) {
            addCriterion("sms_receiver <>", value, "smsReceiver");
            return (Criteria)this;
        }

        public Criteria andSmsReceiverGreaterThan(String value) {
            addCriterion("sms_receiver >", value, "smsReceiver");
            return (Criteria)this;
        }

        public Criteria andSmsReceiverGreaterThanOrEqualTo(String value) {
            addCriterion("sms_receiver >=", value, "smsReceiver");
            return (Criteria)this;
        }

        public Criteria andSmsReceiverLessThan(String value) {
            addCriterion("sms_receiver <", value, "smsReceiver");
            return (Criteria)this;
        }

        public Criteria andSmsReceiverLessThanOrEqualTo(String value) {
            addCriterion("sms_receiver <=", value, "smsReceiver");
            return (Criteria)this;
        }

        public Criteria andSmsReceiverLike(String value) {
            addCriterion("sms_receiver like", value, "smsReceiver");
            return (Criteria)this;
        }

        public Criteria andSmsReceiverNotLike(String value) {
            addCriterion("sms_receiver not like", value, "smsReceiver");
            return (Criteria)this;
        }

        public Criteria andSmsReceiverIn(List<String> values) {
            addCriterion("sms_receiver in", values, "smsReceiver");
            return (Criteria)this;
        }

        public Criteria andSmsReceiverNotIn(List<String> values) {
            addCriterion("sms_receiver not in", values, "smsReceiver");
            return (Criteria)this;
        }

        public Criteria andSmsReceiverBetween(String value1, String value2) {
            addCriterion("sms_receiver between", value1, value2, "smsReceiver");
            return (Criteria)this;
        }

        public Criteria andSmsReceiverNotBetween(String value1, String value2) {
            addCriterion("sms_receiver not between", value1, value2, "smsReceiver");
            return (Criteria)this;
        }

        public Criteria andResetUrlIsNull() {
            addCriterion("reset_url is null");
            return (Criteria)this;
        }

        public Criteria andResetUrlIsNotNull() {
            addCriterion("reset_url is not null");
            return (Criteria)this;
        }

        public Criteria andResetUrlEqualTo(String value) {
            addCriterion("reset_url =", value, "resetUrl");
            return (Criteria)this;
        }

        public Criteria andResetUrlNotEqualTo(String value) {
            addCriterion("reset_url <>", value, "resetUrl");
            return (Criteria)this;
        }

        public Criteria andResetUrlGreaterThan(String value) {
            addCriterion("reset_url >", value, "resetUrl");
            return (Criteria)this;
        }

        public Criteria andResetUrlGreaterThanOrEqualTo(String value) {
            addCriterion("reset_url >=", value, "resetUrl");
            return (Criteria)this;
        }

        public Criteria andResetUrlLessThan(String value) {
            addCriterion("reset_url <", value, "resetUrl");
            return (Criteria)this;
        }

        public Criteria andResetUrlLessThanOrEqualTo(String value) {
            addCriterion("reset_url <=", value, "resetUrl");
            return (Criteria)this;
        }

        public Criteria andResetUrlLike(String value) {
            addCriterion("reset_url like", value, "resetUrl");
            return (Criteria)this;
        }

        public Criteria andResetUrlNotLike(String value) {
            addCriterion("reset_url not like", value, "resetUrl");
            return (Criteria)this;
        }

        public Criteria andResetUrlIn(List<String> values) {
            addCriterion("reset_url in", values, "resetUrl");
            return (Criteria)this;
        }

        public Criteria andResetUrlNotIn(List<String> values) {
            addCriterion("reset_url not in", values, "resetUrl");
            return (Criteria)this;
        }

        public Criteria andResetUrlBetween(String value1, String value2) {
            addCriterion("reset_url between", value1, value2, "resetUrl");
            return (Criteria)this;
        }

        public Criteria andResetUrlNotBetween(String value1, String value2) {
            addCriterion("reset_url not between", value1, value2, "resetUrl");
            return (Criteria)this;
        }

        public Criteria andResetTipIsNull() {
            addCriterion("reset_tip is null");
            return (Criteria)this;
        }

        public Criteria andResetTipIsNotNull() {
            addCriterion("reset_tip is not null");
            return (Criteria)this;
        }

        public Criteria andResetTipEqualTo(String value) {
            addCriterion("reset_tip =", value, "resetTip");
            return (Criteria)this;
        }

        public Criteria andResetTipNotEqualTo(String value) {
            addCriterion("reset_tip <>", value, "resetTip");
            return (Criteria)this;
        }

        public Criteria andResetTipGreaterThan(String value) {
            addCriterion("reset_tip >", value, "resetTip");
            return (Criteria)this;
        }

        public Criteria andResetTipGreaterThanOrEqualTo(String value) {
            addCriterion("reset_tip >=", value, "resetTip");
            return (Criteria)this;
        }

        public Criteria andResetTipLessThan(String value) {
            addCriterion("reset_tip <", value, "resetTip");
            return (Criteria)this;
        }

        public Criteria andResetTipLessThanOrEqualTo(String value) {
            addCriterion("reset_tip <=", value, "resetTip");
            return (Criteria)this;
        }

        public Criteria andResetTipLike(String value) {
            addCriterion("reset_tip like", value, "resetTip");
            return (Criteria)this;
        }

        public Criteria andResetTipNotLike(String value) {
            addCriterion("reset_tip not like", value, "resetTip");
            return (Criteria)this;
        }

        public Criteria andResetTipIn(List<String> values) {
            addCriterion("reset_tip in", values, "resetTip");
            return (Criteria)this;
        }

        public Criteria andResetTipNotIn(List<String> values) {
            addCriterion("reset_tip not in", values, "resetTip");
            return (Criteria)this;
        }

        public Criteria andResetTipBetween(String value1, String value2) {
            addCriterion("reset_tip between", value1, value2, "resetTip");
            return (Criteria)this;
        }

        public Criteria andResetTipNotBetween(String value1, String value2) {
            addCriterion("reset_tip not between", value1, value2, "resetTip");
            return (Criteria)this;
        }

        public Criteria andCreatedAtIsNull() {
            addCriterion("created_at is null");
            return (Criteria)this;
        }

        public Criteria andCreatedAtIsNotNull() {
            addCriterion("created_at is not null");
            return (Criteria)this;
        }

        public Criteria andCreatedAtEqualTo(Date value) {
            addCriterion("created_at =", value, "createdAt");
            return (Criteria)this;
        }

        public Criteria andCreatedAtNotEqualTo(Date value) {
            addCriterion("created_at <>", value, "createdAt");
            return (Criteria)this;
        }

        public Criteria andCreatedAtGreaterThan(Date value) {
            addCriterion("created_at >", value, "createdAt");
            return (Criteria)this;
        }

        public Criteria andCreatedAtGreaterThanOrEqualTo(Date value) {
            addCriterion("created_at >=", value, "createdAt");
            return (Criteria)this;
        }

        public Criteria andCreatedAtLessThan(Date value) {
            addCriterion("created_at <", value, "createdAt");
            return (Criteria)this;
        }

        public Criteria andCreatedAtLessThanOrEqualTo(Date value) {
            addCriterion("created_at <=", value, "createdAt");
            return (Criteria)this;
        }

        public Criteria andCreatedAtIn(List<Date> values) {
            addCriterion("created_at in", values, "createdAt");
            return (Criteria)this;
        }

        public Criteria andCreatedAtNotIn(List<Date> values) {
            addCriterion("created_at not in", values, "createdAt");
            return (Criteria)this;
        }

        public Criteria andCreatedAtBetween(Date value1, Date value2) {
            addCriterion("created_at between", value1, value2, "createdAt");
            return (Criteria)this;
        }

        public Criteria andCreatedAtNotBetween(Date value1, Date value2) {
            addCriterion("created_at not between", value1, value2, "createdAt");
            return (Criteria)this;
        }

        public Criteria andAttributeIsNull() {
            addCriterion("attribute is null");
            return (Criteria)this;
        }

        public Criteria andAttributeIsNotNull() {
            addCriterion("attribute is not null");
            return (Criteria)this;
        }

        public Criteria andAttributeEqualTo(String value) {
            addCriterion("attribute =", value, "attribute");
            return (Criteria)this;
        }

        public Criteria andAttributeNotEqualTo(String value) {
            addCriterion("attribute <>", value, "attribute");
            return (Criteria)this;
        }

        public Criteria andAttributeGreaterThan(String value) {
            addCriterion("attribute >", value, "attribute");
            return (Criteria)this;
        }

        public Criteria andAttributeGreaterThanOrEqualTo(String value) {
            addCriterion("attribute >=", value, "attribute");
            return (Criteria)this;
        }

        public Criteria andAttributeLessThan(String value) {
            addCriterion("attribute <", value, "attribute");
            return (Criteria)this;
        }

        public Criteria andAttributeLessThanOrEqualTo(String value) {
            addCriterion("attribute <=", value, "attribute");
            return (Criteria)this;
        }

        public Criteria andAttributeLike(String value) {
            addCriterion("attribute like", value, "attribute");
            return (Criteria)this;
        }

        public Criteria andAttributeNotLike(String value) {
            addCriterion("attribute not like", value, "attribute");
            return (Criteria)this;
        }

        public Criteria andAttributeIn(List<String> values) {
            addCriterion("attribute in", values, "attribute");
            return (Criteria)this;
        }

        public Criteria andAttributeNotIn(List<String> values) {
            addCriterion("attribute not in", values, "attribute");
            return (Criteria)this;
        }

        public Criteria andAttributeBetween(String value1, String value2) {
            addCriterion("attribute between", value1, value2, "attribute");
            return (Criteria)this;
        }

        public Criteria andAttributeNotBetween(String value1, String value2) {
            addCriterion("attribute not between", value1, value2, "attribute");
            return (Criteria)this;
        }

        public Criteria andWebsiteIdLike(String value) {
            addCriterion("website_id like", value, "websiteId");
            return (Criteria)this;
        }

        public Criteria andWebsiteIdNotLike(String value) {
            addCriterion("website_id not like", value, "websiteId");
            return (Criteria)this;
        }

        public Criteria andWebsiteTypeLike(String value) {
            addCriterion("website_type like", value, "websiteType");
            return (Criteria)this;
        }

        public Criteria andWebsiteTypeNotLike(String value) {
            addCriterion("website_type not like", value, "websiteType");
            return (Criteria)this;
        }

        public Criteria andSmsIntervalLike(String value) {
            addCriterion("sms_interval like", value, "smsInterval");
            return (Criteria)this;
        }

        public Criteria andSmsIntervalNotLike(String value) {
            addCriterion("sms_interval not like", value, "smsInterval");
            return (Criteria)this;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }
    }

    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }
    }
}