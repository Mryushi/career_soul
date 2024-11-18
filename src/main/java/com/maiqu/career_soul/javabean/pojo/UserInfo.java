package com.maiqu.career_soul.javabean.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfo {
    private Long userId;
    private String userName;
    private String tel;
    private String birthDate;
    private String gender; // 0: 男, 1: 女
    private String personalityTraits;
    private String hobbies;

    private String eduLevel;
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
    private String resourceFrom;
    private String neededGuidance;
    private String projectExperience;

}
