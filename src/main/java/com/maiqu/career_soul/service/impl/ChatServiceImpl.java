package com.maiqu.career_soul.service.impl;

import com.alibaba.dashscope.aigc.generation.GenerationResult;
import com.alibaba.dashscope.common.Message;
import com.alibaba.dashscope.common.Role;
import com.maiqu.career_soul.javabean.dto.ChatRequest;
import com.maiqu.career_soul.javabean.pojo.UserInfo;
import com.maiqu.career_soul.mapper.ChatMapper;
import com.maiqu.career_soul.javabean.pojo.Conversations;
import com.maiqu.career_soul.javabean.pojo.Messages;
import com.maiqu.career_soul.mapper.StructureInfoMapper;
import com.maiqu.career_soul.prompt.ChatSystemPrompt;
import com.maiqu.career_soul.service.ChatService;
import com.maiqu.career_soul.utils.AlibabaAIUtil;
import com.maiqu.career_soul.utils.ZhiPuAIUtil;
import com.zhipu.oapi.service.v4.model.ChatMessage;
import com.zhipu.oapi.service.v4.model.ChatMessageRole;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class ChatServiceImpl implements ChatService {

    private static final Map<String, String> CHAT_AGENT_SYSTEM_MESSAGES = new HashMap<>();

    static {
        CHAT_AGENT_SYSTEM_MESSAGES.put("Chat", ChatSystemPrompt.Chat_System_Prompt);
        CHAT_AGENT_SYSTEM_MESSAGES.put("Holland", ChatSystemPrompt.Holland_SYSTEM_MESSAGE);
        CHAT_AGENT_SYSTEM_MESSAGES.put("IQ", ChatSystemPrompt.IQ_System_Prompt);
    }

    @Autowired
    private ChatMapper chatMapper;

    @Autowired
    private StructureInfoMapper structureInfoMapper;

    @Autowired
    private AlibabaAIUtil alibabaAIUtil;

    @Autowired
    private ZhiPuAIUtil zhiPuAIUtil;

    @Override
    public String getResponse(ChatRequest chatRequest) {
        Long conversationsId = chatRequest.getConversationsId();
        String userInput = chatRequest.getUserInput();
        String agentName = chatRequest.getAgentName();

        String agentSystemMessage = CHAT_AGENT_SYSTEM_MESSAGES.get(agentName);

        if (agentName.equals("Chat")) {
            chatRequest.setUserId(1L);
            return getGLMEmohaaResponse(chatRequest);
        } else {
            return getAliQwenResponse(conversationsId, userInput, "qwen-max", agentSystemMessage);
        }
    }

    @Override
    public Long addConversation(Long userId, String agentName) {
        Conversations conversations = new Conversations();
        conversations.setUserId(userId);
        conversations.setAgentName(agentName);
        conversations.setCreateTime(LocalDateTime.now());
        conversations.setUpdateTime(LocalDateTime.now());

        chatMapper.addConversation(conversations);

        return conversations.getConversationId();
    }

    private String getAliQwenResponse(Long conversationsId, String userInput, String model, String testSystemMessage) {
        List<Messages> messages = chatMapper.getMessagesByConversationId(conversationsId);

        List<Message> chatMessages = alibabaAIUtil.makeQwenMessages(messages, testSystemMessage);
        chatMessages.add(Message.builder().role(Role.USER.getValue()).content(userInput).build());

        GenerationResult aiResponse = null;
        try {
            aiResponse = alibabaAIUtil.getQwenResponse(chatMessages, model, false);
            String content = aiResponse.getOutput().getChoices().get(0).getMessage().getContent();

            writeIntoDB(conversationsId, userInput, content);

            return content;
        } catch (Exception e) {
            log.error("模型返回异常：" + aiResponse);
            return "400";
        }
    }

    private String getGLMEmohaaResponse(ChatRequest chatRequest) {
        Long userId = chatRequest.getUserId();
        Long conversationsId = chatRequest.getConversationsId();
        String userInput = chatRequest.getUserInput();

        List<Messages> messages = chatMapper.getMessagesByConversationId(conversationsId);
        List<ChatMessage> chatMessages = zhiPuAIUtil.makeGLMMessages(messages, null);
        chatMessages.add(new ChatMessage(ChatMessageRole.USER.value(), userInput));

        UserInfo userInfo = structureInfoMapper.getUserInfo(userId);
        String eduLevel = userInfo.getEduLevel();

        StringBuilder userInfoBuilder = new StringBuilder();
        userInfoBuilder.append("我是一名高校" + userInfo.getGender() + "学生，我的专业是");

//        userInfoBuilder.append("我是一名" + userInfo.getCurrentGrade() + "的" + userInfo.getGender() + "学生，我的专业是");
//        if (eduLevel.equals("博士")) {
//            userInfoBuilder.append(userInfo.getDoctorMajor());
//        } else if (eduLevel.equals("硕士")) {
//            userInfoBuilder.append(userInfo.getMasterMajor());
//        } else if (eduLevel.equals("本科")) {
//            userInfoBuilder.append(userInfo.getUndergraduateMajor());
//        }
//        userInfoBuilder.append("。");

        if (!userInfo.getHobbies().isEmpty()) {
            userInfoBuilder.append("我的爱好是" + userInfo.getHobbies() + "。");
        }

        if (!userInfo.getTargetIndustry().isEmpty()) {
            userInfoBuilder.append("我想做" + userInfo.getTargetIndustry() + "行业的工作。");
        }

        String userInfoStr = userInfoBuilder.toString();
        String userName = userInfo.getUserName();
        String aiResponse = zhiPuAIUtil.getGLMEmohaaResponse(chatMessages, userName, userInfoStr);

        return aiResponse;
    }

    private void writeIntoDB(Long conversationsId, String userInput, String content) {
        Messages messagesAfterAI = new Messages();
        messagesAfterAI.setConversationId(conversationsId);
        messagesAfterAI.setAiMessage(content);
        messagesAfterAI.setUserMessage(userInput);
        chatMapper.addMessage(messagesAfterAI);
    }
}
