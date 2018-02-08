package com.datatrees.rawdatacentral.service;

import com.datatrees.rawdatacentral.domain.education.EducationParam;
import com.datatrees.rawdatacentral.domain.result.HttpResult;
import org.junit.Test;

import javax.annotation.Resource;
import java.util.Map;

/**
 * Created by zhangyanjia on 2017/11/30.
 */
public class educationTest extends BaseTest {
    @Resource
    private EducationService educationService;
    @Test
    public void iniTest(){
        EducationParam educationParam=new EducationParam();

        educationParam.setTaskId(1122334455L);
        educationParam.setWebsiteName("chsi.com.cn");
        HttpResult<Map<String,Object>> result=educationService.loginInit(educationParam);
        System.out.println(result);
    }
}