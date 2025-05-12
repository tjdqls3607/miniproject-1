package com.mycom.myapp.common.auth.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@Builder
public class SignupRequest {
    private String email;
    private String password;
    private String nickname;
    private String profileImage;
}