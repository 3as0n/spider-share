package com.datatrees.rawdatacentral.plugin.operator.he_nan_10086_web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.datatrees.crawler.plugin.util.PluginHttpUtils;
import com.datatrees.rawdatacentral.common.utils.BeanFactoryUtils;
import com.datatrees.rawdatacentral.common.utils.CheckUtils;
import com.datatrees.rawdatacentral.common.utils.JsonpUtil;
import com.datatrees.rawdatacentral.common.utils.TemplateUtils;
import com.datatrees.rawdatacentral.domain.constant.AttributeKey;
import com.datatrees.rawdatacentral.domain.constant.FormType;
import com.datatrees.rawdatacentral.domain.enums.ErrorCode;
import com.datatrees.rawdatacentral.domain.operator.OperatorParam;
import com.datatrees.rawdatacentral.domain.result.HttpResult;
import com.datatrees.rawdatacentral.service.OperatorPluginService;
import com.datatrees.rawdatacentral.share.RedisService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * 河南移动
 * 登陆地址:https://login.10086.cn/html/login/login.html
 * 登陆方式:服务密码登陆
 * 图片验证码:支持
 * 验证图片验证码:支持
 * 短信验证码:支持
 *
 * Created by zhouxinghai on 2017/7/17.
 */
public class HeNan10000Service implements OperatorPluginService {

    private static final Logger logger = LoggerFactory.getLogger(HeNan10000Service.class);

    @Override
    public HttpResult<Map<String, Object>> init(Long taskId, String websiteName, OperatorParam param) {
        //不必预登陆,cookie从刷新验证码中获取
        //预登陆可以先返回图片验证码
        return refeshPicCode(taskId, websiteName, FormType.LOGIN, param);
    }

    @Override
    public HttpResult<Map<String, Object>> refeshPicCode(Long taskId, String websiteName, String type,
                                                         OperatorParam param) {
        switch (type) {
            case FormType.LOGIN:
                return refeshPicCodeForLogin(taskId, websiteName, param);
            case FormType.VALIDATE_CALL_LOGS:
                return refeshPicCodeForCallLogs(taskId, websiteName, param);
            default:
                return new HttpResult<Map<String, Object>>().failure(ErrorCode.NOT_SUPORT_METHOD);
        }
    }

    @Override
    public HttpResult<Map<String, Object>> refeshSmsCode(Long taskId, String websiteName, String type,
                                                         OperatorParam param) {
        switch (type) {
            case FormType.LOGIN:
                return refeshSmsCodeForLogin(taskId, websiteName, param);
            case FormType.VALIDATE_CALL_LOGS:
                return refeshSmsCodeForCallLogs(taskId, websiteName, param);
            default:
                return new HttpResult<Map<String, Object>>().failure(ErrorCode.NOT_SUPORT_METHOD);
        }
    }

    @Override
    public HttpResult<Map<String, Object>> submit(Long taskId, String websiteName, String type, OperatorParam param) {
        switch (type) {
            case FormType.LOGIN:
                return submitForLogin(taskId, websiteName, param);
            default:
                return new HttpResult<Map<String, Object>>().failure(ErrorCode.NOT_SUPORT_METHOD);
        }
    }

    @Override
    public HttpResult<Map<String, Object>> validatePicCode(Long taskId, String websiteName, String type,
                                                           OperatorParam param) {
        switch (type) {
            case FormType.LOGIN:
                return validatePicCodeForLogin(taskId, websiteName, param);
            case FormType.VALIDATE_CALL_LOGS:
                return validatePicCodeForCallLogs(taskId, websiteName, param);
            default:
                return new HttpResult<Map<String, Object>>().failure(ErrorCode.NOT_SUPORT_METHOD);
        }
    }

    private HttpResult<Map<String, Object>> refeshPicCodeForLogin(Long taskId, String websiteName,
                                                                  OperatorParam param) {
        /**
         * 这里不一定有图片验证码,随机出现
         */
        String templateUrl = "https://login.10086.cn/captchazh.htm?type=05&timestamp={}";
        String url = TemplateUtils.format(templateUrl, System.currentTimeMillis());
        return PluginHttpUtils.refeshPicCodePicCode(taskId, websiteName, url, RETURN_FIELD_PIC_CODE, FormType.LOGIN);
    }

