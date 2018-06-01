/**
 * This document and its contents are protected by copyright 2015 and owned by datatrees.com Inc.
 * The copying and reproduction of this document and/or its content (whether wholly or partly) or
 * any incorporation of the same into any other material in any media or format of any kind is
 * strictly prohibited. All rights are reserved.
 * Copyright (c) datatrees.com Inc. 2015
 */

package com.datatrees.crawler.core.processor.common;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

import com.datatrees.common.conf.Configuration;
import com.datatrees.crawler.core.domain.config.extractor.ResultType;
import com.datatrees.crawler.core.domain.config.operation.AbstractOperation;
import com.datatrees.crawler.core.domain.config.operation.OperationType;
import com.datatrees.crawler.core.domain.config.segment.AbstractSegment;
import com.datatrees.crawler.core.domain.config.segment.SegmentType;
import com.datatrees.crawler.core.domain.config.service.AbstractService;
import com.datatrees.crawler.core.domain.config.service.ServiceType;
import com.datatrees.crawler.core.processor.format.AbstractFormat;
import com.datatrees.crawler.core.processor.format.impl.*;
import com.datatrees.crawler.core.processor.operation.Operation;
import com.datatrees.crawler.core.processor.operation.impl.*;
import com.datatrees.crawler.core.processor.segment.SegmentBase;
import com.datatrees.crawler.core.processor.segment.impl.*;
import com.datatrees.crawler.core.processor.service.ServiceBase;
import com.datatrees.crawler.core.processor.service.impl.DefaultService;
import com.datatrees.crawler.core.processor.service.impl.GrabServiceImpl;
import com.datatrees.crawler.core.processor.service.impl.PluginServiceImpl;
import com.datatrees.crawler.core.processor.service.impl.TaskHttpServiceImpl;
import com.treefinance.crawler.exception.UnexpectedException;

/**
 * @author <A HREF="mailto:wangcheng@datatrees.com.cn">Cheng Wang</A>
 * @version 1.0
 * @since Mar 3, 2014 1:23:56 PM
 */
public final class ProcessorFactory {

    private static final Map<Enum, Class> REGISTER = new HashMap<>();

    static {
        register(SegmentType.BASE, BaseSegmentImpl.class);
        register(SegmentType.REGEX, RegexSegmentImpl.class);
        register(SegmentType.XPATH, XpathSegmentImpl.class);
        register(SegmentType.JSONPATH, JsonPathSegmentImpl.class);
        register(SegmentType.SPLIT, SplitSegmentImpl.class);
        register(SegmentType.CALCULATE, CalculateSegmentImpl.class);

        register(OperationType.PARSER, ParserOperationImpl.class);
        register(OperationType.XPATH, XpathOperationImpl.class);
        register(OperationType.JSONPATH, JsonPathOperationImpl.class);
        register(OperationType.REGEX, RegexOperationImpl.class);
        register(OperationType.REPLACE, ReplaceOperationImpl.class);
        register(OperationType.TEMPLATE, TemplateOperationImpl.class);
        register(OperationType.CODEC, CodecOperationImpl.class);
        register(OperationType.RETURN, ReturnOperationImpl.class);
        register(OperationType.TRIM, TrimOperationImpl.class);
        register(OperationType.SET, SetOperationImpl.class);
        register(OperationType.EXTRACT, ExtractOperationImpl.class);
        register(OperationType.APPEND, AppendOperationImpl.class);
        register(OperationType.MATCHGROUP, MatchGroupOperationImpl.class);
        register(OperationType.DATETIME, DateTimeOperationImpl.class);
        register(OperationType.TRIPLE, TripleOperationImpl.class);
        register(OperationType.MAILPARSER, MailParserOperationImpl.class);
        register(OperationType.CALCULATE, CalculateOperationImpl.class);
        register(OperationType.ESCAPE, EscapeOperationImpl.class);
        register(OperationType.DECODE, DecodeOperationImpl.class);
        register(OperationType.PROXYSET, ProxySetOperationImpl.class);
        register(OperationType.MAPPING, MappingOperationImpl.class);
        register(OperationType.SLEEP, SleepOperationImpl.class);
        register(OperationType.RETURNMATCH, ReturnMatchOperationImpl.class);

        register(ServiceType.Plugin_Service, PluginServiceImpl.class);
        register(ServiceType.Grab_Service, GrabServiceImpl.class);
        register(ServiceType.Task_Http_Service, TaskHttpServiceImpl.class);
        register(ServiceType.Default, DefaultService.class);


            // format
        register(ResultType.DATE, DateFormatImpl.class);
        register(ResultType.String, StringFormatImpl.class);
        register(ResultType.NUMBER, NumberFormatImpl.class);
        register(ResultType.PAYMENT, PaymentFormatImpl.class);
        register(ResultType.FILE, FileFormatImpl.class);
        register(ResultType.RESOURCESTRING, ResourceStringFormatImpl.class);

        register(ResultType.CURRENCY, CurrencyFormatImpl.class);
        register(ResultType.CURRENCYPAYMENT, CurrencyPaymentFormatImpl.class);
        register(ResultType.RMB, RMBFormatImpl.class);
        register(ResultType.BOOLEAN, BooleanFormatImpl.class);
        register(ResultType.INT, IntFormatImpl.class);
        register(ResultType.LONG, LongFormatImpl.class);
    }

    private ProcessorFactory() {
    }

    public static void register(Enum type, Class clazz) {
        REGISTER.put(type, clazz);
    }

    @Nonnull
    private static <R> Class<R> searchRegister(Enum type, String name) {
        Class<R> clazz = REGISTER.get(type);
        if (clazz == null) {
            throw new UnexpectedException("Not found the " + name + " processor in register. " + name + "-type: " + type);
        }
        return clazz;
    }

    public static <T extends AbstractSegment, R extends SegmentBase<T>> R getSegment(T segment) {
        SegmentType type = segment.getType();

        Class<R> clazz = searchRegister(type, "segment");

        R bean;
        try {
            bean = clazz.newInstance();
        } catch (Exception e) {
            throw new UnexpectedException("Error new instance for class: " + clazz, e);
        }
        bean.setSegment(segment);

        return bean;
    }

    public static <T extends AbstractOperation, R extends Operation<T>> R getOperation(T op) {
        OperationType type = op.getType();

        Class<R> clazz = searchRegister(type, "segment");

        R bean;
        try {
            bean = clazz.newInstance();
        } catch (Exception e) {
            throw new UnexpectedException("Error new instance for class: " + clazz, e);
        }
        bean.setOperation(op);

        return bean;
    }

    public static ServiceBase getService(AbstractService service) {
        boolean isDefault = service == null;
        ServiceType type = isDefault ? ServiceType.Default : service.getServiceType();

        Class<ServiceBase> clazz = searchRegister(type, "service");

        ServiceBase base;
        try {
            base = clazz.newInstance();
        } catch (Exception e) {
            throw new UnexpectedException("Error new instance for class: " + clazz, e);
        }
        base.setService(service);

        return base;
    }

    public static AbstractFormat getFormat(ResultType type, Configuration conf) {
        Class<AbstractFormat> clazz = searchRegister(type, "formatter");

        AbstractFormat format;
        try {
            format = clazz.newInstance();
        } catch (Exception e) {
            throw new UnexpectedException("Error new instance for class: " + clazz, e);
        }
        format.setConf(conf);
        format.setType(type);

        return format;
    }
}
