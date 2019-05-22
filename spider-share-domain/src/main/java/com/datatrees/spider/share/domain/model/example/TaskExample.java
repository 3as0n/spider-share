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

package com.datatrees.spider.share.domain.model.example;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class TaskExample implements Serializable {

    private static final long serialVersionUID = 1L;

    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    /** 当前页 */
    protected int pageNum;

    /** 每页数据条数 */
    protected int pageSize;

    public TaskExample() {
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

        public Criteria andIdIsNull() {
            addCriterion("Id is null");
            return (Criteria)this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("Id is not null");
            return (Criteria)this;
        }

        public Criteria andIdEqualTo(Integer value) {
            addCriterion("Id =", value, "id");
            return (Criteria)this;
        }

        public Criteria andIdNotEqualTo(Integer value) {
            addCriterion("Id <>", value, "id");
            return (Criteria)this;
        }

        public Criteria andIdGreaterThan(Integer value) {
            addCriterion("Id >", value, "id");
            return (Criteria)this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("Id >=", value, "id");
            return (Criteria)this;
        }

        public Criteria andIdLessThan(Integer value) {
            addCriterion("Id <", value, "id");
            return (Criteria)this;
        }

        public Criteria andIdLessThanOrEqualTo(Integer value) {
            addCriterion("Id <=", value, "id");
            return (Criteria)this;
        }

        public Criteria andIdIn(List<Integer> values) {
            addCriterion("Id in", values, "id");
            return (Criteria)this;
        }

        public Criteria andIdNotIn(List<Integer> values) {
            addCriterion("Id not in", values, "id");
            return (Criteria)this;
        }

        public Criteria andIdBetween(Integer value1, Integer value2) {
            addCriterion("Id between", value1, value2, "id");
            return (Criteria)this;
        }

        public Criteria andIdNotBetween(Integer value1, Integer value2) {
            addCriterion("Id not between", value1, value2, "id");
            return (Criteria)this;
        }

        public Criteria andTaskidIsNull() {
            addCriterion("TaskId is null");
            return (Criteria)this;
        }

        public Criteria andTaskidIsNotNull() {
            addCriterion("TaskId is not null");
            return (Criteria)this;
        }

        public Criteria andTaskidEqualTo(Long value) {
            addCriterion("TaskId =", value, "taskid");
            return (Criteria)this;
        }

        public Criteria andTaskidNotEqualTo(Long value) {
            addCriterion("TaskId <>", value, "taskid");
            return (Criteria)this;
        }

        public Criteria andTaskidGreaterThan(Long value) {
            addCriterion("TaskId >", value, "taskid");
            return (Criteria)this;
        }

        public Criteria andTaskidGreaterThanOrEqualTo(Long value) {
            addCriterion("TaskId >=", value, "taskid");
            return (Criteria)this;
        }

        public Criteria andTaskidLessThan(Long value) {
            addCriterion("TaskId <", value, "taskid");
            return (Criteria)this;
        }

        public Criteria andTaskidLessThanOrEqualTo(Long value) {
            addCriterion("TaskId <=", value, "taskid");
            return (Criteria)this;
        }

        public Criteria andTaskidIn(List<Long> values) {
            addCriterion("TaskId in", values, "taskid");
            return (Criteria)this;
        }

        public Criteria andTaskidNotIn(List<Long> values) {
            addCriterion("TaskId not in", values, "taskid");
            return (Criteria)this;
        }

        public Criteria andTaskidBetween(Long value1, Long value2) {
            addCriterion("TaskId between", value1, value2, "taskid");
            return (Criteria)this;
        }

        public Criteria andTaskidNotBetween(Long value1, Long value2) {
            addCriterion("TaskId not between", value1, value2, "taskid");
            return (Criteria)this;
        }

        public Criteria andWebsiteidIsNull() {
            addCriterion("WebsiteId is null");
            return (Criteria)this;
        }

        public Criteria andWebsiteidIsNotNull() {
            addCriterion("WebsiteId is not null");
            return (Criteria)this;
        }

        public Criteria andWebsiteidEqualTo(Integer value) {
            addCriterion("WebsiteId =", value, "websiteid");
            return (Criteria)this;
        }

        public Criteria andWebsiteidNotEqualTo(Integer value) {
            addCriterion("WebsiteId <>", value, "websiteid");
            return (Criteria)this;
        }

        public Criteria andWebsiteidGreaterThan(Integer value) {
            addCriterion("WebsiteId >", value, "websiteid");
            return (Criteria)this;
        }

        public Criteria andWebsiteidGreaterThanOrEqualTo(Integer value) {
            addCriterion("WebsiteId >=", value, "websiteid");
            return (Criteria)this;
        }

        public Criteria andWebsiteidLessThan(Integer value) {
            addCriterion("WebsiteId <", value, "websiteid");
            return (Criteria)this;
        }

        public Criteria andWebsiteidLessThanOrEqualTo(Integer value) {
            addCriterion("WebsiteId <=", value, "websiteid");
            return (Criteria)this;
        }

        public Criteria andWebsiteidIn(List<Integer> values) {
            addCriterion("WebsiteId in", values, "websiteid");
            return (Criteria)this;
        }

        public Criteria andWebsiteidNotIn(List<Integer> values) {
            addCriterion("WebsiteId not in", values, "websiteid");
            return (Criteria)this;
        }

        public Criteria andWebsiteidBetween(Integer value1, Integer value2) {
            addCriterion("WebsiteId between", value1, value2, "websiteid");
            return (Criteria)this;
        }

        public Criteria andWebsiteidNotBetween(Integer value1, Integer value2) {
            addCriterion("WebsiteId not between", value1, value2, "websiteid");
            return (Criteria)this;
        }

        public Criteria andNodenameIsNull() {
            addCriterion("NodeName is null");
            return (Criteria)this;
        }

        public Criteria andNodenameIsNotNull() {
            addCriterion("NodeName is not null");
            return (Criteria)this;
        }

        public Criteria andNodenameEqualTo(String value) {
            addCriterion("NodeName =", value, "nodename");
            return (Criteria)this;
        }

        public Criteria andNodenameNotEqualTo(String value) {
            addCriterion("NodeName <>", value, "nodename");
            return (Criteria)this;
        }

        public Criteria andNodenameGreaterThan(String value) {
            addCriterion("NodeName >", value, "nodename");
            return (Criteria)this;
        }

        public Criteria andNodenameGreaterThanOrEqualTo(String value) {
            addCriterion("NodeName >=", value, "nodename");
            return (Criteria)this;
        }

        public Criteria andNodenameLessThan(String value) {
            addCriterion("NodeName <", value, "nodename");
            return (Criteria)this;
        }

        public Criteria andNodenameLessThanOrEqualTo(String value) {
            addCriterion("NodeName <=", value, "nodename");
            return (Criteria)this;
        }

        public Criteria andNodenameLike(String value) {
            addCriterion("NodeName like", value, "nodename");
            return (Criteria)this;
        }

        public Criteria andNodenameNotLike(String value) {
            addCriterion("NodeName not like", value, "nodename");
            return (Criteria)this;
        }

        public Criteria andNodenameIn(List<String> values) {
            addCriterion("NodeName in", values, "nodename");
            return (Criteria)this;
        }

        public Criteria andNodenameNotIn(List<String> values) {
            addCriterion("NodeName not in", values, "nodename");
            return (Criteria)this;
        }

        public Criteria andNodenameBetween(String value1, String value2) {
            addCriterion("NodeName between", value1, value2, "nodename");
            return (Criteria)this;
        }

        public Criteria andNodenameNotBetween(String value1, String value2) {
            addCriterion("NodeName not between", value1, value2, "nodename");
            return (Criteria)this;
        }

        public Criteria andOpenurlcountIsNull() {
            addCriterion("OpenUrlCount is null");
            return (Criteria)this;
        }

        public Criteria andOpenurlcountIsNotNull() {
            addCriterion("OpenUrlCount is not null");
            return (Criteria)this;
        }

        public Criteria andOpenurlcountEqualTo(AtomicInteger value) {
            addCriterion("OpenUrlCount =", value, "openurlcount");
            return (Criteria)this;
        }

        public Criteria andOpenurlcountNotEqualTo(AtomicInteger value) {
            addCriterion("OpenUrlCount <>", value, "openurlcount");
            return (Criteria)this;
        }

        public Criteria andOpenurlcountGreaterThan(AtomicInteger value) {
            addCriterion("OpenUrlCount >", value, "openurlcount");
            return (Criteria)this;
        }

        public Criteria andOpenurlcountGreaterThanOrEqualTo(AtomicInteger value) {
            addCriterion("OpenUrlCount >=", value, "openurlcount");
            return (Criteria)this;
        }

        public Criteria andOpenurlcountLessThan(AtomicInteger value) {
            addCriterion("OpenUrlCount <", value, "openurlcount");
            return (Criteria)this;
        }

        public Criteria andOpenurlcountLessThanOrEqualTo(AtomicInteger value) {
            addCriterion("OpenUrlCount <=", value, "openurlcount");
            return (Criteria)this;
        }

        public Criteria andOpenurlcountIn(List<AtomicInteger> values) {
            addCriterion("OpenUrlCount in", values, "openurlcount");
            return (Criteria)this;
        }

        public Criteria andOpenurlcountNotIn(List<AtomicInteger> values) {
            addCriterion("OpenUrlCount not in", values, "openurlcount");
            return (Criteria)this;
        }

        public Criteria andOpenurlcountBetween(AtomicInteger value1, AtomicInteger value2) {
            addCriterion("OpenUrlCount between", value1, value2, "openurlcount");
            return (Criteria)this;
        }

        public Criteria andOpenurlcountNotBetween(AtomicInteger value1, AtomicInteger value2) {
            addCriterion("OpenUrlCount not between", value1, value2, "openurlcount");
            return (Criteria)this;
        }

        public Criteria andOpenpagecountIsNull() {
            addCriterion("OpenPageCount is null");
            return (Criteria)this;
        }

        public Criteria andOpenpagecountIsNotNull() {
            addCriterion("OpenPageCount is not null");
            return (Criteria)this;
        }

        public Criteria andOpenpagecountEqualTo(AtomicInteger value) {
            addCriterion("OpenPageCount =", value, "openpagecount");
            return (Criteria)this;
        }

        public Criteria andOpenpagecountNotEqualTo(AtomicInteger value) {
            addCriterion("OpenPageCount <>", value, "openpagecount");
            return (Criteria)this;
        }

        public Criteria andOpenpagecountGreaterThan(AtomicInteger value) {
            addCriterion("OpenPageCount >", value, "openpagecount");
            return (Criteria)this;
        }

        public Criteria andOpenpagecountGreaterThanOrEqualTo(AtomicInteger value) {
            addCriterion("OpenPageCount >=", value, "openpagecount");
            return (Criteria)this;
        }

        public Criteria andOpenpagecountLessThan(AtomicInteger value) {
            addCriterion("OpenPageCount <", value, "openpagecount");
            return (Criteria)this;
        }

        public Criteria andOpenpagecountLessThanOrEqualTo(AtomicInteger value) {
            addCriterion("OpenPageCount <=", value, "openpagecount");
            return (Criteria)this;
        }

        public Criteria andOpenpagecountIn(List<AtomicInteger> values) {
            addCriterion("OpenPageCount in", values, "openpagecount");
            return (Criteria)this;
        }

        public Criteria andOpenpagecountNotIn(List<AtomicInteger> values) {
            addCriterion("OpenPageCount not in", values, "openpagecount");
            return (Criteria)this;
        }

        public Criteria andOpenpagecountBetween(AtomicInteger value1, AtomicInteger value2) {
            addCriterion("OpenPageCount between", value1, value2, "openpagecount");
            return (Criteria)this;
        }

        public Criteria andOpenpagecountNotBetween(AtomicInteger value1, AtomicInteger value2) {
            addCriterion("OpenPageCount not between", value1, value2, "openpagecount");
            return (Criteria)this;
        }

        public Criteria andRequestfailedcountIsNull() {
            addCriterion("RequestFailedCount is null");
            return (Criteria)this;
        }

        public Criteria andRequestfailedcountIsNotNull() {
            addCriterion("RequestFailedCount is not null");
            return (Criteria)this;
        }

        public Criteria andRequestfailedcountEqualTo(AtomicInteger value) {
            addCriterion("RequestFailedCount =", value, "requestfailedcount");
            return (Criteria)this;
        }

        public Criteria andRequestfailedcountNotEqualTo(AtomicInteger value) {
            addCriterion("RequestFailedCount <>", value, "requestfailedcount");
            return (Criteria)this;
        }

        public Criteria andRequestfailedcountGreaterThan(AtomicInteger value) {
            addCriterion("RequestFailedCount >", value, "requestfailedcount");
            return (Criteria)this;
        }

        public Criteria andRequestfailedcountGreaterThanOrEqualTo(AtomicInteger value) {
            addCriterion("RequestFailedCount >=", value, "requestfailedcount");
            return (Criteria)this;
        }

        public Criteria andRequestfailedcountLessThan(AtomicInteger value) {
            addCriterion("RequestFailedCount <", value, "requestfailedcount");
            return (Criteria)this;
        }

        public Criteria andRequestfailedcountLessThanOrEqualTo(AtomicInteger value) {
            addCriterion("RequestFailedCount <=", value, "requestfailedcount");
            return (Criteria)this;
        }

        public Criteria andRequestfailedcountIn(List<AtomicInteger> values) {
            addCriterion("RequestFailedCount in", values, "requestfailedcount");
            return (Criteria)this;
        }

        public Criteria andRequestfailedcountNotIn(List<AtomicInteger> values) {
            addCriterion("RequestFailedCount not in", values, "requestfailedcount");
            return (Criteria)this;
        }

        public Criteria andRequestfailedcountBetween(AtomicInteger value1, AtomicInteger value2) {
            addCriterion("RequestFailedCount between", value1, value2, "requestfailedcount");
            return (Criteria)this;
        }

        public Criteria andRequestfailedcountNotBetween(AtomicInteger value1, AtomicInteger value2) {
            addCriterion("RequestFailedCount not between", value1, value2, "requestfailedcount");
            return (Criteria)this;
        }

        public Criteria andRetrycountIsNull() {
            addCriterion("RetryCount is null");
            return (Criteria)this;
        }

        public Criteria andRetrycountIsNotNull() {
            addCriterion("RetryCount is not null");
            return (Criteria)this;
        }

        public Criteria andRetrycountEqualTo(AtomicInteger value) {
            addCriterion("RetryCount =", value, "retrycount");
            return (Criteria)this;
        }

        public Criteria andRetrycountNotEqualTo(AtomicInteger value) {
            addCriterion("RetryCount <>", value, "retrycount");
            return (Criteria)this;
        }

        public Criteria andRetrycountGreaterThan(AtomicInteger value) {
            addCriterion("RetryCount >", value, "retrycount");
            return (Criteria)this;
        }

        public Criteria andRetrycountGreaterThanOrEqualTo(AtomicInteger value) {
            addCriterion("RetryCount >=", value, "retrycount");
            return (Criteria)this;
        }

        public Criteria andRetrycountLessThan(AtomicInteger value) {
            addCriterion("RetryCount <", value, "retrycount");
            return (Criteria)this;
        }

        public Criteria andRetrycountLessThanOrEqualTo(AtomicInteger value) {
            addCriterion("RetryCount <=", value, "retrycount");
            return (Criteria)this;
        }

        public Criteria andRetrycountIn(List<AtomicInteger> values) {
            addCriterion("RetryCount in", values, "retrycount");
            return (Criteria)this;
        }

        public Criteria andRetrycountNotIn(List<AtomicInteger> values) {
            addCriterion("RetryCount not in", values, "retrycount");
            return (Criteria)this;
        }

        public Criteria andRetrycountBetween(AtomicInteger value1, AtomicInteger value2) {
            addCriterion("RetryCount between", value1, value2, "retrycount");
            return (Criteria)this;
        }

        public Criteria andRetrycountNotBetween(AtomicInteger value1, AtomicInteger value2) {
            addCriterion("RetryCount not between", value1, value2, "retrycount");
            return (Criteria)this;
        }

        public Criteria andFilteredcountIsNull() {
            addCriterion("FilteredCount is null");
            return (Criteria)this;
        }

        public Criteria andFilteredcountIsNotNull() {
            addCriterion("FilteredCount is not null");
            return (Criteria)this;
        }

        public Criteria andFilteredcountEqualTo(AtomicInteger value) {
            addCriterion("FilteredCount =", value, "filteredcount");
            return (Criteria)this;
        }

        public Criteria andFilteredcountNotEqualTo(AtomicInteger value) {
            addCriterion("FilteredCount <>", value, "filteredcount");
            return (Criteria)this;
        }

        public Criteria andFilteredcountGreaterThan(AtomicInteger value) {
            addCriterion("FilteredCount >", value, "filteredcount");
            return (Criteria)this;
        }

        public Criteria andFilteredcountGreaterThanOrEqualTo(AtomicInteger value) {
            addCriterion("FilteredCount >=", value, "filteredcount");
            return (Criteria)this;
        }

        public Criteria andFilteredcountLessThan(AtomicInteger value) {
            addCriterion("FilteredCount <", value, "filteredcount");
            return (Criteria)this;
        }

        public Criteria andFilteredcountLessThanOrEqualTo(AtomicInteger value) {
            addCriterion("FilteredCount <=", value, "filteredcount");
            return (Criteria)this;
        }

        public Criteria andFilteredcountIn(List<AtomicInteger> values) {
            addCriterion("FilteredCount in", values, "filteredcount");
            return (Criteria)this;
        }

        public Criteria andFilteredcountNotIn(List<AtomicInteger> values) {
            addCriterion("FilteredCount not in", values, "filteredcount");
            return (Criteria)this;
        }

        public Criteria andFilteredcountBetween(AtomicInteger value1, AtomicInteger value2) {
            addCriterion("FilteredCount between", value1, value2, "filteredcount");
            return (Criteria)this;
        }

        public Criteria andFilteredcountNotBetween(AtomicInteger value1, AtomicInteger value2) {
            addCriterion("FilteredCount not between", value1, value2, "filteredcount");
            return (Criteria)this;
        }

        public Criteria andStatusIsNull() {
            addCriterion("Status is null");
            return (Criteria)this;
        }

        public Criteria andStatusIsNotNull() {
            addCriterion("Status is not null");
            return (Criteria)this;
        }

        public Criteria andStatusEqualTo(Integer value) {
            addCriterion("Status =", value, "status");
            return (Criteria)this;
        }

        public Criteria andStatusNotEqualTo(Integer value) {
            addCriterion("Status <>", value, "status");
            return (Criteria)this;
        }

        public Criteria andStatusGreaterThan(Integer value) {
            addCriterion("Status >", value, "status");
            return (Criteria)this;
        }

        public Criteria andStatusGreaterThanOrEqualTo(Integer value) {
            addCriterion("Status >=", value, "status");
            return (Criteria)this;
        }

        public Criteria andStatusLessThan(Integer value) {
            addCriterion("Status <", value, "status");
            return (Criteria)this;
        }

        public Criteria andStatusLessThanOrEqualTo(Integer value) {
            addCriterion("Status <=", value, "status");
            return (Criteria)this;
        }

        public Criteria andStatusIn(List<Integer> values) {
            addCriterion("Status in", values, "status");
            return (Criteria)this;
        }

        public Criteria andStatusNotIn(List<Integer> values) {
            addCriterion("Status not in", values, "status");
            return (Criteria)this;
        }

        public Criteria andStatusBetween(Integer value1, Integer value2) {
            addCriterion("Status between", value1, value2, "status");
            return (Criteria)this;
        }

        public Criteria andStatusNotBetween(Integer value1, Integer value2) {
            addCriterion("Status not between", value1, value2, "status");
            return (Criteria)this;
        }

        public Criteria andRemarkIsNull() {
            addCriterion("Remark is null");
            return (Criteria)this;
        }

        public Criteria andRemarkIsNotNull() {
            addCriterion("Remark is not null");
            return (Criteria)this;
        }

        public Criteria andRemarkEqualTo(String value) {
            addCriterion("Remark =", value, "remark");
            return (Criteria)this;
        }

        public Criteria andRemarkNotEqualTo(String value) {
            addCriterion("Remark <>", value, "remark");
            return (Criteria)this;
        }

        public Criteria andRemarkGreaterThan(String value) {
            addCriterion("Remark >", value, "remark");
            return (Criteria)this;
        }

        public Criteria andRemarkGreaterThanOrEqualTo(String value) {
            addCriterion("Remark >=", value, "remark");
            return (Criteria)this;
        }

        public Criteria andRemarkLessThan(String value) {
            addCriterion("Remark <", value, "remark");
            return (Criteria)this;
        }

        public Criteria andRemarkLessThanOrEqualTo(String value) {
            addCriterion("Remark <=", value, "remark");
            return (Criteria)this;
        }

        public Criteria andRemarkLike(String value) {
            addCriterion("Remark like", value, "remark");
            return (Criteria)this;
        }

        public Criteria andRemarkNotLike(String value) {
            addCriterion("Remark not like", value, "remark");
            return (Criteria)this;
        }

        public Criteria andRemarkIn(List<String> values) {
            addCriterion("Remark in", values, "remark");
            return (Criteria)this;
        }

        public Criteria andRemarkNotIn(List<String> values) {
            addCriterion("Remark not in", values, "remark");
            return (Criteria)this;
        }

        public Criteria andRemarkBetween(String value1, String value2) {
            addCriterion("Remark between", value1, value2, "remark");
            return (Criteria)this;
        }

        public Criteria andRemarkNotBetween(String value1, String value2) {
            addCriterion("Remark not between", value1, value2, "remark");
            return (Criteria)this;
        }

        public Criteria andResultmessageIsNull() {
            addCriterion("ResultMessage is null");
            return (Criteria)this;
        }

        public Criteria andResultmessageIsNotNull() {
            addCriterion("ResultMessage is not null");
            return (Criteria)this;
        }

        public Criteria andResultmessageEqualTo(String value) {
            addCriterion("ResultMessage =", value, "resultmessage");
            return (Criteria)this;
        }

        public Criteria andResultmessageNotEqualTo(String value) {
            addCriterion("ResultMessage <>", value, "resultmessage");
            return (Criteria)this;
        }

        public Criteria andResultmessageGreaterThan(String value) {
            addCriterion("ResultMessage >", value, "resultmessage");
            return (Criteria)this;
        }

        public Criteria andResultmessageGreaterThanOrEqualTo(String value) {
            addCriterion("ResultMessage >=", value, "resultmessage");
            return (Criteria)this;
        }

        public Criteria andResultmessageLessThan(String value) {
            addCriterion("ResultMessage <", value, "resultmessage");
            return (Criteria)this;
        }

        public Criteria andResultmessageLessThanOrEqualTo(String value) {
            addCriterion("ResultMessage <=", value, "resultmessage");
            return (Criteria)this;
        }

        public Criteria andResultmessageLike(String value) {
            addCriterion("ResultMessage like", value, "resultmessage");
            return (Criteria)this;
        }

        public Criteria andResultmessageNotLike(String value) {
            addCriterion("ResultMessage not like", value, "resultmessage");
            return (Criteria)this;
        }

        public Criteria andResultmessageIn(List<String> values) {
            addCriterion("ResultMessage in", values, "resultmessage");
            return (Criteria)this;
        }

        public Criteria andResultmessageNotIn(List<String> values) {
            addCriterion("ResultMessage not in", values, "resultmessage");
            return (Criteria)this;
        }

        public Criteria andResultmessageBetween(String value1, String value2) {
            addCriterion("ResultMessage between", value1, value2, "resultmessage");
            return (Criteria)this;
        }

        public Criteria andResultmessageNotBetween(String value1, String value2) {
            addCriterion("ResultMessage not between", value1, value2, "resultmessage");
            return (Criteria)this;
        }

        public Criteria andDurationIsNull() {
            addCriterion("Duration is null");
            return (Criteria)this;
        }

        public Criteria andDurationIsNotNull() {
            addCriterion("Duration is not null");
            return (Criteria)this;
        }

        public Criteria andDurationEqualTo(Long value) {
            addCriterion("Duration =", value, "duration");
            return (Criteria)this;
        }

        public Criteria andDurationNotEqualTo(Long value) {
            addCriterion("Duration <>", value, "duration");
            return (Criteria)this;
        }

        public Criteria andDurationGreaterThan(Long value) {
            addCriterion("Duration >", value, "duration");
            return (Criteria)this;
        }

        public Criteria andDurationGreaterThanOrEqualTo(Long value) {
            addCriterion("Duration >=", value, "duration");
            return (Criteria)this;
        }

        public Criteria andDurationLessThan(Long value) {
            addCriterion("Duration <", value, "duration");
            return (Criteria)this;
        }

        public Criteria andDurationLessThanOrEqualTo(Long value) {
            addCriterion("Duration <=", value, "duration");
            return (Criteria)this;
        }

        public Criteria andDurationIn(List<Long> values) {
            addCriterion("Duration in", values, "duration");
            return (Criteria)this;
        }

        public Criteria andDurationNotIn(List<Long> values) {
            addCriterion("Duration not in", values, "duration");
            return (Criteria)this;
        }

        public Criteria andDurationBetween(Long value1, Long value2) {
            addCriterion("Duration between", value1, value2, "duration");
            return (Criteria)this;
        }

        public Criteria andDurationNotBetween(Long value1, Long value2) {
            addCriterion("Duration not between", value1, value2, "duration");
            return (Criteria)this;
        }

        public Criteria andExtractedcountIsNull() {
            addCriterion("ExtractedCount is null");
            return (Criteria)this;
        }

        public Criteria andExtractedcountIsNotNull() {
            addCriterion("ExtractedCount is not null");
            return (Criteria)this;
        }

        public Criteria andExtractedcountEqualTo(Integer value) {
            addCriterion("ExtractedCount =", value, "extractedcount");
            return (Criteria)this;
        }

        public Criteria andExtractedcountNotEqualTo(Integer value) {
            addCriterion("ExtractedCount <>", value, "extractedcount");
            return (Criteria)this;
        }

        public Criteria andExtractedcountGreaterThan(Integer value) {
            addCriterion("ExtractedCount >", value, "extractedcount");
            return (Criteria)this;
        }

        public Criteria andExtractedcountGreaterThanOrEqualTo(Integer value) {
            addCriterion("ExtractedCount >=", value, "extractedcount");
            return (Criteria)this;
        }

        public Criteria andExtractedcountLessThan(Integer value) {
            addCriterion("ExtractedCount <", value, "extractedcount");
            return (Criteria)this;
        }

        public Criteria andExtractedcountLessThanOrEqualTo(Integer value) {
            addCriterion("ExtractedCount <=", value, "extractedcount");
            return (Criteria)this;
        }

        public Criteria andExtractedcountIn(List<Integer> values) {
            addCriterion("ExtractedCount in", values, "extractedcount");
            return (Criteria)this;
        }

        public Criteria andExtractedcountNotIn(List<Integer> values) {
            addCriterion("ExtractedCount not in", values, "extractedcount");
            return (Criteria)this;
        }

        public Criteria andExtractedcountBetween(Integer value1, Integer value2) {
            addCriterion("ExtractedCount between", value1, value2, "extractedcount");
            return (Criteria)this;
        }

        public Criteria andExtractedcountNotBetween(Integer value1, Integer value2) {
            addCriterion("ExtractedCount not between", value1, value2, "extractedcount");
            return (Criteria)this;
        }

        public Criteria andExtractsucceedcountIsNull() {
            addCriterion("ExtractSucceedCount is null");
            return (Criteria)this;
        }

        public Criteria andExtractsucceedcountIsNotNull() {
            addCriterion("ExtractSucceedCount is not null");
            return (Criteria)this;
        }

        public Criteria andExtractsucceedcountEqualTo(Integer value) {
            addCriterion("ExtractSucceedCount =", value, "extractsucceedcount");
            return (Criteria)this;
        }

        public Criteria andExtractsucceedcountNotEqualTo(Integer value) {
            addCriterion("ExtractSucceedCount <>", value, "extractsucceedcount");
            return (Criteria)this;
        }

        public Criteria andExtractsucceedcountGreaterThan(Integer value) {
            addCriterion("ExtractSucceedCount >", value, "extractsucceedcount");
            return (Criteria)this;
        }

        public Criteria andExtractsucceedcountGreaterThanOrEqualTo(Integer value) {
            addCriterion("ExtractSucceedCount >=", value, "extractsucceedcount");
            return (Criteria)this;
        }

        public Criteria andExtractsucceedcountLessThan(Integer value) {
            addCriterion("ExtractSucceedCount <", value, "extractsucceedcount");
            return (Criteria)this;
        }

        public Criteria andExtractsucceedcountLessThanOrEqualTo(Integer value) {
            addCriterion("ExtractSucceedCount <=", value, "extractsucceedcount");
            return (Criteria)this;
        }

        public Criteria andExtractsucceedcountIn(List<Integer> values) {
            addCriterion("ExtractSucceedCount in", values, "extractsucceedcount");
            return (Criteria)this;
        }

        public Criteria andExtractsucceedcountNotIn(List<Integer> values) {
            addCriterion("ExtractSucceedCount not in", values, "extractsucceedcount");
            return (Criteria)this;
        }

        public Criteria andExtractsucceedcountBetween(Integer value1, Integer value2) {
            addCriterion("ExtractSucceedCount between", value1, value2, "extractsucceedcount");
            return (Criteria)this;
        }

        public Criteria andExtractsucceedcountNotBetween(Integer value1, Integer value2) {
            addCriterion("ExtractSucceedCount not between", value1, value2, "extractsucceedcount");
            return (Criteria)this;
        }

        public Criteria andExtractfailedcountIsNull() {
            addCriterion("ExtractFailedCount is null");
            return (Criteria)this;
        }

        public Criteria andExtractfailedcountIsNotNull() {
            addCriterion("ExtractFailedCount is not null");
            return (Criteria)this;
        }

        public Criteria andExtractfailedcountEqualTo(Integer value) {
            addCriterion("ExtractFailedCount =", value, "extractfailedcount");
            return (Criteria)this;
        }

        public Criteria andExtractfailedcountNotEqualTo(Integer value) {
            addCriterion("ExtractFailedCount <>", value, "extractfailedcount");
            return (Criteria)this;
        }

        public Criteria andExtractfailedcountGreaterThan(Integer value) {
            addCriterion("ExtractFailedCount >", value, "extractfailedcount");
            return (Criteria)this;
        }

        public Criteria andExtractfailedcountGreaterThanOrEqualTo(Integer value) {
            addCriterion("ExtractFailedCount >=", value, "extractfailedcount");
            return (Criteria)this;
        }

        public Criteria andExtractfailedcountLessThan(Integer value) {
            addCriterion("ExtractFailedCount <", value, "extractfailedcount");
            return (Criteria)this;
        }

        public Criteria andExtractfailedcountLessThanOrEqualTo(Integer value) {
            addCriterion("ExtractFailedCount <=", value, "extractfailedcount");
            return (Criteria)this;
        }

        public Criteria andExtractfailedcountIn(List<Integer> values) {
            addCriterion("ExtractFailedCount in", values, "extractfailedcount");
            return (Criteria)this;
        }

        public Criteria andExtractfailedcountNotIn(List<Integer> values) {
            addCriterion("ExtractFailedCount not in", values, "extractfailedcount");
            return (Criteria)this;
        }

        public Criteria andExtractfailedcountBetween(Integer value1, Integer value2) {
            addCriterion("ExtractFailedCount between", value1, value2, "extractfailedcount");
            return (Criteria)this;
        }

        public Criteria andExtractfailedcountNotBetween(Integer value1, Integer value2) {
            addCriterion("ExtractFailedCount not between", value1, value2, "extractfailedcount");
            return (Criteria)this;
        }

        public Criteria andStorefailedcountIsNull() {
            addCriterion("StoreFailedCount is null");
            return (Criteria)this;
        }

        public Criteria andStorefailedcountIsNotNull() {
            addCriterion("StoreFailedCount is not null");
            return (Criteria)this;
        }

        public Criteria andStorefailedcountEqualTo(Integer value) {
            addCriterion("StoreFailedCount =", value, "storefailedcount");
            return (Criteria)this;
        }

        public Criteria andStorefailedcountNotEqualTo(Integer value) {
            addCriterion("StoreFailedCount <>", value, "storefailedcount");
            return (Criteria)this;
        }

        public Criteria andStorefailedcountGreaterThan(Integer value) {
            addCriterion("StoreFailedCount >", value, "storefailedcount");
            return (Criteria)this;
        }

        public Criteria andStorefailedcountGreaterThanOrEqualTo(Integer value) {
            addCriterion("StoreFailedCount >=", value, "storefailedcount");
            return (Criteria)this;
        }

        public Criteria andStorefailedcountLessThan(Integer value) {
            addCriterion("StoreFailedCount <", value, "storefailedcount");
            return (Criteria)this;
        }

        public Criteria andStorefailedcountLessThanOrEqualTo(Integer value) {
            addCriterion("StoreFailedCount <=", value, "storefailedcount");
            return (Criteria)this;
        }

        public Criteria andStorefailedcountIn(List<Integer> values) {
            addCriterion("StoreFailedCount in", values, "storefailedcount");
            return (Criteria)this;
        }

        public Criteria andStorefailedcountNotIn(List<Integer> values) {
            addCriterion("StoreFailedCount not in", values, "storefailedcount");
            return (Criteria)this;
        }

        public Criteria andStorefailedcountBetween(Integer value1, Integer value2) {
            addCriterion("StoreFailedCount between", value1, value2, "storefailedcount");
            return (Criteria)this;
        }

        public Criteria andStorefailedcountNotBetween(Integer value1, Integer value2) {
            addCriterion("StoreFailedCount not between", value1, value2, "storefailedcount");
            return (Criteria)this;
        }

        public Criteria andNotextractcountIsNull() {
            addCriterion("NotExtractCount is null");
            return (Criteria)this;
        }

        public Criteria andNotextractcountIsNotNull() {
            addCriterion("NotExtractCount is not null");
            return (Criteria)this;
        }

        public Criteria andNotextractcountEqualTo(Integer value) {
            addCriterion("NotExtractCount =", value, "notextractcount");
            return (Criteria)this;
        }

        public Criteria andNotextractcountNotEqualTo(Integer value) {
            addCriterion("NotExtractCount <>", value, "notextractcount");
            return (Criteria)this;
        }

        public Criteria andNotextractcountGreaterThan(Integer value) {
            addCriterion("NotExtractCount >", value, "notextractcount");
            return (Criteria)this;
        }

        public Criteria andNotextractcountGreaterThanOrEqualTo(Integer value) {
            addCriterion("NotExtractCount >=", value, "notextractcount");
            return (Criteria)this;
        }

        public Criteria andNotextractcountLessThan(Integer value) {
            addCriterion("NotExtractCount <", value, "notextractcount");
            return (Criteria)this;
        }

        public Criteria andNotextractcountLessThanOrEqualTo(Integer value) {
            addCriterion("NotExtractCount <=", value, "notextractcount");
            return (Criteria)this;
        }

        public Criteria andNotextractcountIn(List<Integer> values) {
            addCriterion("NotExtractCount in", values, "notextractcount");
            return (Criteria)this;
        }

        public Criteria andNotextractcountNotIn(List<Integer> values) {
            addCriterion("NotExtractCount not in", values, "notextractcount");
            return (Criteria)this;
        }

        public Criteria andNotextractcountBetween(Integer value1, Integer value2) {
            addCriterion("NotExtractCount between", value1, value2, "notextractcount");
            return (Criteria)this;
        }

        public Criteria andNotextractcountNotBetween(Integer value1, Integer value2) {
            addCriterion("NotExtractCount not between", value1, value2, "notextractcount");
            return (Criteria)this;
        }

        public Criteria andNetworktrafficIsNull() {
            addCriterion("NetworkTraffic is null");
            return (Criteria)this;
        }

        public Criteria andNetworktrafficIsNotNull() {
            addCriterion("NetworkTraffic is not null");
            return (Criteria)this;
        }

        public Criteria andNetworktrafficEqualTo(AtomicLong value) {
            addCriterion("NetworkTraffic =", value, "networktraffic");
            return (Criteria)this;
        }

        public Criteria andNetworktrafficNotEqualTo(AtomicLong value) {
            addCriterion("NetworkTraffic <>", value, "networktraffic");
            return (Criteria)this;
        }

        public Criteria andNetworktrafficGreaterThan(AtomicLong value) {
            addCriterion("NetworkTraffic >", value, "networktraffic");
            return (Criteria)this;
        }

        public Criteria andNetworktrafficGreaterThanOrEqualTo(AtomicLong value) {
            addCriterion("NetworkTraffic >=", value, "networktraffic");
            return (Criteria)this;
        }

        public Criteria andNetworktrafficLessThan(AtomicLong value) {
            addCriterion("NetworkTraffic <", value, "networktraffic");
            return (Criteria)this;
        }

        public Criteria andNetworktrafficLessThanOrEqualTo(AtomicLong value) {
            addCriterion("NetworkTraffic <=", value, "networktraffic");
            return (Criteria)this;
        }

        public Criteria andNetworktrafficIn(List<AtomicLong> values) {
            addCriterion("NetworkTraffic in", values, "networktraffic");
            return (Criteria)this;
        }

        public Criteria andNetworktrafficNotIn(List<AtomicLong> values) {
            addCriterion("NetworkTraffic not in", values, "networktraffic");
            return (Criteria)this;
        }

        public Criteria andNetworktrafficBetween(AtomicLong value1, AtomicLong value2) {
            addCriterion("NetworkTraffic between", value1, value2, "networktraffic");
            return (Criteria)this;
        }

        public Criteria andNetworktrafficNotBetween(AtomicLong value1, AtomicLong value2) {
            addCriterion("NetworkTraffic not between", value1, value2, "networktraffic");
            return (Criteria)this;
        }

        public Criteria andStartedatIsNull() {
            addCriterion("StartedAt is null");
            return (Criteria)this;
        }

        public Criteria andStartedatIsNotNull() {
            addCriterion("StartedAt is not null");
            return (Criteria)this;
        }

        public Criteria andStartedatEqualTo(Date value) {
            addCriterion("StartedAt =", value, "startedat");
            return (Criteria)this;
        }

        public Criteria andStartedatNotEqualTo(Date value) {
            addCriterion("StartedAt <>", value, "startedat");
            return (Criteria)this;
        }

        public Criteria andStartedatGreaterThan(Date value) {
            addCriterion("StartedAt >", value, "startedat");
            return (Criteria)this;
        }

        public Criteria andStartedatGreaterThanOrEqualTo(Date value) {
            addCriterion("StartedAt >=", value, "startedat");
            return (Criteria)this;
        }

        public Criteria andStartedatLessThan(Date value) {
            addCriterion("StartedAt <", value, "startedat");
            return (Criteria)this;
        }

        public Criteria andStartedatLessThanOrEqualTo(Date value) {
            addCriterion("StartedAt <=", value, "startedat");
            return (Criteria)this;
        }

        public Criteria andStartedatIn(List<Date> values) {
            addCriterion("StartedAt in", values, "startedat");
            return (Criteria)this;
        }

        public Criteria andStartedatNotIn(List<Date> values) {
            addCriterion("StartedAt not in", values, "startedat");
            return (Criteria)this;
        }

        public Criteria andStartedatBetween(Date value1, Date value2) {
            addCriterion("StartedAt between", value1, value2, "startedat");
            return (Criteria)this;
        }

        public Criteria andStartedatNotBetween(Date value1, Date value2) {
            addCriterion("StartedAt not between", value1, value2, "startedat");
            return (Criteria)this;
        }

        public Criteria andFinishedatIsNull() {
            addCriterion("FinishedAt is null");
            return (Criteria)this;
        }

        public Criteria andFinishedatIsNotNull() {
            addCriterion("FinishedAt is not null");
            return (Criteria)this;
        }

        public Criteria andFinishedatEqualTo(Date value) {
            addCriterion("FinishedAt =", value, "finishedat");
            return (Criteria)this;
        }

        public Criteria andFinishedatNotEqualTo(Date value) {
            addCriterion("FinishedAt <>", value, "finishedat");
            return (Criteria)this;
        }

        public Criteria andFinishedatGreaterThan(Date value) {
            addCriterion("FinishedAt >", value, "finishedat");
            return (Criteria)this;
        }

        public Criteria andFinishedatGreaterThanOrEqualTo(Date value) {
            addCriterion("FinishedAt >=", value, "finishedat");
            return (Criteria)this;
        }

        public Criteria andFinishedatLessThan(Date value) {
            addCriterion("FinishedAt <", value, "finishedat");
            return (Criteria)this;
        }

        public Criteria andFinishedatLessThanOrEqualTo(Date value) {
            addCriterion("FinishedAt <=", value, "finishedat");
            return (Criteria)this;
        }

        public Criteria andFinishedatIn(List<Date> values) {
            addCriterion("FinishedAt in", values, "finishedat");
            return (Criteria)this;
        }

        public Criteria andFinishedatNotIn(List<Date> values) {
            addCriterion("FinishedAt not in", values, "finishedat");
            return (Criteria)this;
        }

        public Criteria andFinishedatBetween(Date value1, Date value2) {
            addCriterion("FinishedAt between", value1, value2, "finishedat");
            return (Criteria)this;
        }

        public Criteria andFinishedatNotBetween(Date value1, Date value2) {
            addCriterion("FinishedAt not between", value1, value2, "finishedat");
            return (Criteria)this;
        }

        public Criteria andCreatedatIsNull() {
            addCriterion("CreatedAt is null");
            return (Criteria)this;
        }

        public Criteria andCreatedatIsNotNull() {
            addCriterion("CreatedAt is not null");
            return (Criteria)this;
        }

        public Criteria andCreatedatEqualTo(Date value) {
            addCriterion("CreatedAt =", value, "createdat");
            return (Criteria)this;
        }

        public Criteria andCreatedatNotEqualTo(Date value) {
            addCriterion("CreatedAt <>", value, "createdat");
            return (Criteria)this;
        }

        public Criteria andCreatedatGreaterThan(Date value) {
            addCriterion("CreatedAt >", value, "createdat");
            return (Criteria)this;
        }

        public Criteria andCreatedatGreaterThanOrEqualTo(Date value) {
            addCriterion("CreatedAt >=", value, "createdat");
            return (Criteria)this;
        }

        public Criteria andCreatedatLessThan(Date value) {
            addCriterion("CreatedAt <", value, "createdat");
            return (Criteria)this;
        }

        public Criteria andCreatedatLessThanOrEqualTo(Date value) {
            addCriterion("CreatedAt <=", value, "createdat");
            return (Criteria)this;
        }

        public Criteria andCreatedatIn(List<Date> values) {
            addCriterion("CreatedAt in", values, "createdat");
            return (Criteria)this;
        }

        public Criteria andCreatedatNotIn(List<Date> values) {
            addCriterion("CreatedAt not in", values, "createdat");
            return (Criteria)this;
        }

        public Criteria andCreatedatBetween(Date value1, Date value2) {
            addCriterion("CreatedAt between", value1, value2, "createdat");
            return (Criteria)this;
        }

        public Criteria andCreatedatNotBetween(Date value1, Date value2) {
            addCriterion("CreatedAt not between", value1, value2, "createdat");
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