    private HttpResult<Map<String, Object>> validatePicCodeForLogin(Long taskId, String websiteName,
                                                                    OperatorParam param) {
        String templateUrl = "https://login.10086.cn/verifyCaptcha?inputCode={}";
        CheckUtils.checkNotBlank(param.getPicCode(), ErrorCode.EMPTY_PIC_CODE);
        String url = TemplateUtils.format(templateUrl, param.getPicCode());
        HttpResult<Map<String, Object>> result = new HttpResult<>();
        String pateContent = null;
        try {
            //结果枚举:正确{"resultCode":"0"},错误{"resultCode":"1"}
            pateContent = PluginHttpUtils.getString(url, taskId);
            JSONObject json = JSON.parseObject(pateContent);
            if (!StringUtils.equals("0", json.getString("resultCode"))) {
                logger.error("图片验证码验证失败,taskId={},websiteName={},url={},formType={},pateContent={}", taskId,
                    websiteName, url, FormType.LOGIN, pateContent);
                return result.failure(ErrorCode.VALIDATE_PIC_CODE_FAIL);
            }
            logger.info("图片验证码验证成功,taskId={},websiteName={},url={},formType={}", taskId, websiteName, url,
                FormType.LOGIN);
            return result.success();
        } catch (Exception e) {
            logger.error("图片验证码验证失败,taskId={},websiteName={},url={},formType={},pateContent={}", taskId, websiteName,
                url, FormType.LOGIN, pateContent, e);
            return result.failure(ErrorCode.VALIDATE_PIC_CODE_FAIL);
        }
    }

    private HttpResult<Map<String, Object>> refeshPicCodeForCallLogs(Long taskId, String websiteName,
                                                                     OperatorParam param) {
        String templateUrl = "http://shop.10086.cn/i/authImg?t={}";
        String url = TemplateUtils.format(templateUrl, System.currentTimeMillis());
        return PluginHttpUtils.refeshPicCodePicCode(taskId, websiteName, url, RETURN_FIELD_PIC_CODE,
            FormType.VALIDATE_CALL_LOGS);
    }

    private HttpResult<Map<String, Object>> validatePicCodeForCallLogs(Long taskId, String websiteName,
                                                                       OperatorParam param) {
        //http://shop.10086.cn/i/v1/res/precheck/13735874566?captchaVal=123145&_=1500623358942
        String templateUrl = "http://shop.10086.cn/i/v1/res/precheck/{}?captchaVal={}&_={}";
        CheckUtils.checkNotBlank(param.getPicCode(), ErrorCode.EMPTY_PIC_CODE);
        RedisService redisService = BeanFactoryUtils.getBean(RedisService.class);
        //登陆成功是暂存
        String mobile = redisService.getTaskShare(taskId, AttributeKey.MOBILE);
        String url = TemplateUtils.format(templateUrl, mobile, param.getPicCode(), System.currentTimeMillis());
        HttpResult<Map<String, Object>> result = new HttpResult<>();
        String pateContent = null;
        try {
            //结果枚举:正确{"data":null,"retCode":"000000","retMsg":"输入正确，校验成功","sOperTime":null},错误{"data":null,"retCode":"999999","retMsg":"输入错误，校验失败","sOperTime":null}
            pateContent = PluginHttpUtils.getString(url, taskId);
            JSONObject json = JSON.parseObject(pateContent);
            if (!StringUtils.equals("000000", json.getString("retCode"))) {
                logger.error("图片验证码验证失败,taskId={},websiteName={},url={},formType={},pateContent={}", taskId,
                    websiteName, url, FormType.VALIDATE_CALL_LOGS, pateContent);
                return result.failure(ErrorCode.VALIDATE_PIC_CODE_FAIL);
            }
            logger.info("图片验证码验证成功,taskId={},websiteName={},url={},formType={}", taskId, websiteName, url,
                FormType.VALIDATE_CALL_LOGS);
            return result.success();
        } catch (Exception e) {
            logger.error("图片验证码验证失败,taskId={},websiteName={},url={},formType={},pateContent={}", taskId, websiteName,
                url, FormType.VALIDATE_CALL_LOGS, pateContent, e);
            return result.failure(ErrorCode.VALIDATE_PIC_CODE_FAIL);
        }
    }

