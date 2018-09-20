

/*
 * Copyright © 2015 - 2018 杭州大树网络技术有限公司. All Rights Reserved
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import ch.qos.logback.classic.encoder.PatternLayoutEncoder
import com.datatrees.spider.share.common.utils.DateUtils

import java.nio.charset.Charset

scan("60 seconds")
def charsetName = "UTF-8"
def appName = "spider"
def serverIp = System.getProperty("server.ip","127.0.0.1")

def ips =serverIp.split("\\.");
// 日志路径
def publishDate = DateUtils.format(new Date(), "MMdd")
def logPath = "/dashu/log/${appName}"

// 控制台
appender("consoleAppender", ConsoleAppender) {
    encoder(PatternLayoutEncoder) {
        pattern = "%d{yyyy-MM-dd HH:mm:ss} [%p] [%.10t] [%c{1}][%M][%L] %m%n"
        charset = Charset.forName(charsetName)
    }
}

// 业务日志
appender("sysAppender", RollingFileAppender) {
    file = "${logPath}/sys.log"
    encoder(PatternLayoutEncoder) {
        pattern = "%d{yyyy-MM-dd HH:mm:ss} [%p] [%.10t] [%c{1}][%M][%L] %m%n"
        charset = Charset.forName(charsetName)
    }
    rollingPolicy(SizeAndTimeBasedRollingPolicy) {
        fileNamePattern = "${logPath}/sys.log.%d{yyyy-MM-dd}.%i"
        maxFileSize = "1GB"
        maxHistory = 7 // 保留最近天数的日志
    }
}

// plugin日志
appender("pluginAppender", RollingFileAppender) {
    file = "${logPath}/plugin.log"
    encoder(PatternLayoutEncoder) {
        pattern = "%d{yyyy-MM-dd HH:mm:ss} [%p] [%.10t] [%c{1}][%M][%L] %m%n"
        charset = Charset.forName(charsetName)
    }
    rollingPolicy(SizeAndTimeBasedRollingPolicy) {
        fileNamePattern = "${logPath}/plugin.log.%d{yyyy-MM-dd}.%i"
        maxFileSize = "1GB"
        maxHistory = 7 // 保留最近天数的日志
    }
}





root(INFO, ["consoleAppender", "sysAppender"])
logger("plugin_log", INFO, ["pluginAppender"], false)
logger("org.apache.http.impl.conn.Wire", OFF)
logger("org.apache.http", INFO)
logger("org.apache.http.client.protocol.ResponseProcessCookies", INFO)
//logger("org.apache.http.client.protocol.ResponseProcessCookies",DEBUG)
logger("com.alibaba.dubbo.monitor.dubbo", OFF)
logger("com.alibaba.dubbo.rpc.cluster.support.wrapper", OFF)
logger("com.alibaba.dubbo.rpc.protocol.dubbo", OFF)
logger("com.datatrees.spider.spider.share.service.impl.FileStoreServiceImpl", OFF)
logger("com.datatrees.spider.spider.share.service.impl.AppCrawlerConfigServiceImpl", INFO)
logger("com.treefinance.crawler.framework.process.operation", INFO)
logger("com.datatrees.crawler.core.processor.extractor", INFO)

