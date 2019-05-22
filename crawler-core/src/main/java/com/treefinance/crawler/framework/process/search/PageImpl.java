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

package com.treefinance.crawler.framework.process.search;

import com.datatrees.common.conf.Configuration;
import com.datatrees.common.conf.PropertiesConfiguration;
import com.datatrees.common.util.GsonUtils;
import com.datatrees.common.util.URLUtil;
import com.google.common.base.Preconditions;
import com.treefinance.crawler.framework.config.enums.url.FilterType;
import com.treefinance.crawler.framework.config.xml.SearchConfig;
import com.treefinance.crawler.framework.config.xml.filter.UrlFilter;
import com.treefinance.crawler.framework.config.xml.page.Page;
import com.treefinance.crawler.framework.config.xml.page.Regexp;
import com.treefinance.crawler.framework.config.xml.page.Replacement;
import com.treefinance.crawler.framework.config.xml.search.SearchTemplateConfig;
import com.treefinance.crawler.framework.config.xml.segment.AbstractSegment;
import com.treefinance.crawler.framework.consts.Constants;
import com.treefinance.crawler.framework.consts.Status;
import com.treefinance.crawler.framework.context.RequestUtil;
import com.treefinance.crawler.framework.context.ResponseUtil;
import com.treefinance.crawler.framework.context.SearchProcessorContext;
import com.treefinance.crawler.framework.context.function.LinkNode;
import com.treefinance.crawler.framework.context.function.SpiderRequest;
import com.treefinance.crawler.framework.context.function.SpiderResponse;
import com.treefinance.crawler.framework.context.function.SpiderResponseFactory;
import com.treefinance.crawler.framework.context.pipeline.ProcessorInvokerAdapter;
import com.treefinance.crawler.framework.decode.DecodeUtil;
import com.treefinance.crawler.framework.exception.ResultEmptyException;
import com.treefinance.crawler.framework.parser.HTMLParser;
import com.treefinance.crawler.framework.process.PageHelper;
import com.treefinance.crawler.framework.process.ProcessorFactory;
import com.treefinance.crawler.framework.process.SpiderRequestHelper;
import com.treefinance.crawler.framework.process.SpiderResponseHelper;
import com.treefinance.crawler.framework.process.domain.ClassifiedExtractResult;
import com.treefinance.crawler.framework.process.domain.PageExtractObject;
import com.treefinance.crawler.framework.process.segment.SegmentBase;
import com.treefinance.crawler.framework.util.URLSplitter;
import com.treefinance.crawler.framework.util.UrlExtractor;
import com.treefinance.toolkit.util.RegExp;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nonnull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author <A HREF="">Cheng Wang</A>
 * @version 1.0
 * @since Feb 24, 2014 6:35:43 PM
 */
public class PageImpl extends ProcessorInvokerAdapter {

    private static final String titleRegex = PropertiesConfiguration.getInstance().get("page.title.regex", "<title>([^<]*)</title>");
    private static final int URL_MAX_LENGTH = PropertiesConfiguration.getInstance().getInt("url.max.length", 1024);
    private static final int TITLE_LENGTH_LIMIT = 2048;
    private final Page page;

    public PageImpl(@Nonnull Page page) {
        this.page = Objects.requireNonNull(page);
    }

    public Page getPage() {
        return page;
    }

    @Override
    public void process(@Nonnull SpiderRequest request, @Nonnull SpiderResponse response) throws Exception {
        LinkNode current = RequestUtil.getCurrentUrl(request);
        Preconditions.checkNotNull(current, "source url should not be null!");

        String pageContent = RequestUtil.getContent(request);
        pageContent = DecodeUtil.decodeContent(pageContent, request);

        checkContent(request, response);

        if (skipParsing(response)) {
            logger.warn("Skip page processing with the incorrect response status. - LinkNode: {}", current);
            return;
        }

        String content = resolveContent(pageContent);
        if (StringUtils.isEmpty(content)) {
            logger.warn("Empty page content after content resolving! - LinkNode: {}", current);
            return;
        }

        resolveCurrentUrl(current, content);

        Map<String, LinkNode> urlLists;
        List<AbstractSegment> segments = page.getSegmentList();
        if (CollectionUtils.isNotEmpty(segments)) {
            // extract url with segment
            urlLists = extractObjectsWithSegments(content, segments, request, response);
        } else {
            // extract url
            urlLists = new HashMap<>();

            extractUrls(content, current, urlLists);
        }

        findAndAddPageUrls(pageContent, current, request, urlLists);// detect

        // add redirect url
        addRedirectUrl(current, urlLists);

        // filter url set url depth
        List<LinkNode> urlLinkNodes = filterUrls(urlLists, current, request);

        ResponseUtil.setResponseLinkNodes(response, urlLinkNodes);
    }

