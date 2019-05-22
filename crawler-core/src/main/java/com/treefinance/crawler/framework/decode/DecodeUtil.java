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

package com.treefinance.crawler.framework.decode;

import com.treefinance.crawler.framework.context.AbstractProcessorContext;
import com.treefinance.crawler.framework.context.RequestUtil;
import com.treefinance.crawler.framework.context.SearchProcessorContext;
import com.treefinance.crawler.framework.context.function.SpiderRequest;

/**
 * @author <A HREF="">Cheng Wang</A>
 * @version 1.0
 * @since 2015年10月21日 下午5:57:10
 */
public final class DecodeUtil {

    private DecodeUtil() {}

    public static String decodeContent(String content, SpiderRequest request) {
        String result = content;

        AbstractProcessorContext context = request.getProcessorContext();
        if (context instanceof SearchProcessorContext) {
            Decoder decoder = ((SearchProcessorContext)context).getUnicodeDecoder();
            if (decoder != null) {
                String charset = RequestUtil.getContentCharset(request);
                result = decoder.decode(content, charset);
                RequestUtil.setContent(request, result);
            }
        }

        return result;
    }

}
