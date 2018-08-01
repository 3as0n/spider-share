package com.datatrees.spider.share.dao.mapper;

import java.util.List;

import com.datatrees.spider.share.domain.model.WebsiteInfo;
import com.datatrees.spider.share.domain.model.WebsiteInfoCriteria;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface WebsiteInfoMapper {

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table website_info
     * @mbg.generated
     */
    long countByExample(WebsiteInfoCriteria example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table website_info
     * @mbg.generated
     */
    int deleteByExample(WebsiteInfoCriteria example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table website_info
     * @mbg.generated
     */
    int deleteByPrimaryKey(Integer websiteId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table website_info
     * @mbg.generated
     */
    int insert(WebsiteInfo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table website_info
     * @mbg.generated
     */
    int insertSelective(WebsiteInfo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table website_info
     * @mbg.generated
     */
    List<WebsiteInfo> selectByExampleWithBLOBsWithRowbounds(WebsiteInfoCriteria example, RowBounds rowBounds);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table website_info
     * @mbg.generated
     */
    List<WebsiteInfo> selectByExampleWithBLOBs(WebsiteInfoCriteria example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table website_info
     * @mbg.generated
     */
    List<WebsiteInfo> selectByExampleWithRowbounds(WebsiteInfoCriteria example, RowBounds rowBounds);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table website_info
     * @mbg.generated
     */
    List<WebsiteInfo> selectByExample(WebsiteInfoCriteria example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table website_info
     * @mbg.generated
     */
    WebsiteInfo selectByPrimaryKey(Integer websiteId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table website_info
     * @mbg.generated
     */
    int updateByExampleSelective(@Param("record") WebsiteInfo record, @Param("example") WebsiteInfoCriteria example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table website_info
     * @mbg.generated
     */
    int updateByExampleWithBLOBs(@Param("record") WebsiteInfo record, @Param("example") WebsiteInfoCriteria example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table website_info
     * @mbg.generated
     */
    int updateByExample(@Param("record") WebsiteInfo record, @Param("example") WebsiteInfoCriteria example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table website_info
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(WebsiteInfo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table website_info
     * @mbg.generated
     */
    int updateByPrimaryKeyWithBLOBs(WebsiteInfo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table website_info
     * @mbg.generated
     */
    int updateByPrimaryKey(WebsiteInfo record);
}