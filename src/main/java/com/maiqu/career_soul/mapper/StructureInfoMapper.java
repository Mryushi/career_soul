package com.maiqu.career_soul.mapper;

import com.maiqu.career_soul.javabean.pojo.Schools;
import com.maiqu.career_soul.javabean.pojo.UserInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface StructureInfoMapper {

    void updateUserInfo(UserInfo userInfo);

    @Select("select school_name from schools where school_name like concat(#{schoolName}, '%')")
    List<Schools> getSchools(String query);

//    ??
    @Select("select * from user_info where user_id = #{userId}")
    UserInfo getUserInfo(@Param("userId") Long userId);

    UserInfo getBaseReportInfo(@Param("userId") Long userId);
}
