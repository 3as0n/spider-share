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

package com.treefinance.crawler.support.selenium;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

/**
 * @author Jerry
 * @since 23:02 26/11/2017
 */
public class FirefoxDriverBuilder extends RemoteWebDriverBuilder<FirefoxDriverBuilder> {

    private FirefoxDriverBuilder(String remoteUrl) {
        super(DesiredCapabilities.firefox(), remoteUrl);
    }

    public static FirefoxDriverBuilder newBuilder(String remoteUrl) {
        return new FirefoxDriverBuilder(remoteUrl);
    }

    @Override
    protected void settingDefault(DesiredCapabilities capabilities, boolean disableCSS, boolean disableImage) {
        // 禁用CSS表
        if (disableCSS) {
            FirefoxProfile profile = getOrDefault(capabilities);
            profile.setPreference("permissions.default.stylesheet", 2);
        }
        // 禁用图片
        if (disableImage) {
            FirefoxProfile profile = getOrDefault(capabilities);
            profile.setPreference("permissions.default.image", 2);
        }
    }

    @Override
    protected void settingUserAgent(DesiredCapabilities capabilities, String userAgent) {
        if (StringUtils.isNotEmpty(userAgent)) {
            FirefoxProfile profile = getOrDefault(capabilities);
            profile.setPreference("general.useragent.override", userAgent);
        }
    }

    @Override
    protected void settingProxy(DesiredCapabilities capabilities, String proxy) {
        Proxy proxySetting = new Proxy();
        proxySetting.setHttpProxy(proxy).setFtpProxy(proxy).setSslProxy(proxy);
        capabilities.setCapability(CapabilityType.PROXY, proxySetting);
    }

    private FirefoxProfile getOrDefault(DesiredCapabilities capabilities) {
        Object capability = capabilities.getCapability(FirefoxDriver.PROFILE);
        if (capability == null) {
            capability = new FirefoxProfile();
            capabilities.setCapability(FirefoxDriver.PROFILE, capability);
        } else if (!(capability instanceof FirefoxProfile)) {
            throw new IllegalArgumentException("Incorrect capability[firefox.profile]! >>> " + capability.getClass());
        }

        return (FirefoxProfile)capability;
    }

}
