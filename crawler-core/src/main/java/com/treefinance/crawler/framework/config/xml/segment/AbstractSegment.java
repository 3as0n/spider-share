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

package com.treefinance.crawler.framework.config.xml.segment;

import com.datatrees.common.util.json.annotation.Description;
import com.treefinance.crawler.framework.config.annotation.Attr;
import com.treefinance.crawler.framework.config.annotation.ChildTag;
import com.treefinance.crawler.framework.config.annotation.Node;
import com.treefinance.crawler.framework.config.annotation.Tag;
import com.treefinance.crawler.framework.config.enums.SegmentType;
import com.treefinance.crawler.framework.config.xml.extractor.FieldExtractor;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author <A HREF="mailto:wangcheng@datatrees.com.cn">Cheng Wang</A>
 * @version 1.0
 * @since Feb 7, 2014 4:56:49 PM
 */
@Description(value = "type", keys = {"XPATH", "JSONPATH", "REGEX", "SPLIT", "CALCULATE", "BASE"},
    types = {XpathSegment.class, JsonPathSegment.class, RegexSegment.class, SplitSegment.class, CalculateSegment.class, BaseSegment.class})
public abstract class AbstractSegment implements Serializable {

    /**  */
    private static final long serialVersionUID = -5652099003079996052L;

    private SegmentType type;

    private String name;

    private String resultClass;

    // switch for whether return list or list.get(0)
    private Boolean popReturn;

    private List<FieldExtractor> fieldExtractorList;

    private List<AbstractSegment> segmentList;

    private String sourceId;

    private Boolean notEmpty;

    private Integer maxCycles;

    private Boolean isReverse;

    private String breakPattern;

    private Boolean standBy;

    private String contains;

    private String disContains;

    private Boolean merge;

    private Integer containsFlag;

    private Integer disContainsFlag;

    private Integer breakPatternFlag;

    private String businessType;

    public AbstractSegment() {
        super();
        type = SegmentType.BASE;
        fieldExtractorList = new ArrayList<FieldExtractor>();
        segmentList = new ArrayList<AbstractSegment>();
    }

    @Attr("merge")
    public Boolean getMerge() {
        return merge;
    }

    @Node("@merge")
    public void setMerge(Boolean merge) {
        this.merge = merge;
    }

    @Attr("contains")
    public String getContains() {
        return contains;
    }

    @Node("@contains")
    public void setContains(String contains) {
        this.contains = contains;
    }

    @Attr("dis-contains")
    public String getDisContains() {
        return disContains;
    }

    @Node("@dis-contains")
    public void setDisContains(String disContains) {
        this.disContains = disContains;
    }

    @Attr("pop-return")
    public Boolean getPopReturn() {
        return popReturn;
    }

    @Node("@pop-return")
    public void setPopReturn(Boolean popReturn) {
        this.popReturn = popReturn;
    }

    @Attr("name")
    public String getName() {
        return name;
    }

    @Node("@name")
    public void setName(String name) {
        this.name = StringUtils.trim(name);
    }

    @Attr("result-class")
    public String getResultClass() {
        return resultClass;
    }

    @Node("@result-class")
    public void setResultClass(String resultClass) {
        this.resultClass = StringUtils.trim(resultClass);
    }

    @ChildTag("object-segment")
    public List<AbstractSegment> getSegmentList() {
        return Collections.unmodifiableList(segmentList);
    }

    @Node(value = "object-segment", types = {XpathSegment.class, JsonPathSegment.class, RegexSegment.class, SplitSegment.class, CalculateSegment.class, BaseSegment.class})
    public void setSegmentList(AbstractSegment segment) {
        this.segmentList.add(segment);
    }

    @Tag
    public List<FieldExtractor> getFieldExtractorList() {
        return Collections.unmodifiableList(fieldExtractorList);
    }

    @Node("field-extractor")
    public void setFieldExtractorList(FieldExtractor fieldExtractor) {
        this.fieldExtractorList.add(fieldExtractor);
    }

    @Attr("type")
    public SegmentType getType() {
        return type;
    }

    @Node("@type")
    public void setType(String type) {
        this.type = SegmentType.getSegmentType(type);
    }

    @Attr("source")
    public String getSourceId() {
        return sourceId;
    }

    @Node("@source")
    public void setSourceId(String sourceId) {
        this.sourceId = StringUtils.trim(sourceId);
    }

    @Attr("not-empty")
    public Boolean getNotEmpty() {
        return notEmpty;
    }

    @Node("@not-empty")
    public void setNotEmpty(Boolean notEmpty) {
        this.notEmpty = notEmpty;
    }

    @Attr("max-cycles")
    public Integer getMaxCycles() {
        return maxCycles == null || maxCycles < -1 ? Integer.valueOf(-1) : maxCycles;
    }

    @Node("@max-cycles")
    public void setMaxCycles(Integer maxCycles) {
        this.maxCycles = maxCycles;
    }

    @Attr("is-reverse")
    public Boolean getIsReverse() {
        return isReverse;
    }

    @Node("@is-reverse")
    public void setIsReverse(Boolean isReverse) {
        this.isReverse = isReverse;
    }

    @Attr("stand-by")
    public Boolean getStandBy() {
        return standBy;
    }

    @Node("@stand-by")
    public void setStandBy(Boolean standBy) {
        this.standBy = standBy;
    }

    @Attr("break-pattern")
    public String getBreakPattern() {
        return breakPattern;
    }

    @Node("@break-pattern")
    public void setBreakPattern(String breakPattern) {
        this.breakPattern = breakPattern;
    }

    @Attr("contains-flag")
    public Integer getContainsFlag() {
        return containsFlag;
    }

    @Node("@contains-flag")
    public void setContainsFlag(Integer containsFlag) {
        this.containsFlag = containsFlag;
    }

    @Attr("dis-contains-flag")
    public Integer getDisContainsFlag() {
        return disContainsFlag;
    }

    @Node("@dis-contains-flag")
    public void setDisContainsFlag(Integer disContainsFlag) {
        this.disContainsFlag = disContainsFlag;
    }

    @Attr("break-pattern-flag")
    public Integer getBreakPatternFlag() {
        return breakPatternFlag;
    }

    @Node("@break-pattern-flag")
    public void setBreakPatternFlag(Integer breakPatternFlag) {
        this.breakPatternFlag = breakPatternFlag;
    }

    @Attr("business-type")
    public String getBusinessType() {
        return businessType;
    }

    @Node("@business-type")
    public void setBusinessType(String businessType) {
        this.businessType = StringUtils.trim(businessType);
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Segment [type=" + type + ", name=" + name + ", businessType=" + businessType + "]";
    }

}
