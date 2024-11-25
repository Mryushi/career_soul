package com.maiqu.career_soul.javabean.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfo {
    private Long userId;
    private String userName;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd")
    private Date birthDate;
    private String gender;
    private String personalityTraits;
    private String hobbies;

    private String majorLevel;
    private String currentGrade;
    private String undergraduateCollege;
    private String masterCollege;
    private String doctorCollege;
    private String undergraduateMajor;
    private String masterMajor;
    private String doctorMajor;
    private String advantageSubjects;

    private String targetIndustry;
    private String targetPosition;
    private String salaryExpectation;
    private String workLocation;
    //未来规划
    private String futurePlan;
    //对就业市场的了解情况
    private String marketUnderstanding;
    //信息提供
    private String infoProvide;
    //能力提升
    private String abilityImprove;
    private String projectExperience;

    //用户信息总结，后端拼接   或者  AI总结（前端需考虑触发时机）
    private String personalInfo;
}
