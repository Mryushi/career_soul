package com.maiqu.career_soul.service;

import com.maiqu.career_soul.javabean.dao.Report;
import com.maiqu.career_soul.javabean.pojo.Schools;
import com.maiqu.career_soul.javabean.pojo.UserInfo;

import java.util.List;

public interface StructureInfoService {

    void updateUserInfo(UserInfo userInfo);

    List<Schools> getSchools(String query);

    Report getReport(Long userId);
}
