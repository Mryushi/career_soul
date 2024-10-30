package com.maiqu.career_soul.service.impl;

import com.alibaba.dashscope.aigc.generation.GenerationResult;
import com.alibaba.dashscope.common.Message;
import com.alibaba.dashscope.common.Role;
import com.maiqu.career_soul.mapper.TestMapper;
import com.maiqu.career_soul.pojo.Conversations;
import com.maiqu.career_soul.pojo.Messages;
import com.maiqu.career_soul.service.TestService;
import com.maiqu.career_soul.utils.AlibabaAIUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class TestImpl implements TestService {

    @Autowired
    private TestMapper testMapper;

    @Autowired
    private AlibabaAIUtil alibabaAIUtil;


    @Override
    public String getIQResponse(Integer conversationsId, String userInput) {
        List<Messages> messages = testMapper.getMessagesByConversationId(conversationsId);

        List<Message> chatMessages = alibabaAIUtil.makeChatMessages(messages);
        chatMessages.add(Message.builder().role(Role.USER.getValue()).content(userInput).build());

        GenerationResult aiResponse = null;
        try {
            aiResponse = alibabaAIUtil.getAIResponse(chatMessages, "qwen-turbo-2024-09-19");
            String content = aiResponse.getOutput().getChoices().get(0).getMessage().getContent();

            Messages messagesAfterAI = new Messages();
            messagesAfterAI.setConversationId(conversationsId);
            messagesAfterAI.setAiMessage(content);
            messagesAfterAI.setUserMessage(userInput);
            testMapper.addMessage(messagesAfterAI);
            return content;
        } catch (Exception e) {
            log.error("模型返回异常：" + aiResponse);
            return "400";
        }
    }

    @Override
    public Integer addConversation(Integer userId, Integer agentId) {
        Conversations conversations = new Conversations();
        conversations.setUserId(userId);
        conversations.setAgentId(agentId);
        conversations.setCreateTime(LocalDateTime.now());
        conversations.setUpdateTime(LocalDateTime.now());

        testMapper.addConversation(conversations);

        return conversations.getConversationId();
    }
}
