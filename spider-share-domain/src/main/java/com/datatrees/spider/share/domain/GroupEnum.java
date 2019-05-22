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

package com.datatrees.spider.share.domain;

import com.datatrees.spider.share.domain.website.WebsiteType;

import java.util.HashMap;
import java.util.Map;

/**
 * 爬虫分组 Created by zhouxinghai on 2017/6/22
 */
public enum GroupEnum {

    EXMAIL_QQ_H5("EXMAIL_QQ_H5", "腾讯企业邮箱H5", WebsiteType.MAIL, "exmail.qq.com.h5"),
    MAIL_SINA_H5("MAIL_SINA_H5", "新浪邮箱H5", WebsiteType.MAIL, "sina.com.h5"),
    MAIL_QQ_H5("MAIL_QQ_H5", "QQ邮箱H5", WebsiteType.MAIL, "qq.com.h5"),
    MAIL_163_H5("MAIL_163_H5", "163邮箱H5", WebsiteType.MAIL, "163.com.h5"),
    MAIL_126_H5("MAIL_126_H5", "126邮箱H5", WebsiteType.MAIL, "126.com.h5"),
    TAOBAO_COM_H5("TAOBAO_COM_H5", "淘宝H5", WebsiteType.ECOMMERCE, "taobao.com.h5"),
    PASSWORD_TAOBAO_COM_H5("PASSWORD_TAOBAO_COM_H5", "淘宝H5密码登录", WebsiteType.ECOMMERCE, "password.taobao.com.h5"),
    MAIL_QQ("MAIL_QQ", "QQ邮箱", WebsiteType.MAIL, "qq.com"),
    MAIL_163("MAIL_163", "163邮箱", WebsiteType.MAIL, "163.com"),
    MAIL_126("MAIL_126", "126邮箱", WebsiteType.MAIL, "126.com"),
    MAIL_SINA("MAIL_SINA", "新浪邮箱", WebsiteType.MAIL, "sina.com"),
    MAIL_139("MAIL_139", "139邮箱", WebsiteType.MAIL, "139.com"),
    ALIPAY_COM("ALIPAY_COM", "支付宝", WebsiteType.ECOMMERCE, "alipay.com"),
    TAOBAO_COM("TAOBAO_COM", "淘宝", WebsiteType.ECOMMERCE, "taobao.com"),
    CHINA_10086("CHINA_10086", "中国移动", WebsiteType.OPERATOR, ""),
    CHINA_10000("CHINA_10000", "中国电信", WebsiteType.OPERATOR, ""),
    CHINA_10010("CHINA_10010", "中国联通", WebsiteType.OPERATOR, ""),
    ZHE_JIANG_10086("ZHE_JIANG_10086", "浙江移动", WebsiteType.OPERATOR, ""),
    ZHE_JIANG_10000("ZHE_JIANG_10000", "浙江电信", WebsiteType.OPERATOR, ""),
    GUANG_DONG_10086("GUANG_DONG_10086", "广东移动", WebsiteType.OPERATOR, ""),
    GUANG_DONG_10000("GUANG_DONG_10000", "广东电信", WebsiteType.OPERATOR, ""),
    JIANG_SU_10086("JIANG_SU_10086", "江苏移动", WebsiteType.OPERATOR, ""),
    JIANG_SU_10000("JIANG_SU_10000", "江苏电信", WebsiteType.OPERATOR, ""),
    SHAN_DONG_10086("SHAN_DONG_10086", "山东移动", WebsiteType.OPERATOR, ""),
    SHAN_DONG_10000("SHAN_DONG_10000", "山东电信", WebsiteType.OPERATOR, ""),
    HU_NAN_10086("HU_NAN_10086", "湖南移动", WebsiteType.OPERATOR, ""),
    HU_NAN_10000("HU_NAN_10000", "湖南电信", WebsiteType.OPERATOR, ""),
    BEI_JING_10086("BEI_JING_10086", "北京移动", WebsiteType.OPERATOR, ""),
    BEI_JING_10000("BEI_JING_10000", "北京电信", WebsiteType.OPERATOR, ""),
    TIAN_JIN_10086("TIAN_JIN_10086", "天津移动", WebsiteType.OPERATOR, ""),
    TIAN_JIN_10000("TIAN_JIN_10000", "天津电信", WebsiteType.OPERATOR, ""),
    SHANG_HAI_10086("SHANG_HAI_10086", "上海移动", WebsiteType.OPERATOR, ""),
    SHANG_HAI_10000("SHANG_HAI_10000", "上海电信", WebsiteType.OPERATOR, ""),
    CHONG_QING_10086("CHONG_QING_10086", "重庆移动", WebsiteType.OPERATOR, ""),
    CHONG_QING_10000("CHONG_QING_10000", "重庆电信", WebsiteType.OPERATOR, ""),
    HU_BEI_10086("HU_BEI_10086", "湖北移动", WebsiteType.OPERATOR, ""),
    HU_BEI_10000("HU_BEI_10000", "湖北电信", WebsiteType.OPERATOR, ""),
    SI_CHUAN_10086("SI_CHUAN_10086", "四川移动", WebsiteType.OPERATOR, ""),
    SI_CHUAN_10000("SI_CHUAN_10000", "四川电信", WebsiteType.OPERATOR, ""),
    AN_HUI_10086("AN_HUI_10086", "安徽移动", WebsiteType.OPERATOR, ""),
    AN_HUI_10000("AN_HUI_10000", "安徽电信", WebsiteType.OPERATOR, ""),
    HE_NAN_10086("HE_NAN_10086", "河南移动", WebsiteType.OPERATOR, ""),
    HE_NAN_10000("HE_NAN_10000", "河南电信", WebsiteType.OPERATOR, ""),
    JIANG_XI_10086("JIANG_XI_10086", "江西移动", WebsiteType.OPERATOR, ""),
    JIANG_XI_10000("JIANG_XI_10000", "江西电信", WebsiteType.OPERATOR, ""),
    HE_BEI_10086("HE_BEI_10086", "河北移动", WebsiteType.OPERATOR, ""),
    HE_BEI_10000("HE_BEI_10000", "河北电信", WebsiteType.OPERATOR, ""),
    FU_JIAN_10086("FU_JIAN_10086", "福建移动", WebsiteType.OPERATOR, ""),
    FU_JIAN_10000("FU_JIAN_10000", "福建电信", WebsiteType.OPERATOR, ""),
    SHAN_XI_TY_10086("SHAN_XI_TY_10086", "山西移动", WebsiteType.OPERATOR, ""),
    SHAN_XI_TY_10000("SHAN_XI_TY_10000", "山西电信", WebsiteType.OPERATOR, ""),
    JI_LIN_10086("JI_LIN_10086", "吉林移动", WebsiteType.OPERATOR, ""),
    JI_LIN_10000("JI_LIN_10000", "吉林电信", WebsiteType.OPERATOR, ""),
    LIAO_NING_10086("LIAO_NING_10086", "辽宁移动", WebsiteType.OPERATOR, ""),
    LIAO_NING_10000("LIAO_NING_10000", "辽宁电信", WebsiteType.OPERATOR, ""),
    HEI_LONG_JIANG_10086("HEI_LONG_JIANG_10086", "黑龙江移动", WebsiteType.OPERATOR, ""),
    HEI_LONG_JIANG_10000("HEI_LONG_JIANG_10000", "黑龙江电信", WebsiteType.OPERATOR, ""),
    YUN_NAN_10086("YUN_NAN_10086", "云南移动", WebsiteType.OPERATOR, ""),
    YUN_NAN_10000("YUN_NAN_10000", "云南电信", WebsiteType.OPERATOR, ""),
    GUANG_XI_10086("GUANG_XI_10086", "广西移动", WebsiteType.OPERATOR, ""),
    GUANG_XI_10000("GUANG_XI_10000", "广西电信", WebsiteType.OPERATOR, ""),
    SHAN_XI_XA_10086("SHAN_XI_XA_10086", "陕西移动", WebsiteType.OPERATOR, ""),
    SHAN_XI_XA_10000("SHAN_XI_XA_10000", "陕西电信", WebsiteType.OPERATOR, ""),
    GAN_SU_10086("GAN_SU_10086", "甘肃移动", WebsiteType.OPERATOR, ""),
    GAN_SU_10000("GAN_SU_10000", "甘肃电信", WebsiteType.OPERATOR, ""),
    NEI_MENG_GU_10086("NEI_MENG_GU_10086", "内蒙古移动", WebsiteType.OPERATOR, ""),
    NEI_MENG_GU_10000("NEI_MENG_GU_10000", "内蒙古电信", WebsiteType.OPERATOR, ""),
    HAI_NAN_10086("HAI_NAN_10086", "海南移动", WebsiteType.OPERATOR, ""),
    HAI_NAN_10000("HAI_NAN_10000", "海南电信", WebsiteType.OPERATOR, ""),
    GUI_ZHOU_10086("GUI_ZHOU_10086", "贵州移动", WebsiteType.OPERATOR, ""),
    QING_HAI_10086("QING_HAI_10086", "青海移动", WebsiteType.OPERATOR, ""),
    NING_XIA_10086("NING_XIA_10086", "宁夏移动", WebsiteType.OPERATOR, ""),
    EDUCATION("EDUCATION", "学信网", WebsiteType.EDUCATION, "chsi.com.cn");

