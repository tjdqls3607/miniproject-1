package com.mycom.myapp.common.auth.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@Builder
public class LoginRequest {
    private String email;
    private String password;
}
