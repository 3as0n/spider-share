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

package com.treefinance.crawler.framework.protocol;

import com.datatrees.common.conf.PropertiesConfiguration;
import com.datatrees.common.util.PatternUtils;
import com.treefinance.crawler.framework.consts.Constants;
import com.treefinance.crawler.framework.protocol.metadata.Metadata;
import com.treefinance.crawler.framework.protocol.util.EncodingDetector;
import com.treefinance.crawler.framework.util.CharsetUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.regex.Pattern;

public class Content {

    static final Content NULL = new Null();
    private static final Pattern CHARSET_PATTERN = Pattern.compile(Constants.HEADER_CHARSET_PATTERN, Pattern.CASE_INSENSITIVE);
    private static Logger log = LoggerFactory.getLogger(Content.class);
    private String url;

    private String base;

    private byte[] content;

    private String contentType;

    private String mimeType;

    private String charSet;

    private Metadata metadata;

    private int responseCode;

    public Content() {
        metadata = new Metadata();
    }

    public Content(byte[] content, String contentType) {
        this(StringUtils.EMPTY, StringUtils.EMPTY, content, contentType, new Metadata());
    }

    public Content(String url, String base, byte[] content, String contentType, Metadata metadata) {
        if (url == null) {
            throw new IllegalArgumentException("null url");
        }
        if (base == null) {
            throw new IllegalArgumentException("null base");
        }
        if (content == null) {
            throw new IllegalArgumentException("null content");
        }
        if (metadata == null) {
            throw new IllegalArgumentException("null metadata");
        }

        this.url = url;
        this.base = base;
        this.content = content;
        this.metadata = metadata;
        this.contentType = contentType;
        resolveCharset(contentType, content);
    }

    public void resolveCharset(String ct, byte[] content) {
        if (StringUtils.isNotEmpty(ct)) {
            String[] strs = ct.split(";");
            if (strs.length > 0) {
                mimeType = strs[0].toLowerCase();
            }
            String charset = PatternUtils.group(ct, CHARSET_PATTERN, 1);
            if (StringUtils.isNotEmpty(charset)) {
                if (CharsetUtil.exist(charset)) {
                    log.debug("find page charset: [" + charset + "] url " + getUrl());
                    setCharSet(charset);
                } else {
                    log.warn("unsupport charset name: " + charset);
                }
            }
        }
        // only text has meta chartset
        if (StringUtils.isEmpty(getCharSet()) && StringUtils.isNotEmpty(ct) && ct.toLowerCase().startsWith("text/")) {
            String pageContent = null;
            String charset = null;
            if (content != null) {
                try {
                    pageContent = new String(content, CharsetUtil.DEFAULT);
                    charset = PatternUtils.group(pageContent, "<meta\\b[^>]*\\bcharset\\s*=\\s*['\"]?([-\\w.]+)", 1);
                    if (StringUtils.isNotEmpty(charset) && !CharsetUtil.exist(charset)) {
                        charset = null;
                    }
                    log.info("detect page charset: [" + charset + "] url " + getUrl());
                } catch (Exception e) {
                    log.error("....", e);
                }
            }
            if (StringUtils.isNotEmpty(charset)) {
                setCharSet(charset);
            }
        }

        if (StringUtils.isEmpty(getCharSet())) {
            log.debug("empty charset, set it to default " + CharsetUtil.DEFAULT);
            setCharSet(CharsetUtil.DEFAULT);
        }
    }

    //
    // Accessor methods
    //

    public String getCharSet() {
        return charSet;
    }

    public void setCharSet(String charSet) {
        this.charSet = charSet;
    }

    /** The url fetched. */
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * The base url for relative links contained in the content. Maybe be different from url if the request redirected.
     */
    public String getBaseUrl() {
        return base;
    }

    /** The binary content retrieved. */
    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public String getContentAsString() {
        String charset = getCharSet();
        if (StringUtils.isEmpty(getCharSet())) {
            charset = CharsetUtil.DEFAULT;
        }
        Charset cs = CharsetUtil.getCharset(charset);
        return new String(content, cs);
    }

    public String detectContentAsString() {
        EncodingDetector detector = new EncodingDetector(PropertiesConfiguration.getInstance());
        detector.autoDetectClues(this, true, true);
        String charset = detector.guessEncoding(this, CharsetUtil.DEFAULT, true);
        setCharSet(charset);
        log.info("detect content charset: [" + charset + "] url " + getUrl());
        return new String(content, CharsetUtil.getCharset(charset));
    }

    /**
     * @return the mimeType
     */
    public String getMimeType() {
        return mimeType;
    }

    /**
     * @param mimeType the mimeType to set
     */
    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    /**
     * The media type of the retrieved content.
     * 
     * @see <a href="http://www.iana.org/assignments/media-types/"> http://www.iana.org/assignments/media-types/</a>
     */
    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    /** Other protocol-specific data. */
    public Metadata getMetadata() {
        return metadata;
    }

    /** Other protocol-specific data. */
    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Content)) {
            return false;
        }
        Content that = (Content)o;
        return this.url.equals(that.url) && this.base.equals(that.base) && Arrays.equals(this.getContent(), that.getContent()) && this.contentType.equals(that.contentType)
            && this.metadata.equals(that.metadata);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append("url: ").append(url).append("\n");
        builder.append("base: ").append(base).append("\n");
        builder.append("contentType: ").append(contentType).append("\n");
        builder.append("metadata: ").append(metadata).append("\n");
        builder.append("Content:\n");
        builder.append(new String(content)); // try default encoding

        return builder.toString();
    }

    private static class Null extends Content {}

}
