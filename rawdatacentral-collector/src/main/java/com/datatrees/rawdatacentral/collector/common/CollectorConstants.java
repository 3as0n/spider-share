/**
 * This document and its contents are protected by copyright 2015 and owned by datatrees.com Inc.
 * The copying and reproduction of this document and/or its content (whether wholly or partly) or
 * any incorporation of the same into any other material in any media or format of any kind is
 * strictly prohibited. All rights are reserved.
 * Copyright (c) datatrees.com Inc. 2015
 */

package com.datatrees.rawdatacentral.collector.common;

import java.util.ArrayList;
import java.util.List;

import com.datatrees.common.conf.PropertiesConfiguration;
import com.datatrees.common.protocol.ProtocolStatusCodes;

/**
 * @author <A HREF="mailto:wangcheng@datatrees.com.cn">Cheng Wang</A>
 * @version 1.0
 * @since 2015年7月19日 下午9:04:54
 */
public interface CollectorConstants {

    public static PropertiesConfiguration sysConfig                             = (PropertiesConfiguration) PropertiesConfiguration.getInstance();
    public static int                     MAX_QUEUE_SIZE                        = 128;
    // === COLLECTOR properties configuration ===
    public static int                     COLLECTOR_JOB_INTERVAL                = sysConfig.getInt("collector.job.interval", 60);
    public static String                  COLLECTOR_WEBSERVICE_URL              = sysConfig.get("collector.web.service.url", "http://127.0.0.1:8082/");
    public static String                  COLLECTOR_TEMP_DIR                    = sysConfig.get("collector.link.queue.dir", "/tmp/urlQueue/");
    public static int                     COLLECTOR_BDB_MAX_DB_COUNT            = sysConfig.getInt("collector.bdb.max.db.count", 10);
    public static String                  COLLECTOR_CALSS_SUFFIX                = "COLLECTOR";
    public static String                  DATABASE_NAME_SUFFIX                  = "database_";
    public static String                  SECONDARY_NAME_SUFFIX                 = "secondary_";
    public static String                  DEFUALT_ENCODING                      = "UTF-8";
    public static int                     DEFUALT_MAX_PAGE                      = 10000;
    public static List<Integer>           REQUEST_RETRY_LIST                    = new ArrayList<Integer>() {
        {
            /** Temporary failure. Application may retry immediately. */
            add(ProtocolStatusCodes.RETRY);
            /** Unspecified exception occured. Further information may be provided in args. */
            add(ProtocolStatusCodes.EXCEPTION);
            /** Access denied by robots.txt rules. */
            add(ProtocolStatusCodes.ROBOTS_DENIED);
            /** Too many redirects. */
            add(ProtocolStatusCodes.REDIR_EXCEEDED);
            /** Content was not retrieved. Any further errors may be indicated in args. */
            add(ProtocolStatusCodes.FAILED);
        }
    };
    public static String                  NEW_LINK_SITE_SENDER                  = "newLinkSiteSender";
    public static String                  URL_LIST_MD5                          = "urlListMD5";
    public static String                  REFERER_URL                           = "referer_url";
    // unit:minutes
    public static int                     DUPLICATE_BEFORE_REQUEST_FAILURE_TIME = sysConfig.getInt("collector.download.url.before.request.duplicate.failure.time", 120);
    // unit:day
    public static int                     DUPLICATE_AFTER_REQUEST_FAILURE_TIME  = sysConfig.getInt("collector.download.url.after.request.duplicate.failure.time", 14);
    public static String                  POST_DATE                             = "post_date";
    public static String                  DATE_FORMAT                           = "yyyy-MM-dd HH:mm:ss";
    public static String                  RESOURCEURL_REVERSE_SWITCH            = sysConfig.get("collector.resource.reverse.switch", "OFF");
    public static String                  LINK_EXISTS                           = "LINK_EXISTS";
    public static String                  LINK_TIMEOUT                          = "LINK_TIMEOUT";
    public static int                     REFERER_LIMIT_SIZE                    = sysConfig.getInt("collector.referer.url.limit.size", 500);
    public static long                    EXTRACT_ACTOR_TIMEOUT                 = sysConfig.getLong("extract.actor.timeout", 180000);

}
