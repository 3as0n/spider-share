/*
 * Copyright © 2015 - 2018 杭州大树网络技术有限公司. All Rights Reserved
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

package com.treefinance.crawler.framework.format.number;

import com.treefinance.crawler.framework.format.ConfigurableFormatter;
import com.treefinance.crawler.framework.format.FormatConfig;
import com.treefinance.toolkit.util.RegExp;
import org.apache.commons.collections.MapUtils;

import javax.annotation.Nonnull;

import java.text.DecimalFormat;
import java.util.Map;

/**
 * @author Jerry
 * @since 00:46 2018/6/2
 */
public class NumberFormatter extends ConfigurableFormatter<Number> {

    @Override
    protected Number toFormat(@Nonnull String value, @Nonnull FormatConfig config) throws Exception {
        String val = value.replaceAll("\\s+", "");

        Map<String, NumberUnit> numberMap = config.getNumberFormatMap(this.getConf());
        NumberUnit unit = findNumberUnitForNumber(numberMap, val);
        Number result = null;
        if (unit != null) {
            result = getNumber(val);
            if (result != null) {
                result = calcResult(result, unit);
            }
        }
        return result;
    }

    private Number calcResult(Number result, NumberUnit unit) {
        Double rs = null;
        try {
            rs = unit.getProportion() * result.doubleValue();
        } catch (Exception e) {
            // ignore
        }
        return rs;
    }

    private Number getNumber(String value) {
        Number result = null;
        try {
            result = DecimalFormat.getInstance().parse(value);
        } catch (Exception e) {
            logger.warn("parse Number error! - input: {}", value);
        }
        return result;
    }

    private NumberUnit findNumberUnitForNumber(Map<String, NumberUnit> numberMap, String value) {
        NumberUnit result = null;
        if (MapUtils.isNotEmpty(numberMap)) {
            for (Map.Entry<String, NumberUnit> entry : numberMap.entrySet()) {
                String pattern = entry.getKey();
                if (RegExp.find(value, pattern)) {
                    result = entry.getValue();
                    logger.debug("find period: {} unit : {}", value, result);
                    break;
                }
            }
        }

        if (result == null) {
            logger.debug("can't find correct number format conf and use default instead! value: {}", value);
            result = NumberUnit.ONE;
        }
        return result;
    }

}
