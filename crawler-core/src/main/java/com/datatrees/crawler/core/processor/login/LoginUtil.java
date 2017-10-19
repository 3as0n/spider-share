package com.datatrees.crawler.core.processor.login;

import java.util.List;
import java.util.Map;

import com.datatrees.common.conf.PropertiesConfiguration;
import com.datatrees.common.pipeline.Request;
import com.datatrees.common.pipeline.Response;
import com.datatrees.common.protocol.ProtocolOutput;
import com.datatrees.common.protocol.http.HTTPConstants;
import com.datatrees.common.protocol.metadata.Metadata;
import com.datatrees.common.protocol.util.CookieFormater;
import com.datatrees.common.util.GsonUtils;
import com.datatrees.common.util.PatternUtils;
import com.datatrees.crawler.core.domain.config.login.LoginCheckConfig;
import com.datatrees.crawler.core.domain.config.login.LoginConfig;
import com.datatrees.crawler.core.domain.config.segment.AbstractSegment;
import com.datatrees.crawler.core.processor.SearchProcessorContext;
import com.datatrees.crawler.core.processor.bean.LinkNode;
import com.datatrees.crawler.core.processor.common.*;
import com.datatrees.crawler.core.processor.common.exception.ResultEmptyException;
import com.datatrees.crawler.core.processor.segment.SegmentBase;
import com.datatrees.crawler.core.processor.service.ServiceBase;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author <A HREF="mailto:wangcheng@datatrees.com.cn">Cheng Wang</A>
 * @version 1.0
 * @since Feb 21, 2014 10:00:52 AM
 */
public class LoginUtil {

    private static final Logger logger = LoggerFactory.getLogger(LoginUtil.class);
    private static LoginUtil loginUtil;

    static {
        loginUtil = new LoginUtil();
    }

    private LoginUtil() {
    }

    /**
     *
     */
    public static LoginUtil getInstance() {
        return loginUtil;
    }

    /**
     * map key :username ,password
     */
    @SuppressWarnings("unchecked")
    public String doLogin(LoginConfig config, SearchProcessorContext context) {
        try {
            String jsonHeader = config.getHeaders();
            String loginUrl = ReplaceUtils.replaceMap(context.getContext(), config.getUrlTemplate());
            LinkNode linkNode = new LinkNode(loginUrl);
            // // add json headers
            if (StringUtils.isNotEmpty(jsonHeader)) {
                Map<String, String> headersMap = (Map<String, String>) GsonUtils.fromJson(jsonHeader, Map.class);
                linkNode.addHeaders(headersMap);
            }
            // get retainQuote
            boolean retainQuote = context.getCookieConf() != null ? context.getCookieConf().getRetainQuote() : false;

            ProtocolOutput out = this.getProtocolOutput(linkNode, context);

            if (out != null && out.getContent() != null) {
                Metadata metadata = out.getContent().getMetadata();
                String[] vals = metadata.getValues(HTTPConstants.HTTP_HEADER_SET_COOKIE);

                return CookieFormater.INSTANCE.parserCookie(vals, retainQuote);
            }
        } catch (Exception ex) {
            logger.error("LoginUtil doLogin throw exception, error message:[" + ex.getMessage() + "]");
        }
        return "";
    }

    private ProtocolOutput getProtocolOutput(LinkNode linkNode, SearchProcessorContext context) {
        try {
            Request request = new Request();
            RequestUtil.setProcessorContext(request, context);
            RequestUtil.setConf(request, PropertiesConfiguration.getInstance());
            Response response = new Response();
            RequestUtil.setCurrentUrl(request, linkNode);
            ServiceBase serviceProcessor = ProcessorFactory.getService(null);
            serviceProcessor.invoke(request, response);
            return ResponseUtil.getProtocolResponse(response);
        } catch (Exception e) {
            logger.error("execute request error! " + e.getMessage(), e);
        }
        return null;
    }

    /**
     * do login action with the given cookie.
     * <p>
     * Note: The cookie string is contained in {@link SearchProcessorContext#getContext()}
     * </p>
     * @param config  the login config
     * @param context the search context
     * @return true if it is login successfully, otherwise false.
     */
    @SuppressWarnings("unchecked")
    public boolean doLoginByCookies(LoginConfig config, SearchProcessorContext context) throws ResultEmptyException {
        try {
            LoginCheckConfig loginCheckConfig = config.getLoginCheckConfig();

            String checkUrl = ReplaceUtils.replaceMap(context.getContext(), loginCheckConfig.getCheckUrl());
            logger.info("do login check, url:" + checkUrl);
            LinkNode linkNode = new LinkNode(checkUrl);

            // // add json headers
            String jsonHeader = loginCheckConfig.getHeaders();
            if (StringUtils.isNotEmpty(jsonHeader)) {
                Map<String, String> headersMap = (Map<String, String>) GsonUtils.fromJson(jsonHeader, Map.class);
                linkNode.addHeaders(headersMap);
            }

            ProtocolOutput out = this.getProtocolOutput(linkNode, context);

            return checkLoginResult(out, loginCheckConfig, context);
        } catch (ResultEmptyException ex) {
            throw ex;
        } catch (Exception ex) {
            logger.error("LoginUtil doLoginByCookies throw exception, error message:[" + ex.getMessage() + "]", ex);
        }
        return false;
    }

    private boolean checkLoginResult(ProtocolOutput out, LoginCheckConfig loginCheckConfig, SearchProcessorContext context) throws ResultEmptyException {
        if (out != null && out.getContent() != null) {
            String responseBody = out.getContent().getContentAsString();

            if (isFailure(responseBody, loginCheckConfig.getFailPattern())) {
                return false;
            }

            String successPattern = ReplaceUtils.replaceMap(context.getContext(), loginCheckConfig.getSuccessPattern());
            // match by page content or cookieString
            if (isSuccess(responseBody, context, successPattern)) {
                callSegments(loginCheckConfig.getSegmentList(), context, responseBody);

                return true;
            }
        }
        return false;
    }

    private void callSegments(List<AbstractSegment> segments, SearchProcessorContext context, String responseBody) throws ResultEmptyException {
        if (CollectionUtils.isNotEmpty(segments)) {
            Request request = new Request();
            RequestUtil.setProcessorContext(request, context);

            // decode
            responseBody = DecodeUtil.decodeContent(responseBody, request);
            request.setInput(responseBody);
            for (AbstractSegment abstractSegment : segments) {
                try {
                    Response segResponse = Response.build();
                    SegmentBase segmentBase = ProcessorFactory.getSegment(abstractSegment);
                    segmentBase.invoke(request, segResponse);
                } catch (Exception e) {
                    if (e instanceof ResultEmptyException) {
                        throw (ResultEmptyException) e;
                    }
                    logger.error("invoke segment processor error!", e);
                }
            }
        }
    }

    private boolean isSuccess(String responseBody, SearchProcessorContext context, String successPattern) {
        if (successPattern != null) {
            if (PatternUtils.match(successPattern, responseBody)) {
                return true;
            }

            String cookie = ProcessorContextUtil.getCookieString(context);
            if (cookie != null && PatternUtils.match(successPattern, cookie)) {
                return true;
            }
        }

        return false;
    }

    private boolean isFailure(String responseBody, String failPattern) {
        return failPattern != null && PatternUtils.match(failPattern, responseBody);
    }

}