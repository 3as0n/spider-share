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

package com.treefinance.crawler.support.selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.function.Function;

/**
 * @author Jerry
 * @since 17:57 12/12/2017
 */
public final class SeleniumHelper {

    private SeleniumHelper() {}

    /**
     * 执行JavaScript方法
     * 
     * @param script 待执行待script
     * @param args script需要的参数
     * @return script执行结果
     */
    public static Object evalScript(WebDriver webDriver, String script, Object... args) {
        return ((JavascriptExecutor)webDriver).executeScript(script, args);
    }

    /**
     * Wait will ignore instances of NotFoundException that are encountered (thrown) by default in the 'until'
     * condition, and immediately propagate all others. You can add more to the ignore list by calling
     * ignoring(exceptions to add).
     * 
     * @param webDriver The WebDriver instance to pass to the expected conditions
     * @param function the 'util' condition
     * @param timeout The timeout in seconds when an expectation is called
     */
    public static <V> V waitUtil(WebDriver webDriver, Function<WebDriver, V> function, long timeout) {
        return (new WebDriverWait(webDriver, timeout)).until(function);
    }

    /**
     * Wait will ignore instances of NotFoundException that are encountered (thrown) by default in the 'until'
     * condition, and immediately propagate all others. You can add more to the ignore list by calling
     * ignoring(exceptions to add).
     * 
     * @param webDriver The WebDriver instance to pass to the expected conditions
     * @param function the 'util' condition
     * @param timeout The timeout in seconds when an expectation is called
     * @param sleepMillis The duration in milliseconds to sleep between polls.
     */
    public static <V> V waitUtil(WebDriver webDriver, Function<WebDriver, V> function, long timeout, long sleepMillis) {
        return (new WebDriverWait(webDriver, timeout, sleepMillis)).until(function);
    }

    /**
     * 根据class name 查找页面元素
     * 
     * @param className the class name of element that to find
     * @return {@link WebElement}
     */
    public static WebElement findElementByClass(WebDriver webDriver, String className) {
        return webDriver.findElement(By.className(className));
    }

    /**
     * 根据id查找页面元素
     * 
     * @param id the id of element that to find
     * @return {@link WebElement}
     */
    public static WebElement findElementById(WebDriver webDriver, String id) {
        return webDriver.findElement(By.id(id));
    }

    /**
     * 当前页面定向到指定URL的页面
     * 
     * @param url 重定向的URL
     */
    public static void navigateTo(WebDriver webDriver, String url) {
        webDriver.navigate().to(url);
    }

    /**
     * 刷新当前页面
     */
    public static void refreshPage(WebDriver webDriver) {
        webDriver.navigate().refresh();
    }

}
