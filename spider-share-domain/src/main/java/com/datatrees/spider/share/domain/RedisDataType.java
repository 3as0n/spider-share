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

package com.datatrees.spider.share.domain;

/**
 * redis key type
 */
public class RedisDataType {

    public static final String NONE   = "none";//key不存在
    public static final String STRING = "string";//string类型
    public static final String HASH   = "hash";//map类型
    public static final String LIST   = "list";//list类型
    public static final String SET    = "set";//set类型
    public static final String ZSET   = "zset";//有序set类型
}
