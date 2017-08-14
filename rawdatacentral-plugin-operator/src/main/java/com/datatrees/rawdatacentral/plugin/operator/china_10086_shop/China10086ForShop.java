package com.datatrees.rawdatacentral.plugin.operator.china_10086_shop;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.datatrees.common.conf.PropertiesConfiguration;
import com.datatrees.rawdatacentral.common.utils.*;
import com.datatrees.rawdatacentral.domain.constant.AttributeKey;
import com.datatrees.rawdatacentral.domain.constant.FormType;
import com.datatrees.rawdatacentral.domain.constant.PerpertyKey;
import com.datatrees.rawdatacentral.domain.enums.ErrorCode;
import com.datatrees.rawdatacentral.domain.enums.RequestType;
import com.datatrees.rawdatacentral.domain.operator.OperatorParam;
import com.datatrees.rawdatacentral.domain.result.HttpResult;
import com.datatrees.rawdatacentral.domain.vo.Response;
import com.datatrees.rawdatacentral.service.OperatorPluginService;
import com.datatrees.rawdatacentral.api.RedisService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Base64;
import java.util.Map;

/**
 * 中国移动--全国通用
 * 登陆地址:https://login.10086.cn/html/login/login.html
 * 登陆方式:服务密码登陆
 * 图片验证码:支持
 * 验证图片验证码:支持
 * 短信验证码:支持
 *
 * Created by zhouxinghai on 2017/7/17.
 */
public class China10086ForShop implements OperatorPluginService {

    private static final Logger logger = LoggerFactory.getLogger(China10086ForShop.class);

    @Override
    public HttpResult<Map<String, Object>> init(OperatorParam param) {
        HttpResult<Map<String, Object>> result = new HttpResult<>();
        try {
            //登陆页没有获取任何cookie,不用登陆
            return result.success();
        } catch (Exception e) {
            logger.error("登录-->初始化失败,param={}", param, e);
            return result.failure(ErrorCode.TASK_INIT_ERROR);
        }
    }

    @Override
    public HttpResult<String> refeshPicCode(OperatorParam param) {
        switch (param.getFormType()) {
            case FormType.LOGIN:
                return refeshPicCodeForLogin(param);
            case FormType.VALIDATE_BILL_DETAIL:
                return refeshPicCodeForBillDetail(param);
            default:
                return new HttpResult<String>().failure(ErrorCode.NOT_SUPORT_METHOD);
        }
    }

