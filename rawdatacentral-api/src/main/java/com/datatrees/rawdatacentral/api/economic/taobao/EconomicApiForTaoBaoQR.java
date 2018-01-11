package com.datatrees.rawdatacentral.api.economic.taobao;

import com.datatrees.rawdatacentral.domain.plugin.CommonPluginParam;
import com.datatrees.rawdatacentral.domain.result.HttpResult;

/**
 * Created by guimeichao on 18/1/11.
 */
public interface EconomicApiForTaoBaoQR {

    HttpResult<Object> init(CommonPluginParam param);

    HttpResult<Object> refeshPicCode(CommonPluginParam param);

    HttpResult<Object> login(CommonPluginParam param);

}
