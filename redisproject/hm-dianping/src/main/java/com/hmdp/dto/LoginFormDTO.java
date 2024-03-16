package com.hmdp.dto;

import lombok.Data;

@Data
public class LoginFormDTO {
    public Object session;
    private String phone;
    private String code;
    private String password;
}
