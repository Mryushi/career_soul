package com.maiqu.career_soul.javabean.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Conversations {

    private Long conversationId;
    private Long userId;
    private String agentName;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
