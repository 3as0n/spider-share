package com.datatrees.crawler.core.domain;

import java.io.Serializable;

import com.datatrees.crawler.core.domain.config.ExtractorConfig;
import com.datatrees.crawler.core.domain.config.SearchConfig;

/**
 * @author <A HREF="">Cheng Wang</A>
 * @version 1.0
 * @since Jan 13, 2014 2:47:02 PM
 */
public class Website implements Serializable {

    private           Integer         id;

    private           String          websiteName;

    private           String          websiteDomain;

    private           String          websiteType;

    private           Boolean         isEnabled;

    private transient SearchConfig    searchConfig;

    private           String          searchConfigSource;

    private transient ExtractorConfig extractorConfig;

    private           String          extractorConfigSource;

    private           String          taskRegion;

    /** 配置标题 */
    private           String          websiteTitle;

    /** 分组 */
    private           String          groupCode;

    /** 分组 */
    private           String          groupName;

    public String getWebsiteTitle() {
        return websiteTitle;
    }

    public void setWebsiteTitle(String websiteTitle) {
        this.websiteTitle = websiteTitle;
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getWebsiteName() {
        return websiteName;
    }

    public void setWebsiteName(String websiteName) {
        this.websiteName = websiteName;
    }

    public String getWebsiteDomain() {
        return websiteDomain;
    }

    public void setWebsiteDomain(String websiteDomain) {
        this.websiteDomain = websiteDomain;
    }

    public String getWebsiteType() {
        return websiteType;
    }

    public void setWebsiteType(String websiteType) {
        this.websiteType = websiteType;
    }

    public Boolean getIdEnabled() {
        return isEnabled;
    }

    public void setIsEnabled(Boolean enabled) {
        isEnabled = enabled;
    }

    public SearchConfig getSearchConfig() {
        return searchConfig;
    }

    public void setSearchConfig(SearchConfig searchConfig) {
        this.searchConfig = searchConfig;
    }

    public String getSearchConfigSource() {
        return searchConfigSource;
    }

    public void setSearchConfigSource(String searchConfigSource) {
        this.searchConfigSource = searchConfigSource;
    }

    public ExtractorConfig getExtractorConfig() {
        return extractorConfig;
    }

    public void setExtractorConfig(ExtractorConfig extractorConfig) {
        this.extractorConfig = extractorConfig;
    }

    public String getExtractorConfigSource() {
        return extractorConfigSource;
    }

    public void setExtractorConfigSource(String extractorConfigSource) {
        this.extractorConfigSource = extractorConfigSource;
    }

    public String getTaskRegion() {
        return taskRegion;
    }

    public void setTaskRegion(String taskRegion) {
        this.taskRegion = taskRegion;
    }

    public boolean needProxy() {
        return searchConfig != null && searchConfig.getProperties() != null && searchConfig.getProperties().getProxy() != null;
    }
}
