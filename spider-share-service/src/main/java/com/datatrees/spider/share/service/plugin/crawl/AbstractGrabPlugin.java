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

package com.datatrees.spider.share.service.plugin.crawl;

import com.datatrees.common.util.GsonUtils;
import com.datatrees.spider.share.domain.AttributeKey;
import com.datatrees.spider.share.domain.directive.DirectiveEnum;
import com.datatrees.spider.share.domain.directive.DirectiveRedisCode;
import com.datatrees.spider.share.domain.directive.DirectiveResult;
import com.datatrees.spider.share.domain.directive.DirectiveType;
import com.datatrees.spider.share.service.plugin.AbstractRawdataPlugin;
import com.treefinance.crawler.framework.context.AbstractProcessorContext;
import com.treefinance.crawler.framework.extension.plugin.PluginConstants;
import com.treefinance.crawler.framework.extension.plugin.PluginFactory;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * APP抓取任务 业务场景:部分容易被封的url让APP端抓取,成功率会高些 处理流程: 1.程序启动后发送指令,并进入等待结果状态 2.APP端导入数据前端就完成任务 3.如果失败,不让APP端重新处理
 * 4.TODO:如果有需要,失败可以重新发送指令给前端 Created by zhouxinghai on 2017/5/18.
 */
public abstract class AbstractGrabPlugin extends AbstractRawdataPlugin {

    private static final Logger logger = LoggerFactory.getLogger(AbstractGrabPlugin.class);

    public Map<String, String> doProcess(String... args) throws Exception {
        Map<String, String> resultMap = new HashMap<>();
        AbstractProcessorContext context = PluginFactory.getProcessorContext();
        String websiteName = context.getWebsiteName();
        Long taskId = context.getLong(AttributeKey.TASK_ID);
        if (null == taskId || StringUtils.isBlank(websiteName)) {
            logger.error("grab url plugin error, invalid param, taskId={},websiteName={}", taskId, websiteName);
            resultMap.put(AttributeKey.ERROR_CODE, "-1");
            return resultMap;
        }
        logger.info("start run grab plugin!taskId={},websiteName={}", taskId, websiteName);
        Map<String, Object> config = getConfig();
        if (null == config || config.isEmpty()) {
            logger.error("grab url plugin error, config is empty! taskId={},websiteName={}", taskId, websiteName);
            resultMap.put(AttributeKey.ERROR_CODE, "-1");
            return resultMap;
        }
        String remark = GsonUtils.toJson(config);

        // 发送指令
        getMessageService().sendDirective(taskId, DirectiveEnum.GRAB_URL.getCode(), remark);
        // 保存状态到redis
        DirectiveResult<String> sendDirective = new DirectiveResult<>(DirectiveType.GRAB_URL, taskId);
        sendDirective.fill(DirectiveRedisCode.WAIT_APP_DATA, remark);
        getRedisService().saveDirectiveResult(sendDirective);

        // 等待APP处理完成,并通过dubbo将数据写入redis
        String resultKey = sendDirective.getDirectiveKey(DirectiveRedisCode.WAIT_SERVER_PROCESS);

        DirectiveResult<Map<String, String>> receiveDirective = getRedisService().getDirectiveResult(resultKey, 120, TimeUnit.SECONDS);
        if (null == receiveDirective) {
            logger.error("get grab url result timeout,taskId={},websiteName={},resultKey={}", taskId, websiteName, resultKey);
            resultMap.put(AttributeKey.ERROR_CODE, "-1");
            return resultMap;
        }
        String cookeString = receiveDirective.getData().get("cookes");
        logger.info("get cookie success,taskId={},websiteName={},cookes={}", taskId, websiteName, cookeString);
        context.setCookies(cookeString);
        resultMap.put(PluginConstants.FIELD, receiveDirective.getData().get("html"));
        logger.info("get result success,taskId={},websiteName={},resultKey={}", taskId, websiteName, resultKey);
        return resultMap;
    }

    @Override
    public String process(String... args) throws Exception {
        return GsonUtils.toJson(doProcess(args));
    }

    /**
     * 配置文件
     * 
     * @return
     */
    protected abstract Map<String, Object> getConfig();

    /**
     * 检查结果
     * 
     * @param responseMap 前端返回内容
     * @return
     */
    protected abstract boolean checkResult(Map<String, Object> responseMap);