    private HttpResult<Map<String, Object>> refeshSmsCodeForLogin(Long taskId, String websiteName,
                                                                  OperatorParam param) {
        String templateUrl = "https://login.10086.cn/sendRandomCodeAction.action?type=01&channelID=12003&userName={}";
        HttpResult<Map<String, Object>> result = new HttpResult<>();
        String url = TemplateUtils.format(templateUrl, param.getMobile());
        String pageContent = null;
        try {
            pageContent = PluginHttpUtils.postString(url, taskId);
            switch (pageContent) {
                case "0":
                    logger.info("短信发送成功,taskId={},websiteName={},url={},formType={}", taskId, websiteName, url,
                        FormType.LOGIN);
                    return result.success();
                case "1":
                    logger.warn("对不起，短信随机码暂时不能发送，请一分钟以后再试,taskId={},websiteName={},url={},formType={}", taskId,
                        websiteName, url, FormType.LOGIN);
                    return result.failure(ErrorCode.REFESH_SMS_ERROR, "对不起,短信随机码暂时不能发送，请一分钟以后再试");
                case "2":
                    logger.warn("短信下发数已达上限，您可以使用服务密码方式登录,taskId={},websiteName={},url={},formType={}", taskId,
                        websiteName, url, FormType.LOGIN);
                    return result.failure(ErrorCode.REFESH_SMS_ERROR, "短信下发数已达上限");
                case "3":
                    logger.warn("对不起，短信发送次数过于频繁,taskId={},websiteName={},url={},formType={}", taskId, websiteName, url,
                        FormType.LOGIN);
                    return result.failure(ErrorCode.REFESH_SMS_ERROR, "对不起，短信发送次数过于频繁");
                case "4":
                    logger.warn("对不起，渠道编码不能为空,taskId={},websiteName={},url={},formType={}", taskId, websiteName, url,
                        FormType.LOGIN);
                    return result.failure(ErrorCode.REFESH_SMS_ERROR);
                case "5":
                    logger.warn("对不起，渠道编码异常,taskId={},websiteName={},url={},formType={}", taskId, websiteName, url,
                        FormType.LOGIN);
                    return result.failure(ErrorCode.REFESH_SMS_ERROR);
                case "4005":
                    logger.warn("手机号码有误，请重新输入,taskId={},websiteName={},url={},formType={}", taskId, websiteName, url,
                        FormType.LOGIN);
                    return result.failure(ErrorCode.REFESH_SMS_ERROR, "手机号码有误，请重新输入");
                default:
                    logger.error("短信验证码发送失败,taskId={},websiteName={},url={},formType={},result={}", taskId, websiteName,
                        url, FormType.LOGIN, pageContent);
                    return result.failure(ErrorCode.REFESH_SMS_ERROR);
            }
        } catch (Exception e) {
            logger.error("短信验证码发送失败,请重试,taskId={},websiteName={},url={},FormType.LOGIN,pageContent={}", taskId,
                websiteName, url, FormType.LOGIN, pageContent, e);
            return result.failure(ErrorCode.REFESH_SMS_ERROR);
        }
    }

    private HttpResult<Map<String, Object>> refeshSmsCodeForCallLogs(Long taskId, String websiteName,
                                                                     OperatorParam param) {
        //https://shop.10086.cn/i/v1/fee/detbillrandomcodejsonp/18838224796?callback=jQuery183002065868962851658_1500889079942&_=1500889136495
        String templateUrl = "https://shop.10086.cn/i/v1/fee/detbillrandomcodejsonp/{}?callback=jQuery183002065868962851658_{}&_={}";
        RedisService redisService = BeanFactoryUtils.getBean(RedisService.class);
        String mobile = redisService.getTaskShare(taskId, AttributeKey.MOBILE);
        HttpResult<Map<String, Object>> result = new HttpResult<>();
        String url = TemplateUtils.format(templateUrl, param.getMobile(), System.currentTimeMillis(),
            System.currentTimeMillis());
        String pageContent = null;
        try {
            pageContent = PluginHttpUtils.postString(url, taskId);
            String jsonString = JsonpUtil.getJsonString(pageContent);
            JSONObject json = JSON.parseObject(jsonString);
            if (!StringUtils.equals("000000", json.getString("retCode"))) {
                logger.error("通话详单-->短信验证码,发送失败,taskId={},websiteName={},url={},formType={},pateContent={}", taskId,
                    websiteName, url, FormType.VALIDATE_CALL_LOGS, pageContent);
                return result.failure(ErrorCode.VALIDATE_PIC_CODE_FAIL);
            }
            logger.info("通话详单-->短信验证码,发送成功,taskId={},websiteName={},url={},formType={}", taskId, websiteName, url,
                FormType.VALIDATE_CALL_LOGS);
            return result.success();
        } catch (Exception e) {
            logger.error("通话详单-->短信验证码,发送失败,taskId={},websiteName={},url={},FormType.LOGIN,pageContent={}", taskId,
                websiteName, url, FormType.LOGIN, pageContent, e);
            return result.failure(ErrorCode.REFESH_SMS_ERROR);
        }
    }

