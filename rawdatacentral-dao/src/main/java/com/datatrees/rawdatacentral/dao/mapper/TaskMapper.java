package com.datatrees.rawdatacentral.dao.mapper;

import com.datatrees.rawdatacentral.domain.model.Task;
import com.datatrees.rawdatacentral.domain.model.example.TaskExample;
import java.util.List;

 /** create by system from table t_tasklog(task log info)  */
public interface TaskMapper {
    long countByExample(TaskExample example);

    int deleteByPrimaryKey(Integer id);

    int insertSelective(Task record);

    List<Task> selectByExample(TaskExample example);

    Task selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Task record);
}