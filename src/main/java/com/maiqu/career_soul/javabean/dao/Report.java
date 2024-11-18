package com.maiqu.career_soul.javabean.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Report {
    private String eduLevel;
    private String major;
    private String targetPosition;
    private String workLocation;
    private String hobbies;
    private String personalityTraits;
    private String salaryExpectation;

    //目标职位匹配度
    private String positionCompatibility;
    //建议
    private String careerAdvice;

    @Override
    public String toString() {
        return "学历层次：" + eduLevel + "\n专业：" + major + "\n目标职位：" + targetPosition + "\n期望工作地区："
                + workLocation + "\n兴趣爱好：" + hobbies + "\n人格倾向：" + personalityTraits + "\n期望薪资："
                + salaryExpectation;
    }
}
