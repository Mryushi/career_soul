package com.maiqu.career_soul.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Conversations {

    private Integer conversationId;
    private Integer userId;
    private Integer agentId;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
