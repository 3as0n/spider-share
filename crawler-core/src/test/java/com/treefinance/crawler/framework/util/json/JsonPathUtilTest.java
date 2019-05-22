/*
 * Copyright © 2015 - 2017 杭州大树网络技术有限公司. All Rights Reserved
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

package com.treefinance.crawler.framework.util.json;

import com.BaseConfigTest;
import com.google.gson.reflect.TypeToken;
import com.treefinance.toolkit.util.json.GsonUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.junit.Test;

import java.util.Date;
import java.util.List;

/**
 * @author Jerry
 * @datetime 2015-07-18 12:10
 */
public class JsonPathUtilTest extends BaseConfigTest {

    @Test
    public void testReadAsList() {
        String content =
            "{\"attachment\":[{\"name\":\"startTimeReal_Ch_zh\",\"value\":\"2015年7月1日\"},{\"name\":\"endTimeReal_Ch_zh\",\"value\":\"7月17日\"},{\"name\":\"nowReal\",\"value\":\"2015年7月17日\"},{\"name\":\"yearmonthlist\",\"value\":[{\"name\":\"2\",\"value\":\"201502\"},{\"name\":\"3\",\"value\":\"201503\"},{\"name\":\"4\",\"value\":\"201504\"},{\"name\":\"5\",\"value\":\"201505\"},{\"name\":\"6\",\"value\":\"201506\"},{\"name\":\"7\",\"value\":\"201507\"}]},{\"name\":\"selected\",\"value\":\"201507\"},{\"name\":\"startTimeReal\",\"value\":\"20150701000000\"},{\"name\":\"endTimeReal\",\"value\":\"20150717163701\"},{\"name\":\"isChange\",\"value\":\"\"},{\"name\":\"cityCode\",\"value\":\"MM\"}],\"content\":{\"brand\":\"神州行\",\"chargedrecordfeecount\":\"0.00\",\"chargefeecont\":\"0.54\",\"chargerecordfeecount\":\"0.00\",\"evalDataMapStr\":\"dataMap.put(\\\"package\\\",{\\\"data\\\":[[\\\"\\\",\\\"\\\",\\\"\\\",\\\"\\\"]],\\\"total\\\":\\\"0.00\\\"});dataMap.put(\\\"call\\\",{\\\"data\\\":[[\\\"07-07 12:05:38\\\",\\\"茂名\\\",\\\"主叫\\\",\\\"13413343188\\\",\\\"03秒\\\",\\\"本地\\\",\\\"(茂名)神州行低消卡本地套餐(新畅聊卡)\\\",\\\"-\\\",\\\"0.15\\\"],[\\\"07-14 23:32:14\\\",\\\"杭州\\\",\\\"被叫\\\",\\\"18367154602\\\",\\\"11秒\\\",\\\"本地\\\",\\\"(茂名)神州行低消卡本地套餐(新畅聊卡)\\\",\\\"-\\\",\\\"0.39\\\"]],\\\"total\\\":\\\"0.54\\\"});dataMap.put(\\\"msg\\\",{\\\"data\\\":[[\\\"\\\",\\\"\\\",\\\"\\\",\\\"\\\",\\\"\\\",\\\"\\\",\\\"\\\",\\\"\\\"]],\\\"total\\\":\\\"0.00\\\"});dataMap.put(\\\"net\\\",{\\\"data\\\":[[\\\"\\\",\\\"\\\",\\\"\\\",\\\"\\\",\\\"\\\",\\\"\\\",\\\"\\\",\\\"\\\"]],\\\"total\\\":\\\"0.00\\\"});dataMap.put(\\\"sr\\\",{\\\"data\\\":[[\\\"\\\",\\\"\\\",\\\"\\\",\\\"\\\",\\\"\\\"]],\\\"total\\\":\\\"0.00\\\"});dataMap.put(\\\"payfor\\\",{\\\"data\\\":[[\\\"07-15 09:07:34\\\",\\\"\\\",\\\"698033\\\",\\\"18206643779\\\",\\\"\\\",\\\"698033\\\",\\\"\\\",\\\"0.00\\\"]],\\\"total\\\":\\\"0.00\\\"});dataMap.put(\\\"others\\\",{\\\"data\\\":[[\\\"\\\",\\\"\\\",\\\"\\\"]],\\\"total\\\":\\\"0.00\\\"})\",\"imageBJ\":\"78.57\",\"imageBJTime\":\"11.00\",\"imageGprs\":\"0.00\",\"imageGprsCharge\":\"0.00\",\"imageMms\":\"0.00\",\"imageMmsCharge\":\"0.00\",\"imageSms\":\"0.00\",\"imageSmsCharge\":\"0.00\",\"imageVoice\":\"100.00\",\"imageVoiceCharge\":\"0.54\",\"imageZJ\":\"21.43\",\"imageZJTime\":\"3.00\",\"leftTimes\":\"3\",\"netfeecount\":\"0.00\",\"otherfeecont\":\"0.00\",\"realtimeListSearchRspBean\":{\"calldetail\":{\"calldetaillist\":[{\"becall\":\"主叫\",\"chargefee\":\"0.15\",\"contnum\":\"13413343188\",\"conttype\":\"本地\",\"giftfee\":\"-\",\"period\":\"03秒\",\"place\":\"茂名\",\"taocantype\":\"(茂名)神州行低消卡本地套餐(新畅聊卡)\",\"time\":\"07-07 12:05:38\"},{\"becall\":\"被叫\",\"chargefee\":\"0.39\",\"contnum\":\"18367154602\",\"conttype\":\"本地\",\"giftfee\":\"-\",\"period\":\"11秒\",\"place\":\"杭州\",\"taocantype\":\"(茂名)神州行低消卡本地套餐(新畅聊卡)\",\"time\":\"07-14 23:32:14\"}],\"chargefeecont\":\"0.54\"},\"chargedrecord\":{\"chargedrecordlist\":[{\"bustype\":\"698033\",\"chargetype\":\"\",\"comcode\":\"698033\",\"fee\":\"0.00\",\"port\":\"18206643779\",\"servicername\":\"\",\"time\":\"07-15 09:07:34\",\"type\":\"\"}],\"feecount\":\"0.00\"},\"chargerecord\":{\"chargerecordlist\":[{\"fee\":\"\",\"port\":\"\",\"servtype\":\"\",\"taocanname\":\"\",\"time\":\"\"}],\"feecount\":\"0.00\"},\"netdetail\":{\"feecount\":\"0.00\",\"netdetaillist\":[{\"fee\":\"\",\"netcount\":\"\",\"perid\":\"\",\"place\":\"\",\"reducefee\":\"\",\"servicetype\":\"\",\"taocantype\":\"\",\"time\":\"\"}]},\"otherrecord\":{\"chargecount\":\"0.00\",\"otherrecordlist\":[{\"charge\":\"\",\"time\":\"\",\"type\":\"\"}]},\"resultMsg\":\"ok\",\"retCode\":\"0\",\"retMsg\":\"\",\"retType\":\"0\",\"smsdetail\":{\"feecount\":\"0.00\",\"smsdetaillist\":[{\"fee\":\"\",\"place\":\"\",\"send\":\"\",\"servicename\":\"\",\"smsnum\":\"\",\"smstype\":\"\",\"taocantype\":\"\",\"time\":\"\"}]},\"taocan\":{\"feecount\":\"0.00\",\"taocanfeelist\":[{\"cycle\":\"\",\"fee\":\"\",\"feename\":\"\",\"time\":\"\"}]}},\"servernumber\":\"18206643779\",\"smsfeecount\":\"0.00\",\"successful\":false,\"taocanfeecount\":\"0.00\",\"total\":\"0.54\",\"totalTimes\":\"6\",\"username\":\"唐良忠\"},\"type\":\"initPage\"}";
        String jsonpath = "$.attachment[?(@.name=='yearmonthlist')].value[*]";
        List<String> list = JsonPathUtil.readAsList(content, jsonpath);

        for (String text : list) {
            System.out.println(text);
            System.out.println(JsonPathUtil.readAsString(text, "$.value"));
        }

        jsonpath = "$.attachment[?(@.name=='startTimeReal')].value";
        System.out.println(JsonPathUtil.readAsString(content, jsonpath));
    }

