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

package com.treefinance.crawler.framework.process.operation.impl;

import com.treefinance.crawler.framework.download.WrappedFile;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.james.mime4j.MimeIOException;
import org.apache.james.mime4j.field.address.MailboxList;
import org.apache.james.mime4j.message.Message;
import org.apache.james.mime4j.parser.MimeEntityConfig;
import org.apache.james.mime4j.storage.StorageProvider;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author <A HREF="">Cheng Wang</A>
 * @version 1.0
 * @since 2015年10月8日 上午11:39:58
 */
class Mail extends Message {

    private StringBuilder txtBody = new StringBuilder();

    private StringBuilder htmlBody = new StringBuilder();

    private List<WrappedFile> attachments = new ArrayList<>();
    private String websiteName;

    public Mail() {
        super();
    }

    public Mail(InputStream is, MimeEntityConfig config, StorageProvider storageProvider) throws IOException, MimeIOException {
        super(is, config, storageProvider);
    }

    public Mail(InputStream is, MimeEntityConfig config) throws IOException {
        super(is, config);
    }

    public Mail(InputStream is) throws IOException {
        super(is);
    }

    public Mail(Message other) {
        super(other);
    }

    public String getTxtBody() {
        return txtBody.toString();
    }

    public void appendText(String text) {
        if (StringUtils.isNotEmpty(text)) {
            txtBody.append(text);
        }
    }

    public String getHtmlBody() {
        return htmlBody.toString();
    }

    public void appendHtml(String html) {
        if (StringUtils.isNotEmpty(html)) {
            htmlBody.append(html);
        }
    }

    public List<WrappedFile> getAttachments() {
        return attachments;
    }

    public void addAttachment(WrappedFile attachment) {
        if (attachment != null) {
            attachments.add(attachment);
        }
    }

    public String getWebsiteName() {
        return websiteName;
    }

    public void setWebsiteName(String websiteName) {
        this.websiteName = websiteName;
    }

    public String getFromAddress() {
        MailboxList from = getFrom();

        return CollectionUtils.isNotEmpty(from) ? from.get(0).getAddress() : StringUtils.EMPTY;
    }

}
