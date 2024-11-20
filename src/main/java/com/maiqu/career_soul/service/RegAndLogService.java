package com.maiqu.career_soul.service;

import com.maiqu.career_soul.javabean.pojo.Register;

public interface RegAndLogService {

    String register(String userName, String password);

    String login(String userName, String password);

}