    private boolean skipParsing(SpiderResponse response) {
        return response.getStatus() == Status.NO_SEARCH_RESULT;
    }

    /**
     * using white and black list to filter web url adjust depth
     */
    private List<LinkNode> filterUrls(Map<String, LinkNode> urlLists, LinkNode current, SpiderRequest request) {
        List<LinkNode> nodes = new ArrayList<>();
        if (MapUtils.isNotEmpty(urlLists)) {
            SearchProcessorContext processorContext = (SearchProcessorContext)request.getProcessorContext();

            String templateId = RequestUtil.getCurrentTemplateId(request);
            SearchTemplateConfig searchTemplateConfig = processorContext.getSearchTemplateConfig(templateId);
            String revisitPattern = null;
            if (searchTemplateConfig != null && searchTemplateConfig.getRequest() != null && StringUtils.isNotBlank(searchTemplateConfig.getRequest().getReVisitPattern())) {
                revisitPattern = searchTemplateConfig.getRequest().getReVisitPattern();
            }

            UrlFilterHandler urlFilterHandler = createUrlFilterHandler(processorContext, request.getConfiguration());

            URLHandler urlHandler = RequestUtil.getURLHandler(request);

            for (Entry<String, LinkNode> entry : urlLists.entrySet()) {
                String originalURL = entry.getKey();
                LinkNode node = entry.getValue();
                Collection<String> urls = URLSplitter.split(originalURL);
                for (String url : urls) {
                    LinkNode tmp;
                    if (urls.size() > 1) {
                        tmp = GsonUtils.fromJson(GsonUtils.toJson(node), LinkNode.class);
                    } else {
                        tmp = node;
                    }

                    tmp.setUrl(url);
                    tmp.setPageTitle(current.getPageTitle());

                    logger.debug("filter url : {}", url);

                    // run url handler
                    boolean removed = false;
                    if (urlHandler != null) {
                        try {
                            removed = urlHandler.handle(current, tmp);
                            // return true/false remove node
                        } catch (Exception e) {
                            logger.error("invoke url handler exception!", e);
                        }
                    }

                    if (!removed) {
                        if (tmp.isHosting() || !urlFilterHandler.filter(url)) {
                            if (StringUtils.isEmpty(tmp.getReferer())) {
                                String referer = StringUtils.isNotEmpty(current.getRedirectUrl()) ? current.getRedirectUrl() : current.getUrl();
                                tmp.setReferer(referer);
                            }
                            int depth = current.getDepth();
                            if (revisitPattern != null) {
                                try {
                                    depth = RegExp.find(url, revisitPattern) ? depth - 1 : depth;
                                } catch (Exception e) {
                                    logger.error("check revisit error!", e);
                                }
                            }

                            // adjust depth
                            processorContext.adjustUrlDepth(tmp, templateId, depth);

                            nodes.add(tmp);
                            logger.debug("{} @@accept url: {}", current.getUrl(), url);
                        } else {
                            logger.debug("{} @@filter url: {}", current.getUrl(), url);
                        }
                    }

                    /*
                     * if (StringUtils.isNotEmpty(dest) && !removed) { String referer =
                     * StringUtils.isNotEmpty(current.getRedirectUrl()) ? current.getRedirectUrl() :
                     * current.getUrl(); if (StringUtils.isEmpty(tmp.getReferer())) {
                     * tmp.setReferer(referer); } int depth = current.getDepth(); if (revisitPattern
                     * != null) { try { depth = PatternUtils.match(revisitPattern, url) ? depth - 1
                     * : depth; } catch (Exception e) { log.error("check revisit error!", e); } }
                     *
                     * // adjust depth wrapper.adjustUrlDepth(tmp, templateId, depth); nodes.add(tmp);
                     * log.debug(current.getUrl() + "@@accept url:" + url); } else {
                     * log.debug(current.getUrl() + "@@filter url:" + url); }
                     */
                }
            }
        }

        logger.debug("After url filter... original={} @@after={}", urlLists.size(), nodes.size());
        // nodes.add(current);
        return nodes;
    }

    private UrlFilterHandler createUrlFilterHandler(@Nonnull SearchProcessorContext processorContext, Configuration conf) {
        SearchConfig config = processorContext.getSearchConfig();
        List<UrlFilter> urlFilterList = config.getUrlFilterList();

        List<UrlFilter> urlFilters = new ArrayList<>(urlFilterList);

        String blackList = Constants.URL_BLACK_LIST;
        if (conf != null) {
            blackList = conf.get("url.blacklist", blackList);
        }

        UrlFilter filter = new UrlFilter();
        filter.setFilter(blackList);
        filter.setType(FilterType.BLACK_LIST.getValue());
        urlFilters.add(filter);

        // add default domain white filter
        String domain = processorContext.getWebsite().getWebsiteDomain();
        if (StringUtils.isNotEmpty(domain)) {
            filter = new UrlFilter();
            filter.setFilter(domain.toLowerCase());
            filter.setType(FilterType.WHITE_LIST.getValue());
            urlFilters.add(filter);
        }

        return new RegexUrlFilterHandler(urlFilters);
    }

