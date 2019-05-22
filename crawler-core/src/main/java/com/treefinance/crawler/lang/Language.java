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

package com.treefinance.crawler.lang;

import com.datatrees.common.conf.PropertiesConfiguration;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author <A HREF="">Cheng Wang</A>
 * @version 1.0
 * @since 2015年9月20日 下午5:30:33
 */
public final class Language {

    public final static String LANGUAGE_JP = "JP";
    public final static String LANGUAGE_CH = "CH";
    public final static String LANGUAGE_RU = "RU";
    public final static String LANGUAGE_KO = "KO";
    public final static String LANGUAGE_AR = "AR";
    public final static String LANGUAGE_DE = "DE";
    public final static String LANGUAGE_TH = "TH";
    public final static String LANGUAGE_EN = "EN";
    public final static String USER_LANGUAGE_CONFIG = PropertiesConfiguration.getInstance().get("user.language.config", "");
    public final static String USER_LANGUAGE_PATTERN_PREFIX = "pattern.";
    private static final Language INSTANCE = new Language();
    private Map<String, Pattern> languagePatternMapping = new LinkedHashMap<String, Pattern>();

    private Language() {
        init();
    }

    public static Language getInstance() {
        return INSTANCE;
    }

    public boolean check(String word, Pattern pattern) {
        Matcher matcher = pattern.matcher(word);
        return matcher.find();
    }

    public String getLanguage(String word) {
        String resultLanguage = LANGUAGE_EN;
        for (Map.Entry<String, Pattern> l : languagePatternMapping.entrySet()) {
            String language = l.getKey();
            if (check(word, l.getValue())) {
                resultLanguage = language;
                break;
            }
        }
        return resultLanguage;
    }

    private void init() {
        // add default configuration
        addDefaultConfig();
        // add extended configuration
        addUserConfig();
    }

    private void addDefaultConfig() {
        languagePatternMapping.put(LANGUAGE_JP, Pattern.compile("[\\u3041-\\u30FF\\u3104-\\u312A\\uFF66-\\uFF9E]+"));
        languagePatternMapping.put(LANGUAGE_CH, Pattern.compile("[\\u4E00-\\u9FFF]+"));
        languagePatternMapping.put(LANGUAGE_RU, Pattern.compile("[\\u0400-\\u052F]+"));
        languagePatternMapping.put(LANGUAGE_KO, Pattern.compile("[\\uAC00-\\uD7AC]+"));
        languagePatternMapping.put(LANGUAGE_AR, Pattern.compile("[\\u0600-\\u06FF-\\u0750-\\u077F]+"));
        languagePatternMapping.put(LANGUAGE_DE, Pattern.compile("[\\u00C0-\\u00FF]+"));
        languagePatternMapping.put(LANGUAGE_TH, Pattern.compile("[\\u0E00-\\u0E7F]+"));
        languagePatternMapping.put(LANGUAGE_EN, Pattern.compile("[A-Za-z]+"));
    }

    private void addUserConfig() {
        if (StringUtils.isNotEmpty(USER_LANGUAGE_CONFIG)) {
            Map<String, Pattern> userLanguagePatternMapping = new LinkedHashMap<String, Pattern>();
            for (String lan : USER_LANGUAGE_CONFIG.split(",")) {
                String patternString = PropertiesConfiguration.getInstance().get(USER_LANGUAGE_PATTERN_PREFIX + lan);
                if (StringUtils.isNotEmpty(patternString)) {
                    userLanguagePatternMapping.put(lan.toUpperCase(), Pattern.compile(patternString));
                }
            }
            if (MapUtils.isNotEmpty(userLanguagePatternMapping)) {
                languagePatternMapping.putAll(userLanguagePatternMapping);
            }
        }
    }
}
