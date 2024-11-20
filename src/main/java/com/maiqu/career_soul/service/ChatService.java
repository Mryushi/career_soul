package com.maiqu.career_soul.service;

import com.maiqu.career_soul.javabean.dto.ChatRequest;

public interface ChatService {

    /**
     * 获取AI回复
     * @param chatRequest
     * @return
     */
    String getResponse(ChatRequest chatRequest);

    /**
     * 增加新一轮会话
     * @param userId
     * @param agentName
     * @return
     */
    Long addConversation(Long userId, String agentName);
}
