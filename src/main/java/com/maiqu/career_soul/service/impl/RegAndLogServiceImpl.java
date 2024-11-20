package com.maiqu.career_soul.service.impl;

import com.maiqu.career_soul.javabean.pojo.Register;
import com.maiqu.career_soul.mapper.RegisterMapper;
import com.maiqu.career_soul.mapper.StructureInfoMapper;
import com.maiqu.career_soul.service.RegAndLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class RegAndLogServiceImpl implements RegAndLogService {

    @Autowired
    private RegisterMapper registerMapper;

    @Autowired
    private StructureInfoMapper structureInfoMapper;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public String register(String userName, String password) {
        Register reg = registerMapper.findByUserName(userName);

        if (reg != null) {
            return "用户名已存在";
        }

        Register register = new Register();
        register.setUserName(userName);
        register.setPassword(password);
        register.setCreatedTime(LocalDateTime.now());
        register.setUpdatedTime(LocalDateTime.now());

        try {
            registerMapper.addUser(register);
            structureInfoMapper.addUser(userName);
        } catch (Exception e) {
            return "注册失败";
        }

        String userId = structureInfoMapper.findUserName(userName);
        return "userId:" + userId;
    }

    @Override
    public String login(String userName, String password) {

        Register register = registerMapper.findByUserNameAndPassword(userName,password);

        if (register == null) {
            return "用户名或密码错误";
        }

        String userId = structureInfoMapper.findUserName(userName);
        return "userId:" + userId;
    }


}
