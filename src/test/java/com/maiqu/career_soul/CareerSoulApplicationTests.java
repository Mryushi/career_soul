package com.maiqu.career_soul;

import com.maiqu.career_soul.controller.ChatController;
import com.maiqu.career_soul.javabean.dto.ChatRequest;
import com.maiqu.career_soul.javabean.Result;
import com.maiqu.career_soul.service.ChatService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CareerSoulApplicationTests {

    @Autowired
    private ChatController chatController;

    @Autowired
    private ChatService chatService;

    @Test
    void AIResponse() {
        ChatRequest chatRequest = new ChatRequest();
        chatRequest.setConversationsId(1L);
        chatRequest.setUserInput("你好");

        Result result = chatController.AIChat(chatRequest);

        System.out.println("AI回答：" + result);
    }

    @Test
    void addConversation() {
        ChatRequest chatRequest = new ChatRequest();
        chatRequest.setUserId(1L);
        chatRequest.setAgentName("Holland");

        Result conversationId = chatController.addConversation(chatRequest);
        System.out.println("回显ID：" + conversationId);
    }
}
