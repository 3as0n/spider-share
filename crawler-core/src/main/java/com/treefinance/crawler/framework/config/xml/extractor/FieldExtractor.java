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

package com.treefinance.crawler.framework.config.xml.extractor;

import com.treefinance.crawler.framework.config.annotation.Attr;
import com.treefinance.crawler.framework.config.annotation.Node;
import com.treefinance.crawler.framework.config.annotation.Tag;
import com.treefinance.crawler.framework.config.enums.fields.FieldVisibleType;
import com.treefinance.crawler.framework.config.enums.fields.ResultType;
import com.treefinance.crawler.framework.config.xml.AbstractBeanDefinition;
import com.treefinance.crawler.framework.config.xml.operation.AbstractOperation;
import com.treefinance.crawler.framework.config.xml.operation.AppendOperation;
import com.treefinance.crawler.framework.config.xml.operation.CalculateOperation;
import com.treefinance.crawler.framework.config.xml.operation.CodecOperation;
import com.treefinance.crawler.framework.config.xml.operation.DateTimeOperation;
import com.treefinance.crawler.framework.config.xml.operation.DecodeOperation;
import com.treefinance.crawler.framework.config.xml.operation.EscapeOperation;
import com.treefinance.crawler.framework.config.xml.operation.ExtractOperation;
import com.treefinance.crawler.framework.config.xml.operation.JsonPathOperation;
import com.treefinance.crawler.framework.config.xml.operation.MailParserOperation;
import com.treefinance.crawler.framework.config.xml.operation.MappingOperation;
import com.treefinance.crawler.framework.config.xml.operation.MatchGroupOperation;
import com.treefinance.crawler.framework.config.xml.operation.ParserOperation;
import com.treefinance.crawler.framework.config.xml.operation.ProxySetOperation;
import com.treefinance.crawler.framework.config.xml.operation.RegexOperation;
import com.treefinance.crawler.framework.config.xml.operation.ReplaceOperation;
import com.treefinance.crawler.framework.config.xml.operation.ReturnMatchOperation;
import com.treefinance.crawler.framework.config.xml.operation.ReturnOperation;
import com.treefinance.crawler.framework.config.xml.operation.SetOperation;
import com.treefinance.crawler.framework.config.xml.operation.SleepOperation;
import com.treefinance.crawler.framework.config.xml.operation.TemplateOperation;
import com.treefinance.crawler.framework.config.xml.operation.TrimOperation;
import com.treefinance.crawler.framework.config.xml.operation.TripleOperation;
import com.treefinance.crawler.framework.config.xml.operation.XpathOperation;
import com.treefinance.crawler.framework.config.xml.plugin.AbstractPlugin;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author <A HREF="mailto:wangcheng@datatrees.com.cn">Cheng Wang</A>
 * @version 1.0
 * @since Feb 7, 2014 2:23:17 PM
 */
@Tag("field-extractor")
public class FieldExtractor extends AbstractBeanDefinition implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 169636932735670442L;

    private String field;

    private String sourceId;

    private String encoding;

    private ResultType resultType;

    private String format;

    private AbstractPlugin plugin;

    private List<AbstractOperation> operationList;

    private Boolean notEmpty;

    private FieldVisibleType fieldVisibleType;

    private Boolean standBy;

    private String defaultValue;

    private String businessType;

    public FieldExtractor() {
        super();
        operationList = new ArrayList<AbstractOperation>();
    }

    @Attr("default-value")
    public String getDefaultValue() {
        return defaultValue;
    }

    @Node("@default-value")
    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    @Attr("field")
    public String getField() {
        return field;
    }

    @Node("@field")
    public void setField(String field) {
        this.field = StringUtils.trimToEmpty(field);
    }

    @Attr("source")
    public String getSourceId() {
        return sourceId;
    }

    @Node("@source")
    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    @Attr("encoding")
    public String getEncoding() {
        return encoding;
    }

    @Node("@encoding")
    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    @Attr("result-type")
    public ResultType getResultType() {
        return resultType;
    }

    @Node("@result-type")
    public void setResultType(String resultType) {
        this.resultType = ResultType.getResultType(resultType);
    }

    @Attr("field-visible-type")
    public FieldVisibleType getFieldVisibleType() {
        return fieldVisibleType;
    }

    @Node("@field-visible-type")
    public void setFieldVisibleType(String fieldVisibleType) {
        this.fieldVisibleType = FieldVisibleType.getFieldVisibleType(fieldVisibleType);
    }

    @Attr("format")
    public String getFormat() {
        return format;
    }

    @Node("@format")
    public void setFormat(String format) {
        this.format = format;
    }

    @Attr(value = "plugin-ref", referenced = true)
    public AbstractPlugin getPlugin() {
        return plugin;
    }

    @Node(value = "@plugin-ref", referenced = true)
    public void setPlugin(AbstractPlugin plugin) {
        this.plugin = plugin;
    }

    @Tag
    public List<AbstractOperation> getOperationList() {
        return Collections.unmodifiableList(operationList);
    }

    @Node(value = "operation",
        types = {ParserOperation.class, RegexOperation.class, ReplaceOperation.class, TemplateOperation.class, XpathOperation.class, JsonPathOperation.class, CodecOperation.class,
            TrimOperation.class, ReturnOperation.class, SetOperation.class, ExtractOperation.class, AppendOperation.class, MatchGroupOperation.class, DateTimeOperation.class,
            TripleOperation.class, MailParserOperation.class, CalculateOperation.class, EscapeOperation.class, DecodeOperation.class, ProxySetOperation.class,
            MappingOperation.class, SleepOperation.class, ReturnMatchOperation.class})
    public void setOperationList(AbstractOperation operation) {
        this.operationList.add(operation);
    }

    @Attr("not-empty")
    public Boolean getNotEmpty() {
        return notEmpty;
    }

    @Node("@not-empty")
    public void setNotEmpty(Boolean notEmpty) {
        this.notEmpty = notEmpty;
    }

    @Attr("stand-by")
    public Boolean getStandBy() {
        return standBy;
    }

    @Node("@stand-by")
    public void setStandBy(Boolean standBy) {
        this.standBy = standBy;
    }

    @Attr("business-type")
    public String getBusinessType() {
        return businessType;
    }

    @Node("@business-type")
    public void setBusinessType(String businessType) {
        this.businessType = StringUtils.trim(businessType);
    }

    @Override
    public String toString() {
        return "FieldExtractor [id =" + getId() + ",field=" + field + (businessType != null ? ",businessType=" + businessType : "") + "]";
    }

}
