package com.maiqu.career_soul.service.impl;

import com.alibaba.dashscope.aigc.generation.GenerationResult;
import com.alibaba.dashscope.common.Message;
import com.alibaba.dashscope.common.Role;
import com.maiqu.career_soul.javabean.dao.Report;
import com.maiqu.career_soul.javabean.pojo.Schools;
import com.maiqu.career_soul.javabean.pojo.UserInfo;
import com.maiqu.career_soul.mapper.StructureInfoMapper;
import com.maiqu.career_soul.prompt.ReportSystemPrompt;
import com.maiqu.career_soul.service.StructureInfoService;
import com.maiqu.career_soul.utils.AlibabaAIUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 实现结构化信息生成报告的服务
 */
@Slf4j
@Service
public class StructureInfoServiceImpl implements StructureInfoService {

    // 存储报告提示词
    private static final Map<String, String> REPORT_SYSTEM_MESSAGES = new HashMap<>();

    //静态代码块，用于初始化报告提示词
    static {
        REPORT_SYSTEM_MESSAGES.put("positionCompatibility", ReportSystemPrompt.POSITION_COMPATIBILITY_SYSTEM_MESSAGE);
        REPORT_SYSTEM_MESSAGES.put("careerAdvice", ReportSystemPrompt.careerAdvice_SYSTEM_MESSAGE);
    }

    @Autowired
    private StructureInfoMapper structureInfoMapper;

    @Autowired
    private AlibabaAIUtil alibabaAIUtil;


    @Override
    public void updateUserInfo(UserInfo userInfo) {
        structureInfoMapper.updateUserInfo(userInfo);
    }


    @Override
    public List<Schools> getSchools(String query) {
        List<Schools> schools = structureInfoMapper.getSchools(query);
        return schools;
    }


    @Override
    public Report getReport(Long userId) {
        Report report = new Report();

        // 获取数据库中的用户信息
        UserInfo baseInfo = structureInfoMapper.getUserInfo(userId);

        if (baseInfo.getDoctorCollege() != null && !baseInfo.getDoctorCollege().isEmpty()) {
            report.setEduLevel("博士");
            report.setMajor(baseInfo.getDoctorMajor());
        } else if (baseInfo.getMasterCollege() != null && !baseInfo.getMasterCollege().isEmpty()) {
            report.setEduLevel("硕士");
            report.setMajor(baseInfo.getMasterMajor());
        } else if (baseInfo.getUndergraduateCollege() != null && !baseInfo.getUndergraduateCollege().isEmpty()) {
            report.setEduLevel("本科");
            report.setMajor(baseInfo.getUndergraduateMajor());
        } else {
            report.setEduLevel("其他");
            report.setMajor("其他");
        }

        report.setTargetPosition(baseInfo.getTargetPosition());
        report.setWorkLocation(baseInfo.getWorkLocation());
        report.setHobbies(baseInfo.getHobbies());
        report.setPersonalityTraits(baseInfo.getPersonalityTraits());
        report.setSalaryExpectation(baseInfo.getSalaryExpectation());

        // 用户基本信息
        String userInfo = report.toString();

        // 获取报告中的岗位匹配度（AI）
        String positionCompatibility = getReportResponse(userInfo, "qwen-turbo-2024-09-19", REPORT_SYSTEM_MESSAGES.get("positionCompatibility"));
        report.setPositionCompatibility(positionCompatibility);

        // 获取报告中的职业建议（AI）
        String careerAdvice = getReportResponse(userInfo, "qwen-turbo-2024-09-19", REPORT_SYSTEM_MESSAGES.get("careerAdvice"));
        report.setCareerAdvice(careerAdvice);

        return report;
    }

    private String getReportResponse(String userInfo, String model, String reportSystemMessage) {
        List<Message> chatMessages = new ArrayList<>();

        // 报告生成内容提示词
        chatMessages.add(Message.builder().role(Role.SYSTEM.getValue()).content(reportSystemMessage).build());

        // 用户基本信息
        chatMessages.add(Message.builder().role(Role.USER.getValue()).content(userInfo).build());

        // 调用模型
        GenerationResult aiResponse = null;
        try {
            aiResponse = alibabaAIUtil.getQwenResponse(chatMessages, model, true);
            String content = aiResponse.getOutput().getChoices().get(0).getMessage().getContent();
            return content;
        } catch (Exception e) {
            log.error("模型返回异常：" + aiResponse);
            return "400";
        }
    }
}
