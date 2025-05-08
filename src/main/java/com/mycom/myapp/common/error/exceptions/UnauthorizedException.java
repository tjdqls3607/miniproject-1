package com.mycom.myapp.common.error.exceptions;

import com.mycom.myapp.common.enums.ResponseCode;
import org.springframework.security.core.AuthenticationException;


public class UnauthorizedException extends BaseException {

    public UnauthorizedException(ResponseCode responseCode) {
        super(responseCode);
    }

    public UnauthorizedException(ResponseCode responseCode, String message) {
        super(responseCode, message);
    }

    public AuthenticationException toAuthenticationException() {
        return new AuthenticationException(getMessage()) {}; // 익명 클래스로 변환
    }
}
