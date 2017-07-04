package com.datatrees.rawdatacentral.domain.model;

import com.datatrees.rawdatacentral.domain.model.base.AbstractTask;
import java.io.Serializable;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

 /** create by system from table t_tasklog(task log info)  */
public class Task extends AbstractTask implements Serializable {
    /** mail Id */
    private Integer id;

    /** user Id */
    private Long taskId;

    /** mail website id */
    private Integer websiteId;

    /**  */
    private String nodeName;

    /**  */
    private AtomicInteger openUrlCount;

    /**  */
    private AtomicInteger openPageCount;

    /**  */
    private AtomicInteger requestFailedCount;

    /**  */
    private AtomicInteger retryCount;

    /**  */
    private AtomicInteger filteredCount;

    /** 0:init;200:success;101:cookie Invalid，102：block 103：no result */
    private Integer status;

    /**  */
    private String remark;

    /**  */
    private String resultMessage;

    /** Task duration, in seconds */
    private Long duration;

    /**  */
    private Integer extractedCount;

    /**  */
    private Integer extractSucceedCount;

    /**  */
    private Integer extractFailedCount;

    /**  */
    private Integer storeFailedCount;

    /**  */
    private Integer notExtractCount;

    /**  */
    private AtomicLong networkTraffic;

    /**  */
    private Date startedAt;

    /**  */
    private Date finishedAt;

    /**  */
    private Date createdAt;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public Integer getWebsiteId() {
        return websiteId;
    }

    public void setWebsiteId(Integer websiteId) {
        this.websiteId = websiteId;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName == null ? null : nodeName.trim();
    }

    public AtomicInteger getOpenUrlCount() {
        return openUrlCount;
    }

    public void setOpenUrlCount(AtomicInteger openUrlCount) {
        this.openUrlCount = openUrlCount;
    }

    public AtomicInteger getOpenPageCount() {
        return openPageCount;
    }

    public void setOpenPageCount(AtomicInteger openPageCount) {
        this.openPageCount = openPageCount;
    }

    public AtomicInteger getRequestFailedCount() {
        return requestFailedCount;
    }

    public void setRequestFailedCount(AtomicInteger requestFailedCount) {
        this.requestFailedCount = requestFailedCount;
    }

    public AtomicInteger getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(AtomicInteger retryCount) {
        this.retryCount = retryCount;
    }

    public AtomicInteger getFilteredCount() {
        return filteredCount;
    }

    public void setFilteredCount(AtomicInteger filteredCount) {
        this.filteredCount = filteredCount;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public String getResultMessage() {
        return resultMessage;
    }

    public void setResultMessage(String resultMessage) {
        this.resultMessage = resultMessage == null ? null : resultMessage.trim();
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public Integer getExtractedCount() {
        return extractedCount;
    }

    public void setExtractedCount(Integer extractedCount) {
        this.extractedCount = extractedCount;
    }

    public Integer getExtractSucceedCount() {
        return extractSucceedCount;
    }

    public void setExtractSucceedCount(Integer extractSucceedCount) {
        this.extractSucceedCount = extractSucceedCount;
    }

    public Integer getExtractFailedCount() {
        return extractFailedCount;
    }

    public void setExtractFailedCount(Integer extractFailedCount) {
        this.extractFailedCount = extractFailedCount;
    }

    public Integer getStoreFailedCount() {
        return storeFailedCount;
    }

    public void setStoreFailedCount(Integer storeFailedCount) {
        this.storeFailedCount = storeFailedCount;
    }

    public Integer getNotExtractCount() {
        return notExtractCount;
    }

    public void setNotExtractCount(Integer notExtractCount) {
        this.notExtractCount = notExtractCount;
    }

    public AtomicLong getNetworkTraffic() {
        return networkTraffic;
    }

    public void setNetworkTraffic(AtomicLong networkTraffic) {
        this.networkTraffic = networkTraffic;
    }

    public Date getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(Date startedAt) {
        this.startedAt = startedAt;
    }

    public Date getFinishedAt() {
        return finishedAt;
    }

    public void setFinishedAt(Date finishedAt) {
        this.finishedAt = finishedAt;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}