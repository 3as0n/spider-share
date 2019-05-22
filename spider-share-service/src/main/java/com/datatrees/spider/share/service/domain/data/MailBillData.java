
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

package com.datatrees.spider.share.service.domain.data;

import com.datatrees.spider.share.domain.AbstractData;
import com.treefinance.crawler.framework.consts.MailConsts;

import java.util.Date;

/**
 * @author <A HREF="">Cheng Wang</A>
 * @version 1.0
 * @since 2015年7月26日 下午1:32:20
 */
public class MailBillData extends AbstractData {

    public static final String SENDER = "sender";

    public static final String SUBJECT = "subject";

    public static final String RECEIVED = "receiveAt";

    public static final String RECEIVER = "receiver";

    public static final String FIRSTHAND = "firstHand";

    public static final String FOLDER = "folder";

    public static final String BANKID = "bankid";

    public Integer getBankId() {
        return (Integer)this.get(BANKID);
    }

    public void setBankId(int bankid) {
        this.put(BANKID, bankid);
    }

    public String getFolder() {
        return (String)this.get(FOLDER);
    }

    public void setFolder(String folder) {
        this.put(FOLDER, folder);
    }

    public Boolean getFirstHand() {
        return (Boolean)this.get(FIRSTHAND);
    }

    public void setFirstHand(Boolean isFirstHand) {
        this.put(FIRSTHAND, isFirstHand);
    }

    public String getMailHeader() {
        return (String)this.get(MailConsts.MAIL_HEADER);
    }

    public void setMailHeader(String mailHeader) {
        this.put(MailConsts.MAIL_HEADER, mailHeader);
    }

    public String getSender() {
        return (String)this.get(SENDER);
    }

    public void setSender(String sender) {
        this.put(SENDER, sender);
    }

    public String getSubject() {
        return (String)this.get(SUBJECT);

    }

    public void setSubject(String subject) {
        this.put(SUBJECT, subject);
    }

    public String getReceiver() {
        return (String)this.get(RECEIVER);

    }

    public void setReceiver(String receiver) {
        this.put(RECEIVER, receiver);
    }

    public Date getReceiveAt() {
        return (Date)this.get(RECEIVED);
    }

    public void setReceiveAt(Date receiveAt) {
        this.put(RECEIVED, receiveAt);
    }

}