    @Test
    public void testJson() {
        String content = getContent("json/json");
        String jsonpath = "$.[*]";
        List<String> list = JsonPathUtil.readAsList(content, jsonpath);
        System.out.println(list.size());
        for (String text : list) {
            System.out.println(text);
        }

        System.out.println(JsonPathUtil.readAsString(content, jsonpath));
    }

    @Test
    public void testGson() {
        String content = getContent("json/json");

        List<TestObj> list = GsonUtils.fromJson(content, new TypeToken<List<TestObj>>() {}.getType());
        System.out.println(list.size());
        for (TestObj testObj : list) {
            System.out.println(GsonUtils.toJson(testObj));
        }
    }

    private class TestObj {

        private String tradeNumber;
        private float amount;
        private String tradeDetailUrl;
        private String otherSide;
        private String tradeStatus;
        private Date tradeDatetime;
        private String title;
        private int tradeType;

        public String getTradeNumber() {
            return tradeNumber;
        }

        public void setTradeNumber(String tradeNumber) {
            this.tradeNumber = tradeNumber;
        }

        public float getAmount() {
            return amount;
        }

        public void setAmount(float amount) {
            this.amount = amount;
        }

        public String getTradeDetailUrl() {
            return tradeDetailUrl;
        }

        public void setTradeDetailUrl(String tradeDetailUrl) {
            this.tradeDetailUrl = tradeDetailUrl;
        }

        public String getOtherSide() {
            return otherSide;
        }

        public void setOtherSide(String otherSide) {
            this.otherSide = otherSide;
        }

        public String getTradeStatus() {
            return tradeStatus;
        }

        public void setTradeStatus(String tradeStatus) {
            this.tradeStatus = tradeStatus;
        }

        public Date getTradeDatetime() {
            return tradeDatetime;
        }

        public void setTradeDatetime(Date tradeDatetime) {
            this.tradeDatetime = tradeDatetime;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getTradeType() {
            return tradeType;
        }

        public void setTradeType(int tradeType) {
            this.tradeType = tradeType;
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this, ToStringStyle.JSON_STYLE).append("tradeNumber", tradeNumber).append("amount", amount).append("tradeDetailUrl", tradeDetailUrl)
                .append("otherSide", otherSide).append("tradeStatus", tradeStatus).append("tradeDatetime", tradeDatetime).append("title", title).append("tradeType", tradeType)
                .toString();
        }
    }

}