    private static final Map<String, String> groupMap = new HashMap<>();

    static {
        for (GroupEnum e : GroupEnum.values()) {
            groupMap.put(e.groupCode, e.groupName);
        }

    }

    /**
     * 分组代码
     */
    private final String groupCode;

    /**
     * 分组名称
     */
    private final String groupName;

    /**
     * 默认爬虫配置,权重里没有大于0的配置,启用默认
     */
    private final String websiteName;

    /**
     * 配置类型
     */
    private final WebsiteType websiteType;

    GroupEnum(String groupCode, String groupName, WebsiteType websiteType, String websiteName) {
        this.groupCode = groupCode;
        this.groupName = groupName;
        this.websiteName = websiteName;
        this.websiteType = websiteType;
    }

    public static GroupEnum getByGroupCode(String groupCode) {
        for (GroupEnum e : GroupEnum.values()) {
            if (e.groupCode.equals(groupCode)) {
                return e;
            }
        }
        return null;
    }

    public static GroupEnum getByWebsiteName(String websiteName) {
        for (GroupEnum e : GroupEnum.values()) {
            if (e.getWebsiteName().equals(websiteName)) {
                return e;
            }
        }
        return null;
    }

    public static String getGroupName(String groupCode) {
        return groupMap.get(groupCode);
    }

    public String getGroupCode() {
        return groupCode;
    }

    public String getGroupName() {
        return groupName;
    }

    public String getWebsiteName() {
        return websiteName;
    }

    public WebsiteType getWebsiteType() {
        return websiteType;
    }

    // public static void main(String[] args) {
    // for (GroupEnum e : GroupEnum.values()) {
    // if (!e.name().endsWith(e.getGroupCode())) {
    // System.out.println(e.groupCode);
    // }
    // }
    //
    //
    // }
}
