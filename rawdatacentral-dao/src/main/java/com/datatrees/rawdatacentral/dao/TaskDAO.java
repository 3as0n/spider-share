/**
 * This document and its contents are protected by copyright 2015 and owned by datatrees.com Inc.
 * The copying and reproduction of this document and/or its content (whether wholly or partly) or
 * any incorporation of the same into any other material in any media or format of any kind is
 * strictly prohibited. All rights are reserved.
 * Copyright (c) datatrees.com Inc. 2015
 */

package com.datatrees.rawdatacentral.dao;

import javax.annotation.Resource;
import java.util.Date;

import com.datatrees.rawdatacentral.dao.mapper.TaskMapper;

/**
 * Created by zhouxinghai on 2017/6/29
 */
@Resource
public interface TaskDAO extends TaskMapper {

    public Date selectNow();

}