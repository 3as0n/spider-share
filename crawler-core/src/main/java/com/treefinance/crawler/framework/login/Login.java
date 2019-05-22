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
import com.treefinance.crawler.framework.context.SearchProcessorContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author <A HREF="">Cheng Wang</A>
 * @version 1.0
 * @since Mar 16, 2014 4:14:49 PM
 */
public enum Login {
    INSTANCE;
    private static final Logger logger = LoggerFactory.getLogger(Login.class);

    public Status doLogin(SearchProcessorContext context) throws Exception {
        if (context.needLogin()) {
            LoginConfig loginConfig = context.getLoginConfig();
            LoginType loginType = loginConfig.getType();

            boolean isSuccess = LoginHandler.get(loginType).login(context);

            if (isSuccess) {
                logger.info("Cookie checking is pass ");
                return Status.SUCCEED;
            } else if (loginType == LoginType.SERVER) {
                logger.info("Cookie checking is not pass with server's login, switch to client login!");
                isSuccess = LoginHandler.CLIENT_LOGIN.login(context);
                if (isSuccess) {
                    logger.info("Cookie checking is pass ");
                    context.getLoginResource().putCookie(context.getLoginAccountKey(), context.getCookiesAsString());
                    return Status.SUCCEED;
                }
            }
            logger.warn("Cookie checking is not pass ");
            return Status.FAILED;
        }

        return Status.NONE;
    }

    public enum Status {
        SUCCEED,
        FAILED,
        NONE
    }

}
