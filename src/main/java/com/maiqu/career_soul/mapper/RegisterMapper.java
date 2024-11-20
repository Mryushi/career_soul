package com.maiqu.career_soul.mapper;

import com.maiqu.career_soul.javabean.pojo.Register;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface RegisterMapper {

    @Select("select user_name from register where user_name = #{userName}")
    Register findByUserName(String userName);

    @Insert("insert into register(user_name, password, created_time, updated_time) " +
            "values(#{userName}, #{password}, #{createdTime}, #{updatedTime})")
    void addUser(Register register);

    @Select("select user_name, password from register " +
            "where user_name = #{userName} and password = #{password}")
    Register findByUserNameAndPassword(@Param("userName") String userName,@Param("password") String password);
}
