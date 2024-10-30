package com.maiqu.career_soul.service;

public interface TestService {

    String getIQResponse(Integer conversationsId, String userInput);

    Integer addConversation(Integer userId, Integer agentId);
}
