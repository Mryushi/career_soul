package com.maiqu.career_soul.javabean.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRegister {
    private Long registerId;
    private String tel;
    private String userName;
    private String verifiCode;
    private LocalDate createdTime;
    private LocalDate updatedTime;
}
