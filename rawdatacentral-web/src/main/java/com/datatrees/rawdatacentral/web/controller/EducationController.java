package com.datatrees.rawdatacentral.web.controller;

import com.datatrees.rawdatacentral.api.RpcEducationService;
import com.datatrees.rawdatacentral.domain.education.EducationParam;
import com.datatrees.rawdatacentral.service.EducationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * Created by zhangyanjia on 2017/11/30.
 */
@RestController
@RequestMapping("/chsi")
public class EducationController {

    private static final Logger logger= LoggerFactory.getLogger(EducationController.class);

    @Resource
    private RpcEducationService educationService;

    /**
     * 学信网登录初始化
     * @param param
     * @return
     */
    @RequestMapping("/login/init")
    public Object loginInit(EducationParam param){
        return educationService.loginInit(param);
    }

    /**
     * 学信网登录提交
     * @param param
     * @return
     */
    @RequestMapping("/login/submit")
    public Object loginSubmit(EducationParam param){
        return educationService.loginSubmit(param);
    }

    @RequestMapping("/register/init")
    public Object registerInit(EducationParam param){
        return educationService.registerInit(param);
    }


    /**
     * 注册刷新图片
     * @param param
     * @return
     */
    @RequestMapping("/register/refeshPicCode")
    public Object registerRefeshPicCode(EducationParam param){
        return educationService.registerRefeshPicCode(param);
    }

    /**
     * 注册校验图片验证码并发送短信验证码
     * @param param
     * @return
     */
    @RequestMapping("/register/ValidatePicCode")
    public Object registerValidatePicCodeAndSendSmsCode(EducationParam param){
        return educationService.registerValidatePicCodeAndSendSmsCode(param);
    }

    /**
     * 注册提交
     * @param param
     * @return
     */
    @RequestMapping("/register/submit")
    public Object registerSubmit(EducationParam param){
        return educationService.registerSubmit(param);
    }

}