    private void addRedirectUrl(LinkNode current, Map<String, LinkNode> urlLists) {
        if (BooleanUtils.isTrue(page.getRedirectUrlAdd()) && StringUtils.isNotBlank(current.getRedirectUrl())) {
            LinkNode redirectNode = new LinkNode(current.getRedirectUrl());
            redirectNode.setReferer(current.getUrl());
            redirectNode.setDepth(current.getDepth());
            urlLists.put(redirectNode.getUrl(), redirectNode);
            logger.info("add redirect url to urlLists, url: {}, referer: {}", current.getRedirectUrl(), current.getReferer());
        }
    }

    private void findAndAddPageUrls(String content, LinkNode current, SpiderRequest request, @Nonnull Map<String, LinkNode> urlLists) {
        try {
            String contentRegex = page.getContentPageRegex();
            String searchTemplate;
            if (StringUtils.isNotEmpty(contentRegex) && StringUtils.isNotEmpty(searchTemplate = RequestUtil.getSearchTemplate(request)) && StringUtils.isNotEmpty(content)) {
                Matcher matcher = RegExp.getMatcher(contentRegex, content);
                if (matcher.find()) {
                    String keyword = RequestUtil.getKeyWord(request);
                    String charset = RequestUtil.getContentCharset(request);
                    do {
                        String pNumber = matcher.group(1);
                        if (StringUtils.isNotEmpty(pNumber)) {
                            try {
                                int pNum = Integer.parseInt(pNumber);
                                logger.info("add paging number: {},  match-text: {}", pNum, matcher.group());
                                String pageUrl = SearchTemplateCombine.constructSearchURL(searchTemplate, keyword, charset, pNum, false, request.getGlobalScopeAsMap());
                                if (StringUtils.isNotEmpty(pageUrl)) {
                                    logger.info("add page url: {}", pageUrl);
                                    LinkNode tmp = new LinkNode(pageUrl).setReferer(current.getUrl());
                                    tmp.setpNum(pNum);
                                    urlLists.put(pageUrl, tmp);
                                }

                            } catch (Exception e) {
                                logger.warn(e.getMessage());
                            }
                        }
                    } while (matcher.find());
                }
            }
        } catch (Exception e) {
            logger.error("extract page urls error!", e);
        }
    }

    /**
     * get url list need too steps first extract field urls second extract page by regex finally resolve url by base url
     */
    @Nonnull
    private Map<String, LinkNode> extractObjectsWithSegments(String pageContent, List<AbstractSegment> segments, SpiderRequest request, SpiderResponse response)
        throws ResultEmptyException {
        StringBuilder mergedContent = null;

        if (BooleanUtils.isTrue(page.getUrlExtract())) {
            mergedContent = new StringBuilder();
            SpiderRequestHelper.setKeepSegmentProcessingData(request, true);
        }

        PageExtractObject extractObject = new PageExtractObject();
        for (AbstractSegment segment : segments) {
            if (BooleanUtils.isTrue(segment.getStandBy()) && extractObject.isValid(segment.getName())) {
                logger.info("Skip segment processor with flag 'stand-by'. segment: {}", segment);
                continue;
            }

            try {
                request.setInput(pageContent);

                SpiderResponse segResponse = SpiderResponseFactory.make();

                SegmentBase segmentBase = ProcessorFactory.getSegment(segment);
                segmentBase.invoke(request, segResponse);

                Object segResult = segResponse.getOutPut();

                extractObject.setFieldExtractValue(segment.getName(), segResult);

                if (mergedContent != null) {
                    List<String> contents = SpiderResponseHelper.getSegmentProcessingData(segResponse);
                    // append content split to detect url
                    if (CollectionUtils.isNotEmpty(contents)) {
                        for (String split : contents) {
                            mergedContent.append(split);
                        }
                    }
                }
            } catch (ResultEmptyException e) {
                throw e;
            } catch (Exception e) {
                logger.error("invoke segment processor error!", e);
            } finally {
                request.clear();
            }
        }

        ClassifiedExtractResult extractResult = new ClassifiedExtractResult(extractObject);

        Map<String, LinkNode> linkNodes = extractResult.getLinkNodes();
        List<Object> instanceList = extractResult.getSegments();

        if (mergedContent != null && mergedContent.length() > 0) {
            String content = mergedContent.toString();
            logger.debug("after segment page content: {}", content);

            extractUrls(content, RequestUtil.getCurrentUrl(request), linkNodes);
        }

        // return the page objects
        ResponseUtil.setResponseObjectList(response, instanceList);
        // return all link-nodes in current page
        return linkNodes;
    }

