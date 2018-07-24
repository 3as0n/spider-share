/**
 * This document and its contents are protected by copyright 2015 and owned by datatrees.com Inc.
 * The copying and reproduction of this document and/or its content (whether wholly or partly) or any
 * incorporation of the same into any other material in any media or format of any kind is strictly prohibited.
 * All rights are reserved.
 * Copyright (c) datatrees.com Inc. 2017
 */

package com.datatrees.spider.operator.domain.model.field;

import java.util.HashMap;
import java.util.Map;

/**
 * input 标签 业务类型
 * Created by zhouxinghai on 2017/6/22
 */
public enum FieldBizType {

    USERNAME("USERNAME", "手机号"),
    PASSWORD("PASSWORD", "服务密码"),
    SMS_CODE("SMS_CODE", "短信验证码"),
    PIC_CODE("PIC_CODE", "图片验证码"),
    REAL_NAME("REAL_NAME", "真实姓名"),
    ID_CARD("ID_CARD", "身份证号码");

    /**
     * bizType对应的input标签属性
     */
    public static Map<String, InputField> fields = new HashMap<>();

    static {
        for (FieldBizType type : FieldBizType.values()) {
            InputField field = new InputField();
            field.setBizType(type.getCode());
            field.setLabel(type.getRemark());
            switch (type) {
                case USERNAME:
                    field.setName("username");
                    field.setType("text");
                    field.setValidationPattern("\\d{11}");
                    field.setValidationMsg("请输入正确的手机号");
                    field.setPlaceholder("请输入手机号码");
                    break;
                case PASSWORD:
                    field.setName("password");
                    field.setType("password");
                    field.setValidationPattern("");
                    field.setValidationMsg("请输入服务密码");
                    field.setPlaceholder("请输入服务密码");
                    break;
                case SMS_CODE:
                    field.setName("randomPassword");
                    field.setType("text");
                    field.setValidationPattern("");
                    field.setValidationPattern("\\d{4,6}");
                    field.setValidationMsg("请输入短信验证码");
                    field.setPlaceholder("请输入短信验证码");
                    break;
                case PIC_CODE:
                    field.setName("code");
                    field.setType("text");
                    field.setValidationPattern("");
                    field.setValidationMsg("请输入验证码");
                    field.setPlaceholder("请输入验证码");
                    break;
                case REAL_NAME:
                    field.setName("realName");
                    field.setType("text");
                    field.setValidationPattern("");
                    field.setValidationMsg("请输入真实姓名");
                    field.setPlaceholder("请输入真实姓名");
                    break;
                case ID_CARD:
                    field.setName("idCard");
                    field.setType("text");
                    field.setValidationPattern(
                            "^(^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$)|(^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])((\\d{4})|\\d{3}[Xx])$)$");
                    field.setValidationMsg("请输入身份证号码");
                    field.setPlaceholder("请输入身份证号码");
                    break;
                default:
                    throw new RuntimeException("not support input biz type" + type.getRemark());
            }
            fields.put(type.getCode(), field);
        }
    }

    /**
     * 字段类型
     */
    private final String code;

    /**
     * 要显示的label
     */
    private final String remark;

    FieldBizType(String code, String remark) {
        this.code = code;
        this.remark = remark;
    }

    public String getCode() {
        return code;
    }

    public String getRemark() {
        return remark;
    }

    @Override
    public String toString() {
        return "FieldBizType{" + "code='" + code + '\'' + ", remark='" + remark + '\'' + '}';
    }
}