    private HttpResult<Map<String, Object>> submitForLogin(Long taskId, String websiteName, OperatorParam param) {
        String templateUrl = "https://login.10086.cn/login.htm?accountType=01&account={}&password={}&pwdType=01&smsPwd={}&inputCode={}&backUrl=http://shop.10086.cn/i/&rememberMe=0&channelID=12003&protocol=https:&timestamp={}";
        CheckUtils.checkNotBlank(param.getPassword(), ErrorCode.EMPTY_PASSWORD);
        CheckUtils.checkNotBlank(param.getPicCode(), ErrorCode.EMPTY_PIC_CODE);
        CheckUtils.checkNotBlank(param.getSmsCode(), ErrorCode.EMPTY_SMS_CODE);
        HttpResult<Map<String, Object>> result = validatePicCodeForLogin(taskId, websiteName, param);
        if (!result.getStatus()) {
            return result;
        }
        String url = TemplateUtils.format(templateUrl, param.getMobile(), param.getPassword(), param.getSmsCode(),
            param.getPicCode(), System.currentTimeMillis());
        String pageContent = null;
        try {
            /**
             * 结果枚举:
             * 登陆成功:{"artifact":"3490872f8d114992b44dc4e60f595fa0","assertAcceptURL":"http://shop.10086.cn/i/v1/auth/getArtifact"
             ,"code":"0000","desc":"认证成功","islocal":false,"provinceCode":"371","result":"0","uid":"b73f1d1210d94fadaf4ba9ce8c49aef1"
             }
             短信验证码过期:{"code":"6001","desc":"短信随机码不正确或已过期，请重新获取","islocal":false,"result":"8"}
             短信验证码不正确:{"code":"6002","desc":"短信随机码不正确或已过期，请重新获取","islocal":false,"result":"8"}
             {"assertAcceptURL":"http://shop.10086.cn/i/v1/auth/getArtifact","code":"2036","desc":"您的账户名与密码不匹配，请重
             新输入","islocal":false,"result":"2"}
             重复登陆:{"islocal":false,"result":"9"}
             */
            //没有设置referer会出现connect reset
            String referer = "https://login.10086.cn/html/login/login.html";
            pageContent = PluginHttpUtils.getString(url, referer, taskId);
            JSONObject json = JSON.parseObject(pageContent);
            String code = json.getString("code");
            String errorMsg = json.getString("desc");
            //重复登陆:{"islocal":false,"result":"9"}
            if (StringUtils.equals("9", json.getString("result")) || StringUtils.equals("0000", code)) {
                logger.info("登陆成功,taskId={},websiteName={},url={},formType={}", taskId, websiteName, url,
                    FormType.LOGIN);
                RedisService redisService = BeanFactoryUtils.getBean(RedisService.class);
                //保存手机号和服务密码,通话详单要用
                redisService.addTaskShare(taskId, AttributeKey.MOBILE, param.getMobile().toString());
                redisService.addTaskShare(taskId, AttributeKey.PASSWORD, param.getPassword());
                return result.success();
            }
            switch (code) {
                case "2036":
                    logger.warn("账户名与密码不匹配,taskId={},websiteName={},url={},formType={}", taskId, websiteName, url,
                        FormType.LOGIN);
                    return result.failure(ErrorCode.VALIDATE_PASSWORD_FAIL);
                case "6001":
                    logger.warn("短信随机码不正确或已过期,taskId={},websiteName={},url={},formType={}", taskId, websiteName, url,
                        FormType.LOGIN);
                    return result.failure(ErrorCode.VALIDATE_SMS_FAIL);
                case "6002":
                    logger.warn("短信随机码不正确或已过期,taskId={},websiteName={},url={},formType={}", taskId, websiteName, url,
                        FormType.LOGIN);
                    return result.failure(ErrorCode.VALIDATE_SMS_FAIL);
                default:
                    logger.error("登陆失败,taskId={},websiteName={},url={},formType={},pageContent={}", taskId, websiteName,
                        url, FormType.LOGIN, pageContent);
                    if (StringUtils.contains(errorMsg, "密码")) {
                        return result.failure(ErrorCode.VALIDATE_PASSWORD_FAIL);
                    }
                    if (StringUtils.contains(errorMsg, "短信")) {
                        return result.failure(ErrorCode.VALIDATE_SMS_FAIL);
                    }
                    return result.failure(ErrorCode.LOGIN_FAIL);
            }
        } catch (Exception e) {
            logger.error("登陆失败,taskId={},websiteName={},url={},formType={},pageContent={}", taskId, websiteName, url,
                FormType.LOGIN, pageContent, e);
            return result.failure(ErrorCode.LOGIN_FAIL);
        }
    }

}
