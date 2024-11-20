package com.maiqu.career_soul.controller;

import com.maiqu.career_soul.javabean.Result;
import com.maiqu.career_soul.javabean.pojo.Register;
import com.maiqu.career_soul.service.RegAndLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")

@RestController
@RequestMapping("maiqu/career_soul")
public class RegisterAndLoginController {

    @Autowired
    private RegAndLogService regAndLogService;

    @PostMapping("register")
    public Result register(@RequestBody Register register) {
        Result result = new Result();

        String userName = register.getUserName();
        String password = register.getPassword();

//        System.out.println("register: " + register);
        if (userName == null) {
            return Result.error("用户名不能为空");
        }

        if (password == null) {
            return Result.error("密码不能为空");
        }

        String registerInfo = regAndLogService.register(userName, password);

        if (registerInfo == "用户名已存在") {
            result.setCode(409);
            result.setMsg("用户名已存在");
            return result;
        }

        if (registerInfo == "注册失败") {
            result.setCode(501);
            result.setMsg("注册失败,请重新注册");
            return result;
        }

        result.setCode(200);
        result.setMsg("注册成功");
        result.setData(registerInfo);

        return result;
    }

    @PostMapping("login")
    public Result login(@RequestBody Register register) {
        Result result = new Result();

        String userName = register.getUserName();
        String password = register.getPassword();

//        System.out.println("login: " + register);
        if (userName == null) {
            return Result.error("用户名不能为空");
        }

        if (password == null) {
            return Result.error("密码不能为空");
        }

        String loginInfo = regAndLogService.login(userName, password);

        if (loginInfo == "用户名或密码错误") {
            result.setCode(401);
            result.setMsg("用户名或密码错误");
            return result;
        }

        result.setCode(200);
        result.setMsg("登录成功");
        result.setData(loginInfo);

        return result;
    }

}
