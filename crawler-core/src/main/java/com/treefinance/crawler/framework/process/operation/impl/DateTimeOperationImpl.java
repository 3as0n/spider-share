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

package com.treefinance.crawler.framework.process.operation.impl;

import com.treefinance.crawler.framework.config.enums.operation.datetime.BaseType;
import com.treefinance.crawler.framework.config.enums.operation.datetime.DateTimeFieldType;
import com.treefinance.crawler.framework.config.xml.extractor.FieldExtractor;
import com.treefinance.crawler.framework.config.xml.operation.DateTimeOperation;
import com.treefinance.crawler.framework.context.function.SpiderRequest;
import com.treefinance.crawler.framework.context.function.SpiderResponse;
import com.treefinance.crawler.framework.exception.InvalidOperationException;
import com.treefinance.crawler.framework.expression.ExpressionEngine;
import com.treefinance.crawler.framework.process.operation.Operation;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.DurationFieldType;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import javax.annotation.Nonnull;

/**
 * @author likun
 * @version $Id: DateTimeOperationImpl.java, v 0.1 Jul 22, 2015 11:57:20 AM likun Exp $
 */
public class DateTimeOperationImpl extends Operation<DateTimeOperation> {

    public DateTimeOperationImpl(@Nonnull DateTimeOperation operation, @Nonnull FieldExtractor extractor) {
        super(operation, extractor);
    }

    @Override
    protected void validate(@Nonnull DateTimeOperation operation, @Nonnull SpiderRequest request, @Nonnull SpiderResponse response) throws Exception {
        super.validate(operation, request, response);

        if (operation.getBaseType() == null) {
            throw new InvalidOperationException("Invalid datetime operation! - Attribute 'base-type' must not be null.");
        }
    }

    @Override
    protected Object doOperation(@Nonnull DateTimeOperation operation, @Nonnull Object operatingData, @Nonnull SpiderRequest request, @Nonnull SpiderResponse response)
        throws Exception {
        BaseType baseType = operation.getBaseType();

        ExpressionEngine expressionEngine = new ExpressionEngine(request, response);

        int offset = 0;
        if (StringUtils.isNotBlank(operation.getOffset())) {
            String offsetString = expressionEngine.eval(operation.getOffset());
            offset = Integer.parseInt(offsetString);
        }

        DateTimeFieldType dateTimeFieldType = operation.getDateTimeFieldType();

        DateTime baseDateTime = buildBaseDateTime(baseType, operatingData, expressionEngine, operation.getSourceFormat());

        baseDateTime = addDateTimeOffset(baseDateTime, dateTimeFieldType, offset);

        if (BooleanUtils.isTrue(operation.getCalibrate())) {
            baseDateTime = timeCalibrate(baseDateTime, baseType);
        }

        Object result;
        String format = operation.getFormat();
        if (StringUtils.isNotBlank(format)) {
            if (format.matches("^\\s*timestamp\\s*$")) {
                result = baseDateTime.toDate().getTime();
            } else {
                result = baseDateTime.toString(format);
            }
        } else {
            result = baseDateTime.toDate();
        }

        return result;
    }

    @Nonnull
    private DateTime addDateTimeOffset(@Nonnull DateTime baseDateTime, DateTimeFieldType dateTimeFieldType, int offset) {
        if (offset == 0 || dateTimeFieldType == null) {
            return baseDateTime;
        }

        DateTime rawResultObj;
        switch (dateTimeFieldType) {
            case YEAR: {
                rawResultObj = baseDateTime.withFieldAdded(DurationFieldType.years(), offset);
                break;
            }
            case MONTH: {
                rawResultObj = baseDateTime.withFieldAdded(DurationFieldType.months(), offset);
                break;
            }
            case WEEK: {
                rawResultObj = baseDateTime.withFieldAdded(DurationFieldType.weeks(), offset);
                break;
            }
            case WEEK_YEAR: {
                rawResultObj = baseDateTime.withFieldAdded(DurationFieldType.weekyears(), offset);
                break;
            }
            case DATE: {
                rawResultObj = baseDateTime.withFieldAdded(DurationFieldType.days(), offset);
                break;
            }
            case HOUR: {
                rawResultObj = baseDateTime.withFieldAdded(DurationFieldType.hours(), offset);
                break;
            }
            case MINUTE: {
                rawResultObj = baseDateTime.withFieldAdded(DurationFieldType.minutes(), offset);
                break;
            }
            default: {
                rawResultObj = baseDateTime.withFieldAdded(DurationFieldType.seconds(), offset);
                break;
            }
        }
        return rawResultObj;
    }

    @Nonnull
    private DateTime buildBaseDateTime(@Nonnull BaseType baseType, @Nonnull Object operatingData, @Nonnull ExpressionEngine expressionEngine, String datePattern) {
        DateTime baseDateTime;
        switch (baseType) {
            case NOW: {
                baseDateTime = new DateTime();
                break;
            }
            case FIRST_DAY_OF_THIS_WEEK: {
                baseDateTime = new LocalDate().dayOfWeek().withMinimumValue().toDateTimeAtStartOfDay();
                break;
            }
            case LAST_DAY_OF_THIS_WEEK: {
                baseDateTime = new LocalDate().dayOfWeek().withMaximumValue().toDateTimeAtStartOfDay();
                break;
            }
            case FIRST_DAY_OF_THIS_MONTH: {
                baseDateTime = new LocalDate().dayOfMonth().withMinimumValue().toDateTimeAtStartOfDay();
                break;
            }
            case LAST_DAY_OF_THIS_MONTH: {
                baseDateTime = new LocalDate().dayOfMonth().withMaximumValue().toDateTimeAtStartOfDay();
                break;
            }
            case FIRST_DAY_OF_THIS_YEAR: {
                baseDateTime = new LocalDate().dayOfYear().withMinimumValue().toDateTimeAtStartOfDay();
                break;
            }
            case LAST_DAY_OF_THIS_YEAR: {
                baseDateTime = new LocalDate().dayOfYear().withMaximumValue().toDateTimeAtStartOfDay();
                break;
            }
            default: {
                logger.debug("custom src: {}", operatingData);

                if (operatingData instanceof String) {
                    String val = expressionEngine.eval((String)operatingData);

                    String sourceFormat = StringUtils.isBlank(datePattern) ? "yyyy-MM-dd" : datePattern;
                    DateTimeFormatter df = DateTimeFormat.forPattern(sourceFormat);
                    baseDateTime = df.parseDateTime(val);
                } else {
                    baseDateTime = new DateTime(operatingData);
                }
                break;
            }
        }
        return baseDateTime;
    }

    @Nonnull
    private DateTime timeCalibrate(@Nonnull DateTime baseDateTime, @Nonnull BaseType baseType) {
        switch (baseType) {
            case FIRST_DAY_OF_THIS_WEEK: {
                return baseDateTime.dayOfWeek().withMinimumValue();
            }
            case LAST_DAY_OF_THIS_WEEK: {
                return baseDateTime.dayOfWeek().withMaximumValue();
            }
            case FIRST_DAY_OF_THIS_MONTH: {
                return baseDateTime.dayOfMonth().withMinimumValue();
            }
            case LAST_DAY_OF_THIS_MONTH: {
                return baseDateTime.dayOfMonth().withMaximumValue();
            }
            case FIRST_DAY_OF_THIS_YEAR: {
                return baseDateTime.dayOfYear().withMinimumValue();
            }
            case LAST_DAY_OF_THIS_YEAR: {
                return baseDateTime.dayOfYear().withMaximumValue();
            }
            default: {
                return baseDateTime;
            }
        }
    }
}
