package com.datatrees.rawdatacentral.service.impl;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.datatrees.common.conf.PropertiesConfiguration;
import com.datatrees.rawdatacentral.common.http.TaskHttpClient;
import com.datatrees.rawdatacentral.common.utils.CheckUtils;
import com.datatrees.rawdatacentral.common.utils.RegexpUtils;
import com.datatrees.rawdatacentral.common.utils.TemplateUtils;
import com.datatrees.rawdatacentral.dao.WebsiteConfigDAO;
import com.datatrees.rawdatacentral.dao.WebsiteOperatorDAO;
import com.datatrees.rawdatacentral.domain.enums.ErrorCode;
import com.datatrees.rawdatacentral.domain.enums.RedisKeyPrefixEnum;
import com.datatrees.rawdatacentral.domain.enums.RequestType;
import com.datatrees.rawdatacentral.domain.exception.CommonException;
import com.datatrees.rawdatacentral.domain.model.WebsiteOperator;
import com.datatrees.rawdatacentral.domain.model.example.WebsiteOperatorExample;
import com.datatrees.rawdatacentral.domain.vo.WebsiteConfig;
import com.datatrees.rawdatacentral.service.WebsiteOperatorService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class WebsiteOperatorServiceImpl implements WebsiteOperatorService {

    private static final Logger              logger = LoggerFactory.getLogger(WebsiteOperatorServiceImpl.class);
    private static final Map<String, String> hosts  = new HashMap<>();

    static {
        hosts.put("开发", "192.168.5.15:6789");
        hosts.put("测试", "rawdatacentral.saas.test.treefinance.com.cn");
        hosts.put("准生产", "rawdatacentral.approach.saas.treefinance.com.cn");
        hosts.put("预发布", "rawdatecentral.yfb.saas.treefinance.com.cn");
    }

    @Resource
    private WebsiteOperatorDAO websiteOperatorDAO;
    @Resource
    private WebsiteConfigDAO   websiteConfigDAO;

    @Override
    public WebsiteOperator getByWebsiteName(String websiteName) {
        CheckUtils.checkNotBlank(websiteName, ErrorCode.EMPTY_WEBSITE_NAME);
        WebsiteOperatorExample example = new WebsiteOperatorExample();
        example.createCriteria().andWebsiteNameEqualTo(websiteName);
        List<WebsiteOperator> list = websiteOperatorDAO.selectByExample(example);
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public void importWebsite(WebsiteOperator config) {
        CheckUtils.checkNotNull(config, "config is null");
        CheckUtils.checkNotBlank(config.getWebsiteName(), ErrorCode.EMPTY_WEBSITE_NAME);
        WebsiteConfig source = websiteConfigDAO.getWebsiteConfig(null, config.getWebsiteName());
        if (null == source) {
            throw new CommonException("websiteName config not found");
        }
        if (StringUtils.isBlank(config.getOperatorType())) {
            String websiteType = RegexpUtils.select(config.getWebsiteName(), "\\d+", 0);
            config.setOperatorType(websiteType);
        }
        String region = config.getWebsiteTitle().split("\\(")[0].replaceAll("移动", "").replaceAll("联通", "").replaceAll("电信", "");
        config.setRegionName(region);

        config.setWebsiteId(source.getWebsiteId());
        config.setWebsiteName(source.getWebsiteName());
        config.setSearchConfig(source.getSearchConfig());
        config.setExtractorConfig(source.getExtractorConfig());
        config.setLoginTip(source.getLoginTip());
        config.setVerifyTip(source.getVerifyTip());
        config.setResetType(source.getResetType());
        config.setResetTip(source.getResetTip());
        if (!StringUtils.equals("null", source.getSmsTemplate())) {
            config.setSmsTemplate(source.getSmsTemplate());
        }
        if (!StringUtils.equals("null", source.getResetURL())) {
            config.setResetUrl(source.getResetURL());
        }
        config.setSmsReceiver(source.getSmsReceiver());
        config.setSimulate(source.getSimulate());
        if (StringUtils.isBlank(config.getLoginConfig())) {
            config.setLoginConfig(source.getInitSetting().trim().replaceAll(" ", "").replaceAll("\\n", ""));
        }
        if (StringUtils.isBlank(config.getPluginClass())) {
            String pluginClass = PropertiesConfiguration.getInstance().get(RedisKeyPrefixEnum.PLUGIN_CLASS.getRedisKey(config.getWebsiteName()));
            config.setPluginClass(pluginClass);
        }
        websiteOperatorDAO.insertSelective(config);
    }

    @Override
    public void updateWebsite(WebsiteOperator config) {
        WebsiteOperator operatorDb = getByWebsiteName(config.getWebsiteName());
        if (null == operatorDb) {
            throw new CommonException("websiteName not found,websiteName=" + config.getWebsiteName());
        }
        CheckUtils.checkNotBlank(config.getSearchConfig(), "searchConfig is empty");
        CheckUtils.checkNotBlank(config.getExtractorConfig(), "extractorConfig is empty");
        WebsiteOperator updateObj = new WebsiteOperator();
        updateObj.setWebsiteId(operatorDb.getWebsiteId());
        updateObj.setSearchConfig(config.getSearchConfig());
        updateObj.setExtractorConfig(config.getExtractorConfig());
        websiteOperatorDAO.updateByPrimaryKeySelective(updateObj);
    }

    @Override
    public void importConfig(String websiteName, String from) {
        CheckUtils.checkNotBlank(websiteName, ErrorCode.EMPTY_WEBSITE_NAME);
        CheckUtils.checkNotBlank(from, "empty params from");
        if (!hosts.containsKey(from)) {
            throw new RuntimeException("from 配置不存在");
        }
        String queryUrl = TemplateUtils.format("http://{}/website/operator/getByWebsiteName?websiteName=zhe_jiang_10086_web", hosts.get(from));
        String json = TaskHttpClient.create(6L, "china_10000_app", RequestType.POST, "china_10000_app_001").setFullUrl(queryUrl)
                .setProxyEnable(false).invoke().getPageContent();
        WebsiteOperator config = JSON.parseObject(json, new TypeReference<WebsiteOperator>() {});
        if (null == config || StringUtils.isBlank(config.getWebsiteName())) {
            throw new RuntimeException("config not found");
        }
        WebsiteOperator websiteOperatorDb = getByWebsiteName(websiteName);
        if (null == websiteOperatorDb) {
            websiteOperatorDAO.insertSelective(config);
        } else {
            config.setWebsiteId(websiteOperatorDb.getWebsiteId());
            websiteOperatorDAO.updateByPrimaryKeySelective(config);
        }
        logger.info("迁入运营商配置成功,websiteName={},from={}", websiteName, from);
    }

}
