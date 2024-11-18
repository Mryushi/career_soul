package com.maiqu.career_soul.controller;

import com.maiqu.career_soul.javabean.Result;
import com.maiqu.career_soul.javabean.dao.Report;
import com.maiqu.career_soul.javabean.pojo.Schools;
import com.maiqu.career_soul.javabean.pojo.UserInfo;
import com.maiqu.career_soul.service.StructureInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")

@RestController
@RequestMapping("maiqu/structured_info")
public class StructuredInfoController {

    @Autowired
    private StructureInfoService structureInfoService;

    @PutMapping("user_info")
    public Result updateUserInfo(@RequestBody UserInfo userInfo){
        System.out.println(userInfo);
        structureInfoService.updateUserInfo(userInfo);

        return Result.success();
    }

    @GetMapping("schools/{schoolName}")
    public Result getSchool(@PathVariable("schoolName") String schoolName){
        System.out.println("xuexiaoming: " + schoolName);
        Result result = new Result();

        if (schoolName == null) {
            return Result.success();
        }

        List<Schools> schools = structureInfoService.getSchools(schoolName);
        result.setCode(200);
        result.setMsg("success");
        result.setData(schools);
        return result;
    }

    @GetMapping("report/{userId}")
    public Result getReport(@PathVariable("userId") Long userId){
        System.out.println(userId);
        Result result = new Result();

        Report report = structureInfoService.getReport(userId);

        result.setCode(200);
        result.setMsg("success");
        result.setData(report);
        return result;
    }
}
