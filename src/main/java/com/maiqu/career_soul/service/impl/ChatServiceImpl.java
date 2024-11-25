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

/**
 * 聊天服务实现类
 */
@Slf4j
@Service
public class ChatServiceImpl implements ChatService {

    /**
     * 存储不同聊天的系统提示词
     */
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

    /**
     * 获取聊天响应
     *
     * @param chatRequest 聊天请求对象，包含会话ID、用户输入和代理名称
     * @return 根据不同代理返回相应的聊天响应
     */
    @Override
    public String getResponse(ChatRequest chatRequest) {
        Long conversationsId = chatRequest.getConversationsId();
        String userInput = chatRequest.getUserInput();
        String agentName = chatRequest.getAgentName();

        String agentSystemMessage = CHAT_AGENT_SYSTEM_MESSAGES.get(agentName);

        if (agentName.equals("Chat")) {
            return getGLMEmohaaResponse(chatRequest);
        } else {
            return getAliQwenResponse(conversationsId, userInput, "qwen-turbo-2024-09-19", agentSystemMessage);
        }
    }

    /**
     * 添加会话记录
     *
     * @param userId 用户ID
     * @param agentName 代理名称
     * @return 新增会话的ID
     */
    @Override
    public Long addConversation(Long userId, String agentName) {
        Conversations conversations = new Conversations();
        conversations.setUserId(userId);
        conversations.setAgentName(agentName);
        conversations.setCreatedTime(LocalDateTime.now());
        conversations.setUpdatedTime(LocalDateTime.now());

        chatMapper.addConversation(conversations);

        return conversations.getConversationId();
    }

    /**
     * 获取阿里云Qwen模型的响应
     *
     * @param conversationsId 会话ID
     * @param userInput 用户输入
     * @param model 模型名称
     * @param testSystemMessage 系统消息
     * @return 阿里云Qwen模型的响应内容
     */
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
            return "400";
        }
    }

    /**
     * 获取GLM-Emohaa模型的响应
     *
     * @param chatRequest 聊天请求对象
     * @return GLM-Emohaa模型的响应内容
     */
    private String getGLMEmohaaResponse(ChatRequest chatRequest) {
        Long userId = chatRequest.getUserId();
        Long conversationsId = chatRequest.getConversationsId();
        String userInput = chatRequest.getUserInput();

        List<Messages> messages = chatMapper.getMessagesByConversationId(conversationsId);
        List<ChatMessage> chatMessages = zhiPuAIUtil.makeGLMMessages(messages, null);
        chatMessages.add(new ChatMessage(ChatMessageRole.USER.value(), userInput));

        UserInfo userInfo = structureInfoMapper.getUserInfo(userId);
        StringBuilder userInfoBuilder = new StringBuilder();
        userInfoBuilder.append("我是一名高校" + userInfo.getGender() + "学生，我的专业是");

        if (!userInfo.getHobbies().isEmpty()) {
            userInfoBuilder.append("我的爱好是" + userInfo.getHobbies() + "。");
        }

        if (!userInfo.getTargetIndustry().isEmpty()) {
            userInfoBuilder.append("我想做" + userInfo.getTargetIndustry() + "行业的工作。");
        }

        String userInfoStr = userInfoBuilder.toString();
        String userName = userInfo.getUserName();
        String aiResponse = zhiPuAIUtil.getGLMEmohaaResponse(chatMessages, userName, userInfoStr).stripTrailing();

        writeIntoDB(conversationsId, userInput, aiResponse);
        return aiResponse;
    }

    /**
     * 将聊天记录写入数据库
     *
     * @param conversationsId 会话ID
     * @param userInput 用户输入
     * @param content AI响应内容
     */
    private void writeIntoDB(Long conversationsId, String userInput, String content) {
        Messages messagesAfterAI = new Messages();
        messagesAfterAI.setConversationId(conversationsId);
        messagesAfterAI.setAiMessage(content);
        messagesAfterAI.setUserMessage(userInput);
        messagesAfterAI.setCreatedTime(LocalDateTime.now());
        chatMapper.addMessage(messagesAfterAI);
    }
}
