package com.maiqu.career_soul.service;

import com.maiqu.career_soul.javabean.dao.Report;
import com.maiqu.career_soul.javabean.pojo.Schools;
import com.maiqu.career_soul.javabean.pojo.UserInfo;

import java.util.List;

public interface StructureInfoService {

    /**
     * 更新用户信息
     *
     * @param userInfo 用户信息对象
     */
    void updateUserInfo(UserInfo userInfo);

    /**
     * 获取学校列表
     *
     * @param query 查询条件
     * @return 学校列表
     */
    List<Schools> getSchools(String query);

    /**
     * 获取报告
     *
     * @param userId 用户ID
     * @return 报告对象
     */
    Report getReport(Long userId);
}
