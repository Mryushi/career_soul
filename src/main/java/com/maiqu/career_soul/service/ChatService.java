package com.maiqu.career_soul.service;

import com.maiqu.career_soul.javabean.dto.ChatRequest;

public interface ChatService {

    String getResponse(ChatRequest chatRequest);

    Long addConversation(Long userId, String agentName);
}
