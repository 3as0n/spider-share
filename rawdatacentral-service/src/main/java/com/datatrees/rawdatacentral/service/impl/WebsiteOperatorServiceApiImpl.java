package com.datatrees.rawdatacentral.service.impl;

import javax.annotation.Resource;
import java.util.List;

import com.datatrees.rawdatacentral.api.WebsiteOperatorServiceApi;
import com.datatrees.rawdatacentral.domain.model.WebsiteOperator;
import com.datatrees.rawdatacentral.domain.result.HttpResult;
import com.datatrees.rawdatacentral.service.WebsiteOperatorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class WebsiteOperatorServiceApiImpl implements WebsiteOperatorServiceApi {

    private static final Logger logger = LoggerFactory.getLogger(WebsiteOperatorServiceImpl.class);
    @Resource
    private WebsiteOperatorService websiteOperatorService;

    @Override
    public HttpResult<Boolean> updateEnable(String websiteName, Boolean enable) {
        HttpResult<Boolean> result = new HttpResult<>();
        try {
            websiteOperatorService.updateEnable(websiteName, enable);
            logger.info("updateEnable success wesiteName={},enable={}", websiteName, enable);
            return result.success(true);
        } catch (Throwable e) {
            logger.error("updateEnable error wesiteName={},enable={}", websiteName, enable, e);
            return result.failure();
        }
    }

    @Override
    public HttpResult<List<WebsiteOperator>> queryDisable() {
        HttpResult<List<WebsiteOperator>> result = new HttpResult<>();
        try {
            List<WebsiteOperator> list = websiteOperatorService.queryDisable();
            logger.info("queryDisable success size={}", list.size());
            return result.success(list);
        } catch (Throwable e) {
            logger.error("queryDisable error ", e);
            return result.failure();
        }
    }

}