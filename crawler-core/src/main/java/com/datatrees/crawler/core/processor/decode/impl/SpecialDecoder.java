/**
 * This document and its contents are protected by copyright 2015 and owned by datatrees.com Inc. The
 * copying and reproduction of this document and/or its content (whether wholly or partly) or any
 * incorporation of the same into any other material in any media or format of any kind is strictly
 * prohibited. All rights are reserved.
 * 
 * Copyright (c) datatrees.com Inc. 2015
 */
package com.datatrees.crawler.core.processor.decode.impl;

import java.nio.charset.Charset;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.datatrees.crawler.core.processor.decode.AbstractDecoder;

/**
 * 
 * @author <A HREF="mailto:wangcheng@datatrees.com.cn">Cheng Wang</A>
 * @version 1.0
 * @since Mar 15, 2014 11:30:55 AM
 */
public class SpecialDecoder extends AbstractDecoder {

    private static final Logger log = LoggerFactory.getLogger(SpecialDecoder.class);

    @Override
    public String decode(String content, Charset charset) {
        if (charset == null) {
            charset = Charset.defaultCharset();
            log.warn("using default charset! " + charset);
        }
        return decodeUnicode(content,charset);
    }
    
    public String decodeUnicode(String content,Charset charset) {
        StringBuilder sb = new StringBuilder();
        int i = -1;
        int pos = 0;

        while ((i = content.indexOf("\\u", pos)) != -1) {
            sb.append(content.substring(pos, i));
            if (i + 5 < content.length()) {
                pos = i + 6;
                sb.append((char) Integer.parseInt(content.substring(i + 2, i + 6), 16));
            }
        }
        if(sb.length() == 0){
            sb.append(content);
        }
        return new String(sb.toString().getBytes(),charset);
    }
}
