package com.datatrees.rawdatacentral.dao.mapper;

import com.datatrees.rawdatacentral.domain.model.WebsiteGroup;
import com.datatrees.rawdatacentral.domain.model.example.WebsiteGroupExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

 /** create by system from table operator_group(运营商分组)  */
public interface WebsiteGroupMapper {
    int countByExample(WebsiteGroupExample example);

    int deleteByExample(WebsiteGroupExample example);

    int insertSelective(WebsiteGroup record);

    List<WebsiteGroup> selectByExample(WebsiteGroupExample example);

    int updateByExampleSelective(@Param("record") WebsiteGroup record, @Param("example") WebsiteGroupExample example);
}