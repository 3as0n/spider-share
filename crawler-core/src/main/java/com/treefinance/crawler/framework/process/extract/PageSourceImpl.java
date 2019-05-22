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

package com.treefinance.crawler.framework.process.extract;

import com.treefinance.crawler.framework.config.xml.extractor.PageSource;
import com.treefinance.crawler.framework.config.xml.page.Regexp;
import com.treefinance.crawler.framework.config.xml.page.Replacement;
import com.treefinance.crawler.framework.config.xml.plugin.AbstractPlugin;
import com.treefinance.crawler.framework.context.AbstractProcessorContext;
import com.treefinance.crawler.framework.context.function.SpiderRequest;
import com.treefinance.crawler.framework.context.function.SpiderResponse;
import com.treefinance.crawler.framework.context.pipeline.ProcessorInvokerAdapter;
import com.treefinance.crawler.framework.download.WrappedFile;
import com.treefinance.crawler.framework.extension.plugin.PluginCaller;
import com.treefinance.crawler.framework.extension.plugin.PluginConstants;
import com.treefinance.crawler.framework.process.PageHelper;
import com.treefinance.crawler.framework.util.FieldUtils;
import com.treefinance.toolkit.util.Preconditions;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nonnull;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author <A HREF="">Cheng Wang</A>
 * @version 1.0
 * @since 2015年7月14日 下午10:23:47
 */
public class PageSourceImpl extends ProcessorInvokerAdapter {

    private final List<PageSource> pageSources;

    public PageSourceImpl(@Nonnull List<PageSource> pageSources) {
        if (CollectionUtils.isEmpty(pageSources)) {
            throw new IllegalArgumentException("page-source must not be empty!");
        }
        this.pageSources = pageSources;
    }

    @Override
    public void process(@Nonnull SpiderRequest request, @Nonnull SpiderResponse response) throws Exception {
        Object input = request.getInput();
        Preconditions.notNull("input", input);

        StringBuilder builder = new StringBuilder();
        for (PageSource source : pageSources) {
            logger.debug("Search page source : {}", source);

            String result = getPageContent(source, input, request);

            List<Replacement> replacements = source.getReplacements();
            if (CollectionUtils.isNotEmpty(replacements)) {
                result = PageHelper.replaceText(result, replacements);
            }

            Regexp regexp = source.getRegexp();
            if (regexp != null) {
                result = PageHelper.getTextByRegexp(result, regexp);
            }

            logger.debug("Actual page source content: {}", result);

            // TODO: 2018/7/27 不清楚为什么要设置pageSource放在context中
            // set source field to context
            request.addContextScope(source.getField(), result);

            builder.append(result);
        }

        String content = builder.toString();
        logger.debug("Actual extracting content: {}", content);

        response.setOutPut(content);
    }

    @SuppressWarnings("unchecked")
    private String getPageContent(PageSource source, Object input, SpiderRequest request) {
        String separator = StringUtils.defaultString(source.getSeparator());
        AbstractPlugin plugin = source.getPlugin();

        logger.debug("Get page content. <<< source-ref: {}, separator: {}, plugin-ref: {}", source.getField(), separator, plugin != null ? plugin.getId() : null);

        if (plugin != null) {
            Object value = FieldUtils.getFieldValue(input, source.getField());
            if (value instanceof Collection) {
                Stream<String> stream = ((Collection)value).stream().map(obj -> this.getSourceContent(obj, plugin, request));

                return stream.collect(Collectors.joining(separator));
            }

            return this.getSourceContent(value, plugin, request);
        }

        return FieldUtils.getFieldValueAsString(input, source.getField(), separator);
    }

    private String getSourceContent(Object value, AbstractPlugin pluginDesc, SpiderRequest request) {
        if (value == null) {
            return StringUtils.EMPTY;
        }

        AbstractProcessorContext context = request.getProcessorContext();
        String content = (String)PluginCaller.call(pluginDesc, context, () -> {
            Map<String, String> params = new HashMap<>();
            if (value instanceof WrappedFile) {
                WrappedFile file = (WrappedFile)value;
                file.download();// download attachment to local
                params.put(PluginConstants.FILE_WAPPER_PATH, file.getAbsolutePath());
                params.put(PluginConstants.FILE_MIME_TYPE, file.getMimeType());
                params.put(PluginConstants.FILE_NAME, file.getName());
                params.put(PluginConstants.FILE_SOURCE_URL, file.getSourceURL());
            } else if (value instanceof String) {
                params.put(PluginConstants.FILE_CONTENT, (String)value);
            } else {
                params.put(PluginConstants.FILE_CONTENT, value.toString());
                logger.warn("Process page source by plugin with unexpected input type. input: {}, type: {}", value, value.getClass());
            }

            return params;
        });

        logger.debug("Page-source content : {}", content);

        return content;
    }
}
