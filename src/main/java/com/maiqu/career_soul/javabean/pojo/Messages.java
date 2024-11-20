package com.maiqu.career_soul.javabean.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Messages {

    private Long messageId;
    private Long conversationId;
    private String userMessage;
    private String aiMessage;
    private LocalDateTime createdTime;

}
