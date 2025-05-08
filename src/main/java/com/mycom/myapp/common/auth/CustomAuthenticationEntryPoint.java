package com.mycom.myapp.common.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycom.myapp.common.ResponseDTO;
import com.mycom.myapp.common.enums.ResponseCode;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException, ServletException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value()); // 401 상태 코드
        response.setContentType("application/json; charset=UTF-8");

        ResponseDTO<String> errorResponse = ResponseDTO.error(ResponseCode.UNAUTHORIZED, authException.getMessage());
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}