    @Override
    public HttpResult<Map<String, Object>> refeshSmsCode(OperatorParam param) {
        switch (param.getFormType()) {
            case FormType.LOGIN:
                return refeshSmsCodeForLogin(param);
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
    public HttpResult<Map<String, Object>> validatePicCode(OperatorParam param) {
        switch (param.getFormType()) {
            case FormType.LOGIN:
                return validatePicCodeForLogin(param);
            case FormType.VALIDATE_BILL_DETAIL:
                return validatePicCodeForBillDetail(param);
            default:
                return new HttpResult<Map<String, Object>>().failure(ErrorCode.NOT_SUPORT_METHOD);
        }
    }

    private HttpResult<String> refeshPicCodeForLogin(OperatorParam param) {
        /**
         * 这里不一定有图片验证码,随机出现
         */
        HttpResult<String> result = new HttpResult<>();
        Response response = null;
        try {
            String templateUrl = "https://login.10086.cn/captchazh.htm?type=05&timestamp={}";
            response = TaskHttpClient
                .create(param.getTaskId(), param.getWebsiteName(), RequestType.GET, "china_10086_shop_001")
                .setFullUrl(templateUrl, System.currentTimeMillis()).invoke();
            logger.info("登录-->图片验证码-->刷新成功,param={}", param);
            return result.success(response.getPateContentForBase64());
        } catch (Exception e) {
            logger.error("登录-->图片验证码-->刷新失败,param={},response={}", param, response, e);
            return result.failure(ErrorCode.REFESH_PIC_CODE_ERROR);
        }
    }

    private HttpResult<Map<String, Object>> validatePicCodeForLogin(OperatorParam param) {
        CheckUtils.checkNotBlank(param.getPicCode(), ErrorCode.EMPTY_PIC_CODE);
        HttpResult<Map<String, Object>> result = new HttpResult<>();
        Response response = null;
        try {
            String templateUrl = "https://login.10086.cn/verifyCaptcha?inputCode={}";
            response = TaskHttpClient
                .create(param.getTaskId(), param.getWebsiteName(), RequestType.GET, "china_10086_shop_002")
                .setFullUrl(templateUrl, param.getPicCode()).invoke();
            //结果枚举:正确{"resultCode":"0"},错误{"resultCode":"1"}
            JSONObject json = response.getPateContentForJSON();
            if (!StringUtils.equals("0", json.getString("resultCode"))) {
                logger.error("登录-->图片验证码-->校验失败,param={},pageContent={}", param, response.getPateContent());
                return result.failure(ErrorCode.VALIDATE_PIC_CODE_FAIL);
            }
            logger.info("登录-->图片验证码-->校验成功,param={}", param);
            return result.success();
        } catch (Exception e) {
            logger.error("登录-->图片验证码-->校验失败,param={},response={}", param, response, e);
            return result.failure(ErrorCode.VALIDATE_PIC_CODE_FAIL);
        }
    }

    private HttpResult<String> refeshPicCodeForBillDetail(OperatorParam param) {
        HttpResult<String> result = new HttpResult<>();
        Response response = null;
        try {
            String templateUrl = "http://shop.10086.cn/i/authImg?t={}";
            response = TaskHttpClient
                .create(param.getTaskId(), param.getWebsiteName(), RequestType.GET, "china_10086_shop_006")
                .setFullUrl(templateUrl, System.currentTimeMillis()).invoke();
            logger.info("详单-->图片验证码-->刷新成功,param={}", param);
            return result.success(response.getPateContentForBase64());
        } catch (Exception e) {
            logger.error("详单-->图片验证码-->刷新失败,param={},response={}", param, response, e);
            return result.failure(ErrorCode.REFESH_PIC_CODE_ERROR);
        }
    }

    private HttpResult<Map<String, Object>> validatePicCodeForBillDetail(OperatorParam param) {
        //http://shop.10086.cn/i/v1/res/precheck/13735874566?captchaVal=123145&_=1500623358942
        CheckUtils.checkNotBlank(param.getPicCode(), ErrorCode.EMPTY_PIC_CODE);
        HttpResult<Map<String, Object>> result = new HttpResult<>();
        Response response = null;
        try {
            String templateUrl = "http://shop.10086.cn/i/v1/res/precheck/{}?captchaVal={}&_={}";
            //结果枚举:正确{"data":null,"retCode":"000000","retMsg":"输入正确，校验成功","sOperTime":null},
            //错误{"data":null,"retCode":"999999","retMsg":"输入错误，校验失败","sOperTime":null}
            response = TaskHttpClient.create(param, RequestType.GET, "china_10086_shop_007")
                .setFullUrl(templateUrl, param.getMobile(), param.getPicCode(), System.currentTimeMillis()).invoke();
            if (!StringUtils.equals("000000", response.getPateContentForJSON().getString("retCode"))) {
                logger.error("详单-->图片验证码-->校验失败,param={},pateContent={}", param, response.getPateContent());
                return result.failure(ErrorCode.VALIDATE_PIC_CODE_FAIL);
            }
            logger.info("详单-->图片验证码-->校验成功,param={}", param);
            return result.success();
        } catch (Exception e) {
            logger.error("详单-->图片验证码-->校验失败,param={},response={}", param, response, e);
            return result.failure(ErrorCode.VALIDATE_PIC_CODE_FAIL);
        }
    }

    private HttpResult<Map<String, Object>> refeshSmsCodeForLogin(OperatorParam param) {
        HttpResult<Map<String, Object>> result = new HttpResult<>();
        Response response = null;
        try {
            String templateUrl = "https://login.10086.cn/sendRandomCodeAction.action?type=01&channelID=12003&userName={}";
            response = TaskHttpClient.create(param, RequestType.POST, "china_10086_shop_003")
                .setFullUrl(templateUrl, param.getMobile()).invoke();
            switch (response.getPateContent()) {
                case "0":
                    logger.info("登录-->短信验证码-->刷新成功,param={}", param);
                    return result.success();
                case "1":
                    logger.warn("登录-->短信验证码-->刷新失败,对不起，短信随机码暂时不能发送，请一分钟以后再试,param={}", param);
                    return result.failure(ErrorCode.REFESH_SMS_ERROR, "对不起,短信随机码暂时不能发送，请一分钟以后再试");
                case "2":
                    logger.warn("登录-->短信验证码-->刷新失败,短信下发数已达上限，您可以使用服务密码方式登录,param={}", param);
                    return result.failure(ErrorCode.REFESH_SMS_ERROR, "短信下发数已达上限");
                case "3":
                    logger.warn("登录-->短信验证码-->刷新失败,对不起，短信发送次数过于频繁,param={}", param);
                    return result.failure(ErrorCode.REFESH_SMS_ERROR, "对不起，短信发送次数过于频繁");
                case "4":
                    logger.warn("登录-->短信验证码-->刷新失败,对不起，渠道编码不能为空,param={}", param);
                    return result.failure(ErrorCode.REFESH_SMS_ERROR);
                case "5":
                    logger.warn("登录-->短信验证码-->刷新失败,对不起，渠道编码异常,param={}", param);
                    return result.failure(ErrorCode.REFESH_SMS_ERROR);
                case "4005":
                    logger.warn("登录-->短信验证码-->刷新失败,手机号码有误，请重新输入,param={}", param);
                    return result.failure(ErrorCode.REFESH_SMS_ERROR, "手机号码有误，请重新输入");
                default:
                    logger.error("登录-->短信验证码-->刷新失败,param={},pageContent={}", param, response.getPateContent());
                    return result.failure(ErrorCode.REFESH_SMS_ERROR);
            }
        } catch (Exception e) {
            logger.error("登录-->短信验证码-->刷新失败,param={},response={}", param, response, e);
            return result.failure(ErrorCode.REFESH_SMS_ERROR);
        }
    }

    private HttpResult<Map<String, Object>> refeshSmsCodeForBillDetail(OperatorParam param) {
        //https://shop.10086.cn/i/v1/fee/detbillrandomcodejsonp/18838224796?callback=jQuery183002065868962851658_1500889079942&_=1500889136495
        HttpResult<Map<String, Object>> result = new HttpResult<>();
        Response response = null;
        try {
            String templateUrl = "https://shop.10086.cn/i/v1/fee/detbillrandomcodejsonp/{}?callback=jQuery183002065868962851658_1500889079942_={}";
            response = TaskHttpClient.create(param, RequestType.POST, "china_10086_shop_008")
                .setFullUrl(templateUrl, param.getMobile(), System.currentTimeMillis()).invoke();
            //String referer = TemplateUtils.format("http://shop.10086.cn/i/?welcome={}", mobile);
            String jsonString = JsonpUtil.getJsonString(response.getPateContent());
            JSONObject json = JSON.parseObject(jsonString);
            if (!StringUtils.equals("000000", json.getString("retCode"))) {
                logger.error("详单-->短信验证码-->刷新失败,param={},pateContent={}", param, response.getPateContent());
                return result.failure(ErrorCode.REFESH_SMS_ERROR);
            }
            logger.info("详单-->短信验证码-->刷新成功,param={}", param);
            return result.success();
        } catch (Exception e) {
            logger.error("详单-->短信验证码-->刷新失败,param={},response={}", param, response, e);
            return result.failure(ErrorCode.REFESH_SMS_ERROR);
        }
    }

    private HttpResult<Map<String, Object>> submitForBillDetail(OperatorParam param) {
        //https://shop.10086.cn/i/v1/fee/detailbilltempidentjsonp/13844034615?callback=jQuery183042723042018780055_1500975082967&pwdTempSerCode=NzE2MjUz&pwdTempRandCode=NDI4MTUz&captchaVal=a3xeva&_=1500975147178";
        String templateUrl = "https://shop.10086.cn/i/v1/fee/detailbilltempidentjsonp/{}?pwdTempSerCode={}&pwdTempRandCode={}&captchaVal={}&_={}";
        String pwdTempSerCode = Base64.getEncoder().encodeToString(param.getPassword().getBytes());
        String pwdTempRandCode = Base64.getEncoder().encodeToString(param.getSmsCode().getBytes());
        String loginName = CookieUtils.getCookieValue(param.getTaskId(), "loginName");

        HttpResult<Map<String, Object>> result = validatePicCodeForBillDetail(param);
        if (!result.getStatus()) {
            return result;
        }
        Response response = null;
        try {
            /**
             * 结果枚举:
             //jQuery183042723042018780055_1500975082967({"data":null,"retCode":"000000","retMsg":"认证成功!","sOperTime":null})
             */
            //没有设置referer会出现connect reset
            String referer = "https://login.10086.cn/html/login/login.html";
            response = TaskHttpClient
                .create(param, RequestType.GET, "china_10086_shop_009").setReferer(referer).setFullUrl(templateUrl,
                    loginName, pwdTempSerCode, pwdTempRandCode, param.getPicCode(), System.currentTimeMillis())
                .invoke();
            String jsonString = JsonpUtil.getJsonString(response.getPateContent());
            JSONObject json = JSON.parseObject(jsonString);
            String code = json.getString("retCode");
            if (StringUtils.equals("000000", code)) {
                logger.info("详单-->校验成功,param={}", param);
                return result.success();
            }
            switch (code) {
                case "570005":
                    logger.warn("详单-->短信验证码错误,param={}", param);
                    return result.failure(ErrorCode.VALIDATE_SMS_FAIL);
                default:
                    logger.error("详单-->校验失败,param={},pageContent={}", param, response.getPateContent());
                    return result.failure(ErrorCode.VALIDATE_FAIL);
            }
        } catch (Exception e) {
            logger.error("详单-->校验失败,param={},response={}", param, response, e);
            return result.failure(ErrorCode.VALIDATE_FAIL);
        }
    }

    private HttpResult<Map<String, Object>> submitForLogin(OperatorParam param) {
        CheckUtils.checkNotBlank(param.getPassword(), ErrorCode.EMPTY_PASSWORD);
        CheckUtils.checkNotBlank(param.getPicCode(), ErrorCode.EMPTY_PIC_CODE);
        CheckUtils.checkNotBlank(param.getSmsCode(), ErrorCode.EMPTY_SMS_CODE);
        HttpResult<Map<String, Object>> result = validatePicCodeForLogin(param);
        if (!result.getStatus()) {
            return result;
        }
        Response response = null;
        try {
            String templateUrl = "https://login.10086.cn/login.htm?accountType=01&account={}&password={}&pwdType=01&smsPwd={}&inputCode={}&backUrl=http://shop.10086.cn/i/&rememberMe=0&channelID=12003&protocol=https:&timestamp={}";
            String referer = "https://login.10086.cn/html/login/login.html";
            response = TaskHttpClient.create(param, RequestType.GET, "china_10086_shop_004").setReferer(referer)
                .setFullUrl(templateUrl, param.getMobile(), param.getPassword(), param.getSmsCode(), param.getPicCode(),
                    System.currentTimeMillis())
                .invoke();
            /**
             * 结果枚举:
             * 登陆成功:{"artifact":"3490872f8d114992b44dc4e60f595fa0","assertAcceptURL":"http://shop.10086.cn/i/v1/auth/getArtifact"
             ,"code":"0000","desc":"认证成功","islocal":false,"provinceCode":"371","result":"0","uid":"b73f1d1210d94fadaf4ba9ce8c49aef1"
             }这里的provinceCode可能会没有
             短信验证码过期:{"code":"6001","desc":"短信随机码不正确或已过期，请重新获取","islocal":false,"result":"8"}
             短信验证码不正确:{"code":"6002","desc":"短信随机码不正确或已过期，请重新获取","islocal":false,"result":"8"}
             {"assertAcceptURL":"http://shop.10086.cn/i/v1/auth/getArtifact","code":"2036","desc":"您的账户名与密码不匹配，请重
             新输入","islocal":false,"result":"2"}
             重复登陆:{"islocal":false,"result":"9"}
             */
            //没有设置referer会出现connect reset
            JSONObject json = response.getPateContentForJSON();
            //重复登陆:{"islocal":false,"result":"9"}
            if (StringUtils.equals("9", json.getString("result"))) {
                logger.info("重复登陆,param={}", param);
                return result.success();
            }
            String code = json.getString("code");
            String errorMsg = json.getString("desc");
            if (StringUtils.equals("0000", code)) {
                logger.info("登陆成功,param={}", param);
                RedisService redisService = BeanFactoryUtils.getBean(RedisService.class);
                //保存手机号和服务密码,详单要用
                redisService.addTaskShare(param.getTaskId(), AttributeKey.MOBILE, param.getMobile().toString());
                redisService.addTaskShare(param.getTaskId(), AttributeKey.PASSWORD, param.getPassword());

                //获取权限信息,必须访问下主页,否则详单有些cookie没用
                String artifact = json.getString("artifact");
                TaskHttpClient.create(param, RequestType.GET, "china_10086_shop_005")
                    .setFullUrl(
                        "http://shop.10086.cn/i/v1/auth/getArtifact?backUrl=http://shop.10086.cn/i/?f=home&artifact={}",
                        artifact)
                    .invoke();
                String provinceCode = CookieUtils.getCookieValue(param.getTaskId(), "ssologinprovince");
                String provinceName = getProvinceName(provinceCode);
                redisService.addTaskShare(param.getTaskId(), AttributeKey.PROVINCE_NAME, provinceName);

                return result.success();
            }
            switch (code) {
                case "2036":
                    logger.warn("登录失败-->账户名与密码不匹配,param={}", param);
                    return result.failure(ErrorCode.VALIDATE_PASSWORD_FAIL);
                case "6001":
                    logger.warn("登录失败-->短信随机码不正确或已过期,param={}", param);
                    return result.failure(ErrorCode.VALIDATE_SMS_FAIL);
                case "6002":
                    logger.warn("登录失败-->短信随机码不正确或已过期,param={}", param);
                    return result.failure(ErrorCode.VALIDATE_SMS_FAIL);
                default:
                    logger.error("登陆失败,param={},pageContent={}", param, response.getPateContent());
                    if (StringUtils.contains(errorMsg, "密码")) {
                        return result.failure(ErrorCode.VALIDATE_PASSWORD_FAIL);
                    }
                    if (StringUtils.contains(errorMsg, "短信")) {
                        return result.failure(ErrorCode.VALIDATE_SMS_FAIL);
                    }
                    return result.failure(ErrorCode.LOGIN_FAIL);
            }
        } catch (Exception e) {
            logger.error("登陆失败,param={},response={}", param, response, e);
            return result.failure(ErrorCode.LOGIN_FAIL);
        }
    }

    private String getProvinceName(String provinceCode) {
        CheckUtils.checkNotBlank(provinceCode, "provinceCode is blank");
        String json = PropertiesConfiguration.getInstance().get(PerpertyKey.OPERATOR_10086_SHOP_PROVINCE_CODE);
        CheckUtils.checkNotBlank(json, "propery operator.10086.shop.province.code not found");
        Map<String, String> map = JSON.parseObject(json, new TypeReference<Map<String, String>>() {
        });
        return map.get(provinceCode);
    }

}