    /**
     *
     * 配置文件详见:https://tower.im/projects/960d325b149a4aaa8cf3ba30a18a8abe/docs/fb24574b83224535a74ea8102e35077f/
     * config示例： { "css": [ { "value": "#header,.web_qr_login .bottom{display:none !important;}", "key":
     * "/cgi-bin/login?" } ], "usePCUA": true, "js": [ { "value": "function getCookie(c_name){if
     * (document.cookie.length>0){c_start=document.cookie.indexOf(c_name + '=');if (c_start!=-1){ c_start=c_start +
     * c_name.length+1;c_end=document.cookie.indexOf(';',c_start);if (c_end==-1){c_end=document.cookie.length;}return
     * unescape(document.cookie.substring(c_start,c_end))} }return ''}function setCookie(c_name, value) {var exp = new
     * Date();exp.setTime(exp.getTime() + 30*24*60*60*1000);document.cookie = c_name + '=' + escape(value) +
     * ';domain=qq.com;expires=' + exp.toGMTString();}function getToken(skey){var hash = 5381,token = null;if (skey) {if
     * (skey !==null) {var i = 0,l = skey.length;for (; i < l; ++i){ hash += (hash << 5) +
     * skey.charAt(i).charCodeAt();}token = hash & 2147483647}} else {token = null;}return
     * token}saveCookie('qzone_token',getToken(getCookie('p_skey') || getCookie('skey') ||
     * ''));saveCookie('qzone_p_skey',getCookie('p_skey'));saveCookie('qzone_skey',getCookie('skey'));location.href='https://ui.ptlogin2.qq.com/cgi-bin/login?style=9&appid=522005705&daid=4&s_url=http%3A%2F%2Fw.mail.qq.com%2Fcgi-bin%2Flogin%3Fvt%3Dpassport%26vm%3Dwsk%26delegate_url%3D%26f%3Dxhtml%26target%3D&hln_css=http%3A%2F%2Fmail.qq.com%2Fzh_CN%2Fhtmledition%2Fimages%2Flogo%2Fqqmail%2Fqqmail_logo_default_200h.png&low_login=1&hln_autologin=%E8%AE%B0%E4%BD%8F%E7%99%BB%E5%BD%95%E7%8A%B6%E6%80%81&pt_no_onekey=1'",
     * "key": "qzone.qq.com/cgi-bin/apptrace" }, { "value":
     * "location.href='https://w.mail.qq.com/cgi-bin/login?vt=passport&vm=wsk&delegate_url=&f=xhtml'", "key":
     * "w.mail.qq.com/cgi-bin/login\?vt=passport&ss=1" } ], "startUrl": [
     * "https://ui.ptlogin2.qq.com/cgi-bin/login?style=9&appid=522005705&daid=4&s_url=http%3A//m.qzone.com/infocenter%3Fg_f%3D275%26g_ut%3D3&hln_css=http%3A%2F%2Fmail.qq.com%2Fzh_CN%2Fhtmledition%2Fimages%2Flogo%2Fqqmail%2Fqqmail_logo_default_200h.png",
     * "http://analy.qzone.qq.com/cgi-bin/apptrace" ], "endUrl": [ "//h5.qzone.qq.com/",
     * "w.mail.qq.com/cgi-bin/today\?sid=" ], "httpConfig": { "cookies": [ { "cookieDomain": "pbsz.ebank.cmbchina.com",
     * "cookiePath": "/", "isSecure": false, "hasPathAttribute": false, "hasDomainAttribute": false, "cookieVersion": 0,
     * "name": "DeviceType", "value": "A" }, { "cookieDomain": ".cmbchina.com", "cookieExpiryDate": "2027-04-23
     * 21:10:49", "cookiePath": "/", "isSecure": false, "hasPathAttribute": false, "hasDomainAttribute": false,
     * "cookieVersion": 0, "name": "WEBTRENDS_ID", "value":
     * "122.224.99.210-1539347328.30588357::D03C7567F5D525C3A1034C66A4E" }, { "cookieDomain": "pbsz.ebank.cmbchina.com",
     * "cookiePath": "/", "isSecure": false, "hasPathAttribute": false, "hasDomainAttribute": false, "cookieVersion": 0,
     * "name": "ProVersion", "value": "" }, { "cookieDomain": "pbsz.ebank.cmbchina.com", "cookieExpiryDate": "2027-04-23
     * 22:37:11", "cookiePath": "/CmbBank_GenShell/UI/GenShellPC/Login/", "isSecure": false, "hasPathAttribute": false,
     * "hasDomainAttribute": false, "cookieVersion": 0, "name": "CMB_GenServer", "value": "BranchNo:" }, {
     * "cookieDomain": "pbsz.ebank.cmbchina.com", "cookiePath": "/", "isSecure": false, "hasPathAttribute": false,
     * "hasDomainAttribute": false, "cookieVersion": 0, "name": "AuthType", "value": "A" }, { "cookieDomain":
     * "pbsz.ebank.cmbchina.com", "cookieExpiryDate": "2027-04-23 22:37:13", "cookiePath": "/", "isSecure": false,
     * "hasPathAttribute": false, "hasDomainAttribute": false, "cookieVersion": 0, "name": "WTFPC", "value":
     * "id\u003d233827d36fda017e51f1493125847964:lv\u003d1493131033500:ss\u003d1493131019507" }, { "cookieDomain":
     * "pbsz.ebank.cmbchina.com", "cookiePath": "/", "isSecure": false, "hasPathAttribute": false, "hasDomainAttribute":
     * false, "cookieVersion": 0, "name": "ClientStamp", "value": "5715131266354546657" } ], "proxy": "", "header": "",
     * "responseData": [ "html", "cookie" ] }, "client": "webview", "visible": true, "visitType": "url" }
     * 
     * name value description usePCUA： true／false 是否需要使用pc的useagent startUrl url数组／页面内容 需要请求的url或者页面 endUrl url数组／页面内容
     * 结束的url或者页面 responseData "html","cookie"的数组 返回数据类型 client webview／http 以webview或httpclient打开 visible true／false
     * webview是否需要显示加载 visitType url／html starturl中是url还是html
     *
     * @return
     */
}
