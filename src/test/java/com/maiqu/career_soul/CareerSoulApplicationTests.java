package com.maiqu.career_soul;

import com.maiqu.career_soul.controller.TestController;
import com.maiqu.career_soul.pojo.Conversations;
import com.maiqu.career_soul.pojo.Result;
import com.maiqu.career_soul.service.TestService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

@SpringBootTest
class CareerSoulApplicationTests {

    @Autowired
    private TestController testController;

    @Autowired
    private TestService testService;

    @Test
    void AIResponse() {
        Result result = testController.IQTest(1, "总结一下本次交流");

        System.out.println("AI回答：" + result);
    }

    @Test
    void addConversation() {
        Conversations conversations = new Conversations();
        conversations.setUserId(1);
        conversations.setAgentId(1);
        conversations.setCreateTime(LocalDateTime.now());
        conversations.setUpdateTime(LocalDateTime.now());
        Integer conversationId = testService.addConversation(1, 1);
        System.out.println("回显ID：" + conversationId);
    }
}
