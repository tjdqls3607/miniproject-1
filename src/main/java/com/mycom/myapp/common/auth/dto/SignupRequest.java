package com.mycom.myapp.common.auth.dto;

import lombok.Getter;

@Getter
public class SignupRequest {
    private String email;
    private String password;
    private String nickname;
    private String profileImage;
}