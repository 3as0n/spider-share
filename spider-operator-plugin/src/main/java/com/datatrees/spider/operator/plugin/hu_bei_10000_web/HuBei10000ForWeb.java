/*
 * Copyright © 2015 - 2018 杭州大树网络技术有限公司. All Rights Reserved
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.datatrees.spider.operator.plugin.hu_bei_10000_web;

import java.util.Map;

import com.datatrees.common.util.PatternUtils;
import com.datatrees.spider.share.common.http.TaskHttpClient;
import com.datatrees.spider.share.common.utils.TaskUtils;
import com.datatrees.spider.share.common.utils.CheckUtils;
import com.datatrees.spider.share.domain.RequestType;
import com.datatrees.spider.share.domain.http.Response;
import com.datatrees.spider.operator.plugin.common.LoginUtilsForChina10000Web;
import com.datatrees.spider.operator.domain.OperatorParam;
import com.datatrees.spider.operator.service.plugin.OperatorPlugin;
import com.datatrees.spider.share.domain.ErrorCode;
import com.datatrees.spider.share.domain.FormType;
import com.datatrees.spider.share.domain.http.HttpResult;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 登录 电信web端统一登录
 * 详单 短信验证码
 * User: yand
 * Date: 2017/10/19
 */
public class HuBei10000ForWeb implements OperatorPlugin {

    private static final Logger                     logger     = LoggerFactory.getLogger(HuBei10000ForWeb.class);

    private              LoginUtilsForChina10000Web loginUtils = new LoginUtilsForChina10000Web();

    @Override
    public HttpResult<Map<String, Object>> init(OperatorParam param) {
        return loginUtils.init(param);
    }

    @Override
    public HttpResult<String> refeshPicCode(OperatorParam param) {
        switch (param.getFormType()) {
            case FormType.LOGIN:
                return loginUtils.refeshPicCode(param);
            default:
                return new HttpResult<String>().failure(ErrorCode.NOT_SUPORT_METHOD);
        }
    }

    @Override
    public HttpResult<Map<String, Object>> validatePicCode(OperatorParam param) {
        return new HttpResult<Map<String, Object>>().failure(ErrorCode.NOT_SUPORT_METHOD);
    }

    @Override
    public HttpResult<Map<String, Object>> refeshSmsCode(OperatorParam param) {
        switch (param.getFormType()) {
            case FormType.VALIDATE_BILL_DETAIL:
                return refeshSmsCodeForBillDetail(param);
            default:
                return new HttpResult<Map<String, Object>>().failure(ErrorCode.NOT_SUPORT_METHOD);
        }
    }

    @Override
    public HttpResult<Map<String, Object>> submit(OperatorParam param) {
        switch (param.getFormType()) {
            case FormType.LOGIN:
                return submitForLogin(param);
            case FormType.VALIDATE_BILL_DETAIL:
                return submitForBillDetail(param);
            default:
                return new HttpResult<Map<String, Object>>().failure(ErrorCode.NOT_SUPORT_METHOD);
        }
    }

    @Override
    public HttpResult<Object> defineProcess(OperatorParam param) {
        return new HttpResult<Object>().failure(ErrorCode.NOT_SUPORT_METHOD);
    }

