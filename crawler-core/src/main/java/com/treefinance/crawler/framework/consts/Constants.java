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

package com.treefinance.crawler.framework.consts;

import com.datatrees.common.conf.PropertiesConfiguration;

/**
 * @author <A HREF="">Cheng Wang</A>
 * @version 1.0
 * @since Feb 18, 2014 2:32:25 PM
 */
public final class Constants {

    public static final String PARSER_SPLIT = "@PARSER@";

    public static final String EMPTY = "";

    // field result map
    public static final String FIELDS_RESULT_MAP = "Field_RESULT_MAP";

    public static final String HTTP_HEADER_REFERER = "Referer";

    public static final String PAGE_EXTRACT_OBJECT_LIST = "PAGE_EXTRACT_OBJECT_LIST";

    public static final String PAGE_EXTRACT = "PAGE_EXTRACT";

    public static final String CURRENT_LINK_NODE = "CURRENT_LINK_NODE";

    public static final String CURRENT_PAGE = "CURRENT_PAGE";

    public static final String CURRENT_SEARCH_TEMPLATE = "CURRENT_SEARCH_TEMPLATE";

    public static final String URL_BLACK_LIST =
        ".*(\\.(css|js|bmp|gif|jpe?g" + "|png|tiff?|mid|mp2|mp3|mp4" + "|wav|avi|mov|mpeg|ram|m4v|pdf" + "|rm|smil|wmv|swf|wma|zip|rar|gz|xml))$";

    public static final String RESPONSE_LINKNODES = "Response.LinkNodes";

    public static final String RESPONSE_Protocol_OUTPUT = "Response.Protocol.OUTPUT";

    public static final String CRAWLER_URL_FIELD = "url";

    public static final String CRAWLER_REQUEST_URL_HANDLER = "CRAWLER_REQUEST_URL_HANDLER";

    public static final String CRAWLER_RREQUEST_PLUGIN_CONF = "CRAWLER_RREQUEST_PLUGIN_CONF";

    public static final String CRAWLER_RREQUEST_CONF = "CRAWLER_RREQUEST_CONF";

    public static final String REQUEST_BODY = "REQUEST_BODY";

    public static final String CRAWLER_PAGECONTENT_CHARSET = "CRAWLER_PAGECONTENT_CHARSET";

    public static final String CRAWLER_REQUEST_KEYWORD = "CRAWLER_REQUEST_KEYWORD";

    public static final String CRAWLER_DATE_FORMAT = "CRAWLER_DATE_FORMAT";

    public static final String DOWNLOAD_FILE_STORE_PATH = "download.file.store.path";

    public static final String PLUGIN_STORE_PATH = "plugin.store.path";

    public static final String NUMBER_FORMAT_CONFIG = "number.format.config";

    public static final String CRAWLER_REQUEST_NUMBER_MAP = "CRAWLER_REQUEST_NUMBER_MAP";

    public static final String CRAWLER_REQUEST_TEMPLATE = "CRAWLER_REQUEST_TEMPLATE";

    public static final String CRAWLER_REQUEST_RETRY_COUNT = "CRAWLER_REQUEST_RETRY_COUNT";

    public static final String PAGE_REQUEST_CONTEXT_KEYWORD = "keyword";

    public static final String PAGE_REQUEST_CONTEXT_ORIGINAL_KEYWORD = "original_keyword";

    public static final String PAGE_REQUEST_CONTEXT_CURRENT_URL = "current_url";

    public static final String PAGE_REQUEST_CONTEXT_CURRENT_DEPTH = "depth";

    public static final String PAGE_REQUEST_CONTEXT_REFERER_URL = "referer_url";

    public static final String PAGE_REQUEST_CONTEXT_REDIRECT_URL = "redirect_url";

    public static final String PAGE_REQUEST_CONTEXT_PAGE_TITLE = "page_title";

    public static final String PAYMENT_FROMAT_CONFIG = "PAYMENT_FROMAT_CONFIG";

    public static final String RMB_FROMAT_CONFIG = "RMB_FROMAT_CONFIG";

    public static final String REQUEST_PREFIX = "REQUEST_PREFIX";

    public static final String BODY_CHARSET_PATTERN = "<meta\\b[^>]*\\bcharset\\s*=\\s*['\"]?([-\\w.]+)";

    public static final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd";

    public static final String COOKIE = "cookie";

    public static final String COOKIE_STRING = "cookie_string";

    public static final String ACCOUNT_KEY = "account_key";

    public static final String USERNAME = "username";

    public static final String PASSWORD = "password";

    public static final String TASK_UNIQUE_SIGN = "TASK_UNIQUE_SIGN";

    public static final String THREAD_LOCAL_RESPONSE = "THREAD_LOCAL_RESPONSE";

    public static final String THREAD_LOCAL_LINKNODE = "THREAD_LOCAL_LINKNODE";

    public static final String HTTP_STATE = "HTTP_STATE";

    public static final String HTTP_CLIENT_TYPE = "http_client_type";

    public static final String HEADER_CHARSET_PATTERN = "charset=([\\w-]+)";

    public static final String REDIRECT_URL = "REDIRECT_URL";

    public static final String URL_REGEX = PropertiesConfiguration.getInstance().get("url.extractor.regex",
        "(mms://[^<'\\r\\n\\t#]+|rtsp://[^<'\\r\\n\\t#]+|rtmp://[^<'\\r\\n\\t#]+|pa://[^<'\\r\\n\\t#]+|thunder://[^<'\\r\\n\\t#:]+|bdhd://[^<'\\r\\n\\t#:]+|qvod://[^<'\\r\\n\\t#:]+|qvod://[^<'\\r\\n\\t#:]+|((([-\\w]+\\.)+(com|org|net|edu|gov|info|biz|eu|us|cn|jp|uk|hk|io|de|ru))/([-\\w]+\\.)+[\\w-]+(:\\d+)?|"
            + "(((www\\.)|(https?(:|&#58;)//))([-\\w]+\\.)+[\\w-]+(:\\d+)?))(/[\\w-\\./?%&=\\*\\+\\[\\]\\(\\) ,:!]*)?)");

    private Constants() {}
}
