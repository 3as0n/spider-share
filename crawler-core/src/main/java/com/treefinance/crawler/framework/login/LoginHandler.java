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

package com.treefinance.crawler.framework.login;

import com.treefinance.crawler.framework.config.enums.LoginType;
import com.treefinance.crawler.framework.config.xml.login.LoginConfig;
import com.treefinance.crawler.framework.config.xml.plugin.AbstractPlugin;
import com.treefinance.crawler.framework.context.ProcessorContextUtil;
import com.treefinance.crawler.framework.context.SearchProcessorContext;
import com.treefinance.crawler.framework.exception.LoginException;
import com.treefinance.crawler.framework.exception.ResultEmptyException;
import com.treefinance.crawler.framework.extension.plugin.PluginCaller;
import com.treefinance.crawler.framework.extension.plugin.PluginConstants;
import com.treefinance.crawler.framework.extension.plugin.PluginUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Jerry
 * @since 14:10 2018/6/22
 */
public enum LoginHandler {
    APP_LOGIN(LoginType.APP) {
        @Override
        Cookie applyCookies(LoginConfig loginConfig, SearchProcessorContext context) {
            return null;
        }

        @Override
        boolean checkCookies() {
            return true;
        }
    },
    PLUGIN_LOGIN(LoginType.PLUGIN) {
        @Override
        Cookie applyCookies(LoginConfig loginConfig, SearchProcessorContext context) throws LoginException {
            AbstractPlugin pluginDesc = loginConfig.getPlugin();
            if (pluginDesc == null) {
                throw new LoginException("There was no available login plugin!");
            }

            String cookies = (String)PluginCaller.call(pluginDesc, context, null);
            logger.info("Acquire cookies by plugin. cookies: {}", cookies);

            Map<String, Object> resultMap = PluginUtil.checkPluginResult(cookies);

            Object val = resultMap.remove(PluginConstants.COOKIE);
            if (!(val instanceof String) || StringUtils.isEmpty((String)val)) {
                throw new LoginException("Invalid cookies!");
            }

            cookies = (String)val;

            // set other params from login to context
            if (!resultMap.isEmpty()) {
                context.addAttributes(resultMap);
            }

            return new Cookie(cookies);
        }
    },
    SERVER_LOGIN(LoginType.SERVER) {
        @Override
        Cookie applyCookies(LoginConfig loginConfig, SearchProcessorContext context) {
            Cookie cookie = context.getLoginCookies();
            if (cookie == null) {
                cookie = Cookie.EMPTY;
            }
            return cookie;
        }
    },
    CLIENT_LOGIN(LoginType.CLIENT) {
        @Override
        Cookie applyCookies(LoginConfig loginConfig, SearchProcessorContext context) throws LoginException {
            WebsiteAccount account = context.getLoginAccount();
            if (account == null) {
                throw new LoginException("no active accounts while do client login for " + context.getLoginAccountKey());
            }

            ProcessorContextUtil.setAccount(context, account);

            String cookies;
            AbstractPlugin pluginDesc = loginConfig.getPlugin();
            if (pluginDesc != null) {// plugin
                cookies = (String)PluginCaller.call(pluginDesc, context, () -> {
                    Map<String, String> params = new HashMap<>();
                    params.put(PluginConstants.USERNAME, account.getUserName());
                    params.put(PluginConstants.PASSWORD, account.getPassword());

                    return params;
                });
                logger.info("Acquire cookies by plugin. cookies: {}", cookies);

                Map<String, Object> resultMap = PluginUtil.checkPluginResult(cookies);

                Object val = resultMap.remove(PluginConstants.COOKIE);
                if (!(val instanceof String) || StringUtils.isEmpty((String)val)) {
                    throw new LoginException("Invalid cookies!");
                }

                cookies = (String)val;

                if (!resultMap.isEmpty()) {
                    context.addAttributes(resultMap);
                }
            } else {// httpclient
                cookies = LoginUtil.getInstance().doLogin(loginConfig, context);
            }

            return new Cookie(account.getUserName(), cookies);
        }
    };
    private static final Logger logger = LoggerFactory.getLogger(LoginHandler.class);
    private LoginType type;

    LoginHandler(LoginType type) {
        this.type = type;
    }

    public static LoginHandler get(LoginType loginType) {
        LoginHandler[] values = LoginHandler.values();
        for (LoginHandler value : values) {
            if (value.getType().equals(loginType)) {
                return value;
            }
        }
        throw new UnsupportedOperationException("Can not find login handler. login-type : " + loginType);
    }

    public LoginType getType() {
        return type;
    }

    public boolean login(SearchProcessorContext context) throws LoginException, ResultEmptyException {
        LoginConfig loginConfig = context.getLoginConfig();

        Cookie cookie = applyCookies(loginConfig, context);
        if (cookie != null) {
            // set cookie to context
            ProcessorContextUtil.setCookieObject(context, cookie);
            logger.info("Set cookies: {}", cookie);
        }

        return LoginUtil.getInstance().doLoginByCookies(loginConfig, context, checkCookies());
    }

    boolean checkCookies() {
        return false;
    }

    abstract Cookie applyCookies(LoginConfig loginConfig, SearchProcessorContext context) throws LoginException;

}
