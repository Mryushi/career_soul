package com.maiqu.career_soul.javabean.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatRequest {
    private Long userId;
    private String agentName;
    private Long conversationsId;
    private String userInput;
}
