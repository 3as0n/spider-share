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

package com.treefinance.crawler.framework.config.enums;

import com.datatrees.spider.share.domain.website.WebsiteType;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * User: yand Date: 2018/4/9
 */
public enum BusinessType {
    /**
     * 电商类业务
     */
    BASE_INFO("base_info", "个人信息", WebsiteType.ECOMMERCE, 0, true, false),
    HUABEI("huabei", "花呗信息", WebsiteType.ECOMMERCE, 1),
    MY_RATE("my_rate", "买家信用", WebsiteType.ECOMMERCE, 2),
    BANK_CARD("bank_card", "银行卡信息", WebsiteType.ECOMMERCE, 3),
    FEES("fees", "缴费信息", WebsiteType.ECOMMERCE, 5),
    ADDRESS("address", "收货地址", WebsiteType.ECOMMERCE, 4),
    ZM_CREDIT("zm_credit", "芝麻分", WebsiteType.ECOMMERCE, 9, false),
    TAOBAO_RECORD("taobao_record", "淘宝交易记录", WebsiteType.ECOMMERCE, 7),
    ALIPAY_RECORD("alipay_record", "支付宝交易记录", WebsiteType.ECOMMERCE, 6),
    TRADE_ADDRESS("trade_address", "淘宝交易地址", WebsiteType.ECOMMERCE, 8, false),
    /**
     * 运营商
     */
    PERSONAL_INFO("personal_info", "基本信息", WebsiteType.OPERATOR, 0, true, false),
    BILL_DETAILS("bill_details", "账单信息", WebsiteType.OPERATOR, 1),
    CALL_DETAILS("call_details", "通话详单", WebsiteType.OPERATOR, 2);

    private String code;

    private String name;

    private WebsiteType websiteType;

    private int order;

    private boolean open; // 是否抓取

    private boolean enable; // 是否控制

    BusinessType(String code, String name, WebsiteType websiteType, int order) {
        this(code, name, websiteType, order, true);
    }

    BusinessType(String code, String name, WebsiteType websiteType, int order, boolean open) {
        this(code, name, websiteType, order, open, true);
    }

    BusinessType(String code, String name, WebsiteType websiteType, int order, boolean open, boolean enable) {
        this.code = code;
        this.name = name;
        this.websiteType = websiteType;
        this.order = order;
        this.open = open;
        this.enable = enable;
    }

    public static BusinessType getBusinessType(String code) {
        return Arrays.stream(BusinessType.values()).filter(businessType -> businessType.getCode().equals(code)).findFirst().orElse(null);
    }

    public static Map<WebsiteType, List<BusinessType>> getGroup() {
        return Holder.MAP;
    }

    public static List<BusinessType> getBusinessTypeList(WebsiteType websiteType) {
        return getGroup().get(websiteType);
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public WebsiteType getWebsiteType() {
        return websiteType;
    }

    public int getOrder() {
        return order;
    }

    public boolean isOpen() {
        return open;
    }

    public boolean isEnable() {
        return enable;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE).append("code", code).append("name", name).append("websiteType", websiteType).append("open", open)
            .append("enable", enable).toString();
    }

    private static class Holder {

        private static final Map<WebsiteType, List<BusinessType>> MAP = Arrays.stream(BusinessType.values()).collect(Collectors.groupingBy(BusinessType::getWebsiteType));
    }
}