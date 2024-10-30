package com.maiqu.career_soul.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Messages {

    private Integer messageId;
    private Integer conversationId;
    private String userMessage;
    private String aiMessage;
}
