package com.maiqu.career_soul.javabean.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Register {
    private Long registerId;
    private String userName;
    private String password;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;
}
