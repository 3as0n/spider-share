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

package com.datatrees.spider.share.service.typehandler;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by zhouxinghai on 2017/7/4.
 */
public class AtomicLongTypeHander implements TypeHandler {

    private static final Logger logger = LoggerFactory.getLogger(AtomicLongTypeHander.class);

    @Override
    public void setParameter(PreparedStatement ps, int i, Object parameter, JdbcType jdbcType) throws SQLException {
        Long v = null;
        if (null != parameter) {
            v = ((Number)parameter).longValue();
        }
        ps.setLong(i, v);

    }

    @Override
    public Object getResult(ResultSet rs, String columnName) throws SQLException {
        String v = rs.getString(columnName);
        if (!StringUtils.isEmpty(v)) {
            return new AtomicLong(Long.parseLong(v));
        }
        return null;
    }

    @Override
    public Object getResult(ResultSet rs, int columnIndex) throws SQLException {
        String v = rs.getString(columnIndex);
        if (!StringUtils.isEmpty(v)) {
            return new AtomicLong(Long.parseLong(v));
        }
        return null;
    }

    @Override
    public Object getResult(CallableStatement cs, int columnIndex) throws SQLException {
        return null;
    }
}
