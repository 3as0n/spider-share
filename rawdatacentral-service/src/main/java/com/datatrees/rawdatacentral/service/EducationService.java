package com.datatrees.rawdatacentral.service;

import com.datatrees.rawdatacentral.domain.education.EducationParam;
import com.datatrees.rawdatacentral.domain.result.HttpResult;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by zhangyanjia on 2017/11/30.
 */
public interface EducationService {

    /**
     * 登录初始化
     *
     * @return
     */
    HttpResult<Map<String, Object>> loginInit(EducationParam param);

    /**
     * 登录提交
     *
     * @return
     */
    HttpResult<Map<String, Object>> loginSubmit(EducationParam param);

    /**
     * 注册初始化
     *
     * @return
     */
    HttpResult<Map<String, Object>> registerInit(EducationParam param);

    /**
     * 注册刷新图片验证码
     *
     * @return
     */
    HttpResult<Map<String, Object>> registerRefeshPicCode(EducationParam param);

    /**
     * 注册验证图片验证码,成功则直接发送短信验证码
     *
     * @return
     */
    HttpResult<Map<String, Object>> registerValidatePicCodeAndSendSmsCode(EducationParam param);


    /**
     * 注册提交
     *
     * @return
     */
    HttpResult<Map<String, Object>> registerSubmit(EducationParam param);
}
