package com.maiqu.career_soul.mapper;

import com.maiqu.career_soul.javabean.pojo.Schools;
import com.maiqu.career_soul.javabean.pojo.UserInfo;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface StructureInfoMapper {

    void updateUserInfo(UserInfo userInfo);

    @Select("select school_name from schools where school_name like concat(#{schoolName}, '%')")
    List<Schools> getSchools(String query);

    @Select("select user_name, birth_date, gender, major_level, current_grade, advantage_subjects," +
            " personality_traits, hobbies, undergraduate_college, master_college, doctor_college," +
            " undergraduate_major, master_major, doctor_major, target_industry, target_position," +
            " salary_expectation, work_location, future_plan, market_understanding, info_provide," +
            " ability_improve, project_experience, personal_info from user_info where user_id = #{userId}")
    UserInfo getUserInfo(@Param("userId") Long userId);

    @Insert("insert into user_info(user_name) " +
            "values (#{userName})")
    void addUser(String userName);

    @Select("select user_id from user_info where user_name = #{userName}")
    String findUserName(String userName);
}