    private void extractUrls(String content, LinkNode current, Map<String, LinkNode> linkNodeMap) {
        String currentUrl = current.getUrl();

        // extract urls by html parser
        logger.debug("start to extract html links: {}", currentUrl);
        HTMLParser htmlParser = new HTMLParser();
        htmlParser.parse(content, StringUtils.isNotEmpty(current.getBaseUrl()) ? current.getBaseUrl() : currentUrl);
        for (Entry<String, String> fl : htmlParser.getLinks().entrySet()) {
            String key = fl.getKey();
            logger.debug(" extractor url >> {}", key);
            linkNodeMap.computeIfAbsent(key, s -> new LinkNode(s).setReferer(currentUrl));
        }

        // extract urls by custom url extractor
        logger.debug("start to extract text urls: {}", currentUrl);
        List<String> textUrls = UrlExtractor.extract(content);
        for (String nextURL : textUrls) {
            try {
                String url = URLUtil.urlFormat(nextURL.trim());
                if (StringUtils.isEmpty(url) || url.length() > URL_MAX_LENGTH) {
                    continue;
                }

                logger.debug("new url extracted in text extractor: {}", url);
                linkNodeMap.computeIfAbsent(url, s -> new LinkNode(url).setReferer(currentUrl));
            } catch (Exception e) {
                logger.error("url format error...", e);
            }
        }
    }

    private void resolveCurrentUrl(LinkNode current, String content) {
        String title = RegExp.group(content, titleRegex, Pattern.CASE_INSENSITIVE, 1).trim();
        if (StringUtils.isNotEmpty(title) && title.length() > TITLE_LENGTH_LIMIT) {
            title = title.substring(0, TITLE_LENGTH_LIMIT);
        }

        String pageTitle = StringUtils.isNotEmpty(title) ? title : current.getPageTitle();
        if (StringUtils.isNotEmpty(pageTitle) && StringUtils.isNotBlank(page.getPageTitleRegex())) {
            pageTitle = RegExp.group(pageTitle, page.getPageTitleRegex(), Pattern.CASE_INSENSITIVE, 1);
        }
        current.setPageTitle(pageTitle);

        try {
            String pageRegex = page.getPageNumRegex();
            if (StringUtils.isNotEmpty(pageRegex)) {
                String pidS = RegExp.group(current.getUrl(), pageRegex, 1);

                if (StringUtils.isNotEmpty(pidS)) {
                    int pNum = Integer.parseInt(pidS);

                    logger.info("find page number from url: {}, num: {}", current.getUrl(), pNum);
                    current.setpNum(pNum);
                }
            }
        } catch (Exception e) {
            logger.error("set page num error!", e);
        }
    }

    private String resolveContent(String pageContent) {
        String content = pageContent;
        List<Replacement> replacements = page.getReplacementList();
        if (CollectionUtils.isNotEmpty(replacements)) {
            content = PageHelper.replaceText(content, replacements);
        }

        Regexp regexp = page.getRegexp();
        if (regexp != null) {
            content = PageHelper.getTextByRegexp(content, regexp);
        }

        return content;
    }

    private void checkContent(SpiderRequest request, SpiderResponse response) {
        SearchTemplateConfig templateConfig = SpiderRequestHelper.getTemplateConfig(request);
        if (templateConfig != null) {
            com.treefinance.crawler.framework.config.xml.search.Request requestConfig = templateConfig.getRequest();
            if (requestConfig != null) {
                String content = RequestUtil.getContent(request);

                if (resetResponseStatusWithPattern(content, requestConfig.getBlockPattern(), Status.BLOCKED, response)) {
                    return;
                }

                if (resetResponseStatusWithPattern(content, requestConfig.getNoResultPattern(), Status.NO_SEARCH_RESULT, response)) {
                    return;
                }

                resetResponseStatusWithPattern(content, requestConfig.getLastPagePattern(), Status.LAST_PAGE, response);
            }
        }
    }

    private boolean resetResponseStatusWithPattern(String content, String pattern, int status, SpiderResponse response) {
        if (StringUtils.isNotEmpty(pattern) && RegExp.find(content, pattern)) {
            logger.info("set status: {}", Status.format(status));
            response.setStatus(status);
            return true;
        }
        return false;
    }

}
