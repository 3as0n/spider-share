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

package com.treefinance.crawler.framework.boot;

import com.treefinance.crawler.framework.config.xml.page.Page;
import com.treefinance.crawler.framework.config.xml.service.AbstractService;
import com.treefinance.crawler.framework.consts.Status;
import com.treefinance.crawler.framework.context.RequestUtil;
import com.treefinance.crawler.framework.context.SearchProcessorContext;
import com.treefinance.crawler.framework.context.function.CrawlRequest;
import com.treefinance.crawler.framework.context.function.CrawlResponse;
import com.treefinance.crawler.framework.context.function.LinkNode;
import com.treefinance.crawler.framework.exception.ResponseCheckException;
import com.treefinance.crawler.framework.exception.ResultEmptyException;
import com.treefinance.crawler.framework.process.ProcessorFactory;
import com.treefinance.crawler.framework.process.search.PageImpl;
import com.treefinance.crawler.framework.process.service.ServiceBase;
import com.treefinance.crawler.framework.protocol.ProtocolStatusCodes;
import com.treefinance.toolkit.util.Preconditions;
import com.treefinance.toolkit.util.RegExp;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;

/**
 * @author <A HREF="">Cheng Wang</A>
 * @version 1.0
 * @since Mar 3, 2014 8:48:55 PM
 */
public class Crawler {

    private static final Logger LOGGER = LoggerFactory.getLogger(Crawler.class);

    /**
     * crawler request main route step 1: request via httpclient step 2: parse page content
     * 
     * @param request
     * @return
     * @exception ResultEmptyException
     */
    public static CrawlResponse crawl(@Nonnull CrawlRequest request) throws ResultEmptyException {
        Preconditions.notNull("request", request);
        LOGGER.info("request handling ... {}", request);

        CrawlResponse response = CrawlResponse.build();
        try {

            SearchProcessorContext context = (SearchProcessorContext)request.getProcessorContext();
            String templateId = request.getSearchTemplateId();
            LinkNode url = request.getUrl();

            Page page = context.getPageDefinition(url, templateId);
            if (page != null) {
                RequestUtil.setCurrentPage(request, page);

                AbstractService service = page.getService();
                if (null == service) {
                    service = context.getDefaultService();
                }

                ServiceBase serviceProcessor = ProcessorFactory.getService(service);
                try {
                    // fetch page content
                    serviceProcessor.invoke(request, response);

                    // check the page response failed
                    String content = (String)response.getOutPut();
                    if (BooleanUtils.isTrue(page.getResponseCheck())
                        && (StringUtils.isBlank(content) || StringUtils.isNotBlank(page.getPageFailedPattern()) && RegExp.find(content, page.getPageFailedPattern()))) {
                        throw new ResponseCheckException("page:" + page.getId() + ",url:" + url.getUrl() + " response check failed contains " + page.getPageFailedPattern());
                    }
                } catch (ResponseCheckException e) {
                    throw e;
                } catch (Exception e) {
                    // response code failed
                    if (BooleanUtils.isTrue(page.getResponseCheck()) && RegExp.find(Integer.toString(response.getStatus()), getFailurePattern(page))) {
                        throw new ResponseCheckException("page:" + page.getId() + ",url:" + request.getUrl() + " response check failed!", e);
                    } else {
                        throw e;
                    }
                }

                // reset response
                response.clear();

                if (url.isNeedRequeue()) {
                    LOGGER.info("need requeue linkNode: {}", url);
                } else {
                    // parser
                    PageImpl pageProcessor = new PageImpl(page);
                    pageProcessor.invoke(request, response);
                }
            } else {
                response.setStatus(Status.FILTERED);
                LOGGER.info("no available page found for linkNode: {}, template: {},set filtered.", url, templateId);
            }

        } catch (ResultEmptyException e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Error crawling url : {}", request.getUrl(), e);
            response.setStatus(Status.PROCESS_EXCEPTION);
            response.setException(e);
        }

        return response;
    }

    private static String getFailurePattern(Page page) {
        return StringUtils.defaultString(page.getFailedCodePattern(), "^(" + ProtocolStatusCodes.EXCEPTION + "|" + ProtocolStatusCodes.SERVER_EXCEPTION + ")$");
    }

}
