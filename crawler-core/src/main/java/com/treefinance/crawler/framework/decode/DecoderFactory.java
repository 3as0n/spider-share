/*
 * Copyright © 2015 - 2017 杭州大树网络技术有限公司. All Rights Reserved
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

package com.treefinance.crawler.framework.decode;

import com.datatrees.crawler.core.domain.config.properties.UnicodeMode;
import com.treefinance.crawler.framework.decode.impl.*;

/**
 * @author <A HREF="">Cheng Wang</A>
 * @version 1.0
 * @since Mar 12, 2014 8:03:50 PM
 */
public final class DecoderFactory {

    private DecoderFactory() {}

    public static Decoder getDecoder(UnicodeMode unicodeMode) {
        UnicodeMode mode = unicodeMode;
        if (mode == null) {
            mode = UnicodeMode.DEFAULT;
        }

        Decoder decoder;
        switch (mode) {
            case COMPLEX:
                decoder = ComplexDecoder.DEFAULT;
                break;
            case HEX:
                decoder = HexDecoder.DEFAULT;
                break;
            case STANDARD:
                decoder = StandardDecoder.DEFAULT;
                break;
            case SPECIAL:
                decoder = SpecialDecoder.DEFAULT;
                break;
            default:
                decoder = BasicDecoder.DEFAULT;
                break;
        }

        return decoder;
    }

}
