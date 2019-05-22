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

package com.datatrees.spider.share.service.collector.common;

import com.datatrees.common.util.GsonUtils;
import com.sleepycat.bind.tuple.TupleBinding;
import com.sleepycat.bind.tuple.TupleInput;
import com.sleepycat.bind.tuple.TupleOutput;
import com.treefinance.crawler.framework.context.function.LinkNode;

import java.util.Map;

/**
 * @author <A HREF="">Cheng Wang</A>
 * @version 1.0
 * @since 2015年7月20日 上午12:45:41
 */
public class LinkNodeTupleBinding extends TupleBinding<LinkNode> {

    @Override
    public LinkNode entryToObject(TupleInput input) {
        LinkNode linkNode = new LinkNode(input.readString());
        linkNode.setDepth(input.readInt());
        linkNode.setRedirectUrl(input.readString());
        linkNode.setRetryCount(input.readInt());
        linkNode.setPageTitle(input.readString());
        linkNode.setReferer(input.readString());
        linkNode.addHeaders((Map<String, String>)GsonUtils.fromJson(input.readString(), Map.class));
        linkNode.addPropertys((Map<String, Object>)GsonUtils.fromJson(input.readString(), Map.class));

        return linkNode;
    }

    @Override
    public void objectToEntry(LinkNode linkNode, TupleOutput output) {
        output.writeString(linkNode.getUrl());
        output.writeInt(linkNode.getDepth());
        output.writeString(linkNode.getRedirectUrl());
        output.writeInt(linkNode.getRetryCount());
        output.writeString(linkNode.getPageTitle());
        output.writeString(linkNode.getReferer());
        output.writeString(GsonUtils.toJson(linkNode.getHeaders()));
        output.writeString(GsonUtils.toJson(linkNode.getPropertys()));
    }
}
