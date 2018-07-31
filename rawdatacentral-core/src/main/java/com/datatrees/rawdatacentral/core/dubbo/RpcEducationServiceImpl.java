package com.datatrees.rawdatacentral.core.dubbo;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

import com.datatrees.rawdatacentral.api.CommonPluginApi;
import com.datatrees.rawdatacentral.api.RpcEducationService;
import com.datatrees.spider.share.common.utils.TaskUtils;
import com.datatrees.spider.share.domain.*;
import com.datatrees.spider.share.domain.http.HttpResult;
import com.datatrees.spider.share.service.MessageService;
import com.datatrees.spider.share.service.MonitorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Created by zhangyanjia on 2017/12/1.
 */
@Service
public class RpcEducationServiceImpl implements RpcEducationService {

    private static final Logger          logger               = LoggerFactory.getLogger(RpcEducationServiceImpl.class);

    /**
     * 默认格式格式化成JSON后发送的字符编码
     */
    private static final String          DEFAULT_CHARSET_NAME = "UTF-8";

    @Resource
    private              CommonPluginApi commonPluginApi;

    @Resource
    private              MonitorService  monitorService;

    @Resource
    private              MessageService  messageService;

    @Override
    public HttpResult<Object> loginInit(CommonPluginParam param) {
        if (param.getTaskId() == null || param.getWebsiteName() == null) {
            throw new RuntimeException(ErrorCode.PARAM_ERROR.getErrorMsg());
        }
        Long taskId = param.getTaskId();
        String websiteName = param.getWebsiteName();
        param.setFormType(FormType.LOGIN);
        //提供username字段，防止初始化出错
        param.setUsername("未知");
        HttpResult<Object> result = commonPluginApi.init(param);
        if (!result.getStatus()) {
            monitorService.sendTaskLog(taskId, websiteName, "学信网登录-->初始化-->失败");
            logger.error("学信网登录-->初始化-->失败,result={}", result);
            return result;
        }
        monitorService.sendTaskLog(taskId, websiteName, "学信网登录-->初始化-->成功");
        logger.info("学信网登录-->初始化-->成功,result={}", result);
        return result;
    }

    @Override
    public HttpResult<Object> loginSubmit(CommonPluginParam param) {
        if (param.getTaskId() == null || param.getWebsiteName() == null || param.getUsername() == null || param.getPassword() == null) {
            throw new RuntimeException(ErrorCode.PARAM_ERROR.getErrorMsg());
        }
        Long taskId = param.getTaskId();
        String websiteName = param.getWebsiteName();
        param.setFormType(FormType.LOGIN);
        HttpResult<Object> result = commonPluginApi.submit(param);
        HashMap<String, Object> loginStatus = (HashMap<String, Object>) result.getData();
        if (!result.getStatus() || !(result.getData() != null && "login_success".equals((loginStatus.get("directive"))))) {
            monitorService.sendTaskLog(taskId, websiteName, "学信网登陆-->校验-->失败");
            logger.error("学信网登陆-->校验-->失败,result={}", result);
            return result;
        }

        Map<String, Object> map = new HashMap<>();
        map.put(AttributeKey.TASK_ID, taskId);
        map.put(AttributeKey.WEBSITE_NAME, websiteName);
        String cookies = TaskUtils.getCookieString(taskId);
        map.put(AttributeKey.COOKIE, cookies);
        messageService.sendMessage(TopicEnum.RAWDATA_INPUT.getCode(), TopicTag.LOGIN_INFO.getTag(), map, DEFAULT_CHARSET_NAME);
        logger.info("学信网，启动爬虫成功,result={}", result);
        return result;
    }

    @Override
    public HttpResult<Object> registerInit(CommonPluginParam param) {
        if (param.getTaskId() == null || param.getWebsiteName() == null) {
            throw new RuntimeException(ErrorCode.PARAM_ERROR.getErrorMsg());
        }
        HttpResult<Object> result = commonPluginApi.registerInit(param);
        if (!result.getStatus()) {
            monitorService.sendTaskLog(param.getTaskId(), param.getWebsiteName(), "学信网注册-->初始化-->失败");
            logger.error("学信网注册-->初始化-->失败,result={}", result);
            return result;
        }
        monitorService.sendTaskLog(param.getTaskId(), param.getWebsiteName(), "学信网注册-->初始化-->成功");
        logger.info("学信网注册-->初始化-->成功,result={}", result);
        return result;
    }

    @Override
    public HttpResult<Object> registerRefeshPicCode(CommonPluginParam param) {
        if (param.getTaskId() == null || param.getWebsiteName() == null || param.getMobile() == null) {
            throw new RuntimeException(ErrorCode.PARAM_ERROR.getErrorMsg());
        }

        HttpResult<Object> result = commonPluginApi.registerRefreshPicCode(param);
        if (!result.getStatus()) {
            monitorService.sendTaskLog(param.getTaskId(), param.getWebsiteName(), "学信网注册-->刷新图片验证码-->失败");
            logger.error("学信网注册-->刷新图片验证码-->失败,result={}", result);
            return result;
        }
        monitorService.sendTaskLog(param.getTaskId(), param.getWebsiteName(), "学信网注册-->刷新图片验证码-->成功");
        logger.info("学信网注册-->刷新图片验证码-->成功,param={}", param);
        return result;
    }

    @Override
    public HttpResult<Object> registerValidatePicCodeAndSendSmsCode(CommonPluginParam param) {
        if (param.getTaskId() == null || param.getWebsiteName() == null || param.getPicCode() == null || param.getMobile() == null) {
            throw new RuntimeException(ErrorCode.PARAM_ERROR.getErrorMsg());
        }
        HttpResult<Object> result = commonPluginApi.registerValidatePicCodeAndSendSmsCode(param);
        if (!result.getStatus()) {
            monitorService.sendTaskLog(param.getTaskId(), param.getWebsiteName(), "学信网注册-->校验图片验证码-->失败");
            logger.error("学信网注册-->校验图片验证码-->失败");
            return result;
        }
        monitorService.sendTaskLog(param.getTaskId(), param.getWebsiteName(), "学信网注册-->校验图片验证码-->成功");
        logger.info("学信网注册-->校验图片验证码-->成功");
        return result;
    }

    @Override
    public HttpResult<Object> registerSubmit(CommonPluginParam param) {
        if (param.getTaskId() == null || param.getWebsiteName() == null || param.getMobile() == null || param.getSmsCode() == null ||
                param.getPassword() == null || param.getPassword() == null || param.getRealName() == null || param.getIdCard() == null ||
                param.getIdCardType() == null) {
            throw new RuntimeException(ErrorCode.PARAM_ERROR.getErrorMsg());
        }
        HttpResult<Object> result = commonPluginApi.registerSubmit(param);
        if (!result.getStatus()) {
            monitorService.sendTaskLog(param.getTaskId(), param.getWebsiteName(), "学信网注册-->校验信息-->失败");
            logger.error("学信网注册-->校验信息-->失败");
            return result;
        }
        monitorService.sendTaskLog(param.getTaskId(), param.getWebsiteName(), "学信网注册-->校验信息-->注册成功");
        logger.info("学信网注册-->校验信息-->注册成功");
        return result;
    }

}