    private HttpResult<Map<String, Object>> submitForLogin(OperatorParam param) {
        CheckUtils.checkNotBlank(param.getPassword(), ErrorCode.EMPTY_PASSWORD);
        HttpResult<Map<String, Object>> result = new HttpResult<>();
        Response response = null;
        try {
            //登录
            result = loginUtils.submit(param);
            if (!result.getStatus()) {
                return result;
            }

            String templateUrl
                    = "http://www.189.cn/login/sso/ecs.do?method=linkTo&platNo=10018&toStUrl=http://hb.189.cn/SSOtoWSSNew?toWssUrl=/pages/selfservice/feesquery/feesyue.jsp&trackPath=SYleftDH";
            String referer = "http://www.189.cn/hb/";
            response = TaskHttpClient.create(param.getTaskId(), param.getWebsiteName(), RequestType.GET).setFullUrl(templateUrl).setReferer(referer)
                    .invoke();
            String pageContent = response.getPageContent();
            if (StringUtils.isBlank(pageContent)) {
                logger.error("hu_bei_10000_web_002请求失败,param={},response={}", param, response);
                return result.failure(ErrorCode.NOT_EMPTY_ERROR_CODE);
            }

            templateUrl = "http://hb.189.cn/hbuserCenter.action";
            referer = "http://hb.189.cn/pages/selfservice/feesquery/newBOSSQueryCustBill.action";
            response = TaskHttpClient.create(param.getTaskId(), param.getWebsiteName(), RequestType.GET).setFullUrl(templateUrl).setReferer(referer)
                    .invoke();
            pageContent = response.getPageContent();
            if (StringUtils.isBlank(pageContent)) {
                logger.error("hu_bei_10000_web_003请求失败,param={},response={}", param, response);
                return result.failure(ErrorCode.NOT_EMPTY_ERROR_CODE);
            }

            String cityname = PatternUtils.group(pageContent, "var cityname=\"([^\"]+)\"", 1);
            TaskUtils.addTaskShare(param.getTaskId(), "cityname", cityname);
            String realname = PatternUtils.group(pageContent, "var username=\"([^\"]+)\"", 1);
            TaskUtils.addTaskShare(param.getTaskId(), "realname", realname);
            String acctype = PatternUtils.group(pageContent, "var\\s*acctype\\s*=\\s*\"([^\"]+)\"", 1);
            TaskUtils.addTaskShare(param.getTaskId(), "acctype", acctype);

            templateUrl = "http://hb.189.cn/ajaxServlet/getCityCodeAndIsLogin";
            String templateData = "method=getCityCodeAndIsLogin";
            referer = "http://hb.189.cn/hbuserCenter.action";
            response = TaskHttpClient.create(param.getTaskId(), param.getWebsiteName(), RequestType.POST).setFullUrl(templateUrl)
                    .setRequestBody(templateData).setReferer(referer).invoke();
            pageContent = response.getPageContent();
            if (StringUtils.isNotBlank(pageContent) && pageContent.contains("LOGIN\":\"true")) {
                logger.warn("登录成功,params={}", param);
                return result.success();
            } else {
                logger.warn("登录失败,param={},response={}", param, response);
                return result.failure(ErrorCode.LOGIN_UNEXPECTED_RESULT);
            }
        } catch (Exception e) {
            logger.error("登陆失败,param={},response={}", param, response, e);
            return result.failure(ErrorCode.LOGIN_ERROR);
        }
    }

    private HttpResult<Map<String, Object>> refeshSmsCodeForBillDetail(OperatorParam param) {
        HttpResult<Map<String, Object>> result = new HttpResult<>();
        Response response = null;
        try {
            String citycode = TaskUtils.getTaskContext(param.getTaskId(), "citycode");
            String templateUrl = "http://hb.189.cn/feesquery_sentPwd.action";
            String templateData = "productNumber=" + param.getMobile() + "&cityCode=" + citycode + "&sentType=C&ip=0";
            String referer = "http://hb.189.cn/pages/selfservice/feesquery/detailListQuery.jsp";
            response = TaskHttpClient.create(param.getTaskId(), param.getWebsiteName(), RequestType.POST).setFullUrl(templateUrl)
                    .setRequestBody(templateData).setReferer(referer).invoke();
            String pageContent = response.getPageContent();
            if (pageContent.contains("随机验证码已经发送")) {
                logger.info("详单-->短信验证码-->刷新成功,param={}", param);
                return result.success();
            } else if (pageContent.contains("随机码操作过于频繁")) {
                logger.error("详单-->短信验证码-->刷新失败-->随机码操作过于频繁,param={},pageContent={}", param, pageContent);
                return result.failure(ErrorCode.REFESH_SMS_UNEXPECTED_RESULT);
            } else {
                logger.error("详单-->短信验证码-->刷新失败,param={},pageContent={}", param, pageContent);
                return result.failure(ErrorCode.REFESH_SMS_UNEXPECTED_RESULT);
            }

        } catch (Exception e) {
            logger.error("详单-->短信验证码-->刷新失败,param={},response={}", param, response, e);
            return result.failure(ErrorCode.REFESH_SMS_ERROR);
        }
    }

    private HttpResult<Map<String, Object>> submitForBillDetail(OperatorParam param) {
        HttpResult<Map<String, Object>> result = new HttpResult<>();
        Response response = null;
        try {
            String templateUrl = "http://hb.189.cn/feesquery_checkCDMAFindWeb.action";
            String templateData = "random=" + param.getSmsCode() + "&sentType=C";
            String referer = "http://hb.189.cn/pages/selfservice/feesquery/detailListQuery.jsp";
            response = TaskHttpClient.create(param.getTaskId(), param.getWebsiteName(), RequestType.POST).setFullUrl(templateUrl)
                    .setRequestBody(templateData).setReferer(referer).invoke();
            String pageContent = response.getPageContent();
            if (pageContent.contains("1")) {
                logger.info("详单-->校验成功,param={}", param);
                return result.success();
            }
            logger.error("详单-->校验失败,param={},pateContent={}", param, pageContent);
            return result.failure(ErrorCode.REFESH_SMS_UNEXPECTED_RESULT);

        } catch (Exception e) {
            logger.error("详单-->校验失败,param={},response={}", param, response, e);
            return result.failure(ErrorCode.VALIDATE_ERROR);
        }
    }
}

