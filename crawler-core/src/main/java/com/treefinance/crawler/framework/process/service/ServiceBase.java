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

package com.treefinance.crawler.framework.process.service;

import com.treefinance.crawler.framework.config.xml.service.AbstractService;
import com.treefinance.crawler.framework.context.RequestUtil;
import com.treefinance.crawler.framework.context.function.LinkNode;
import com.treefinance.crawler.framework.context.function.SpiderRequest;
import com.treefinance.crawler.framework.context.function.SpiderResponse;
import com.treefinance.crawler.framework.context.pipeline.ProcessorInvokerAdapter;
import com.treefinance.crawler.framework.util.UrlExtractor;
import com.treefinance.toolkit.util.RegExp;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import javax.annotation.Nonnull;

import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * @author <A HREF="">Cheng Wang</A>
 * @version 1.0
 * @since Mar 7, 2014 7:43:14 PM
 */
public abstract class ServiceBase<S extends AbstractService> extends ProcessorInvokerAdapter {

    protected final S service;

    public ServiceBase() {
        this.service = null;
    }

    public ServiceBase(@Nonnull S service) {
        this.service = Objects.requireNonNull(service);
    }

    public S getService() {
        return service;
    }

    // resolve base url
    @Override
    protected void postProcess(@Nonnull SpiderRequest request, @Nonnull SpiderResponse response) throws Exception {
        LinkNode current = RequestUtil.getCurrentUrl(request);
        if (current != null) {
            String content = RequestUtil.getContent(request);
            if (StringUtils.isNotEmpty(content)) {
                String baseContent = RegExp.group(content, "<base(.*)>", Pattern.CASE_INSENSITIVE, 1);
                String baseDomainUrl = null;
                if (StringUtils.isNotEmpty(baseContent)) {
                    List<String> urlsInText = UrlExtractor.extract(baseContent);
                    if (CollectionUtils.isNotEmpty(urlsInText)) {
                        baseDomainUrl = urlsInText.get(0);
                    }
                }
                if (StringUtils.isEmpty(baseDomainUrl)) {
                    if (StringUtils.isNotEmpty(current.getRedirectUrl())) {
                        baseDomainUrl = current.getRedirectUrl();
                    } else {
                        baseDomainUrl = current.getUrl();
                    }
                }
                current.setBaseUrl(baseDomainUrl);
                logger.debug("originUrl: {}, baseDomainUrl: {}", current.getUrl(), baseDomainUrl);
            }
        }
    }

}
