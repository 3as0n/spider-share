/*
 * Copyright © 2015 - 2019 杭州大树网络技术有限公司. All Rights Reserved
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */

package com.treefinance.crawler.framework.format.money;

import com.treefinance.crawler.framework.format.ConfigurableFormatter;
import com.treefinance.crawler.framework.format.FormatConfig;
import com.treefinance.toolkit.util.RegExp;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;

import javax.annotation.Nonnull;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Jerry
 * @since 00:45 2018/6/2
 */
public class RmbFormatter extends ConfigurableFormatter<Number> {

    private static final String PAYMENT_NUM_REGEX = "([\\.,\\d]+)";

    @Override
    protected Number toFormat(@Nonnull String value, @Nonnull FormatConfig config) throws Exception {
        String numPart = RegExp.group(value, PAYMENT_NUM_REGEX, 1);

        String actualPattern = config.trimmedPattern();
        Map<String, RMBUnit> rmbMapper = buildRMPUnitMapping(actualPattern);
        RMBUnit unit = findRMBUnit(rmbMapper, value);

        if (unit == null) {
            rmbMapper = RmbUnitMapping.getRMBUnitMap(getConf());
            unit = findRMBUnit(rmbMapper, value);
        }

        if (unit == null) {
            logger.debug("can't find current RMB conf! input: {}, numPart: {}, pattern: {}", value, numPart, actualPattern);
            unit = RMBUnit.YUAN;
        }

        // save to .00
        double temp = Double.valueOf(numPart);

        return temp * unit.getConversion();
    }

    private RMBUnit findRMBUnit(Map<String, RMBUnit> unitMap, String value) {
        RMBUnit result = null;
        if (MapUtils.isNotEmpty(unitMap) && StringUtils.isNotEmpty(value)) {
            String input = value.toLowerCase();
            for (Map.Entry<String, RMBUnit> entry : unitMap.entrySet()) {
                String pattern = entry.getKey().toLowerCase();
                if (RegExp.find(input, pattern)) {
                    result = entry.getValue();
                    logger.debug("find RMB {} unit : {}", value, result);
                    break;
                }
            }
        }
        return result;
    }

    private Map<String, RMBUnit> buildRMPUnitMapping(String pattern) {
        if (StringUtils.isNotEmpty(pattern)) {
            String[] values = pattern.split("###");
            if (values.length == 3) {
                Map<String, RMBUnit> map = new HashMap<>();
                map.put(values[0], RMBUnit.YUAN);
                map.put(values[1], RMBUnit.JIAO);
                map.put(values[2], RMBUnit.FEN);
                return map;
            }
        }
        return null;
    }
}
