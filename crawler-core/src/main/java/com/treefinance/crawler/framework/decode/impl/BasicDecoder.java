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

package com.treefinance.crawler.framework.decode.impl;

import java.nio.charset.Charset;

import com.treefinance.crawler.framework.decode.Decoder;
import org.apache.commons.lang.StringEscapeUtils;

/**
 * @author <A HREF="">Cheng Wang</A>
 * @version 1.0
 * @since Mar 12, 2014 8:22:14 PM
 */
public class BasicDecoder implements Decoder {

    public static final BasicDecoder DEFAULT = new BasicDecoder();

    @Override
    public String decode(String content, Charset charset) {
        return StringEscapeUtils.unescapeHtml(content);
    }

}