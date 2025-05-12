package com.mycom.myapp.domain.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycom.myapp.common.ResponseDTO;
import com.mycom.myapp.common.auth.AuthController;
import com.mycom.myapp.common.auth.JwtTokenProvider;
import com.mycom.myapp.common.auth.dto.LoginRequest;
import com.mycom.myapp.common.auth.dto.LoginResponse;
import com.mycom.myapp.common.auth.dto.SignupRequest;
import com.mycom.myapp.common.entity.User;
import com.mycom.myapp.common.enums.ResponseCode;
import com.mycom.myapp.common.error.exceptions.BadRequestException;
import com.mycom.myapp.domain.user.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthContollerTest {

    @InjectMocks
    private AuthController authController;

    @Mock
    private UserService userService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("회원가입 성공")
    void testSignupSuccess() {
        SignupRequest request = SignupRequest.builder()
                .email("test@example.com")
                .password("password123")
                .nickname("tester")
                .profileImage("profile.jpg")
                .build();

        when(userService.findByEmail("test@example.com")).thenReturn(Optional.empty());
        when(userService.findByNickname("tester")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password123")).thenReturn("encoded-password");

        ResponseEntity<ResponseDTO<Void>> response = authController.signup(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(ResponseCode.CREATED.getCode(), response.getBody().getCode());

        verify(userService).save(any(User.class));
    }

    @Test
    @DisplayName("회원가입 실패 - 중복 이메일")
    void testSignupFail_DuplicatedEmail() {
        SignupRequest request = SignupRequest.builder()
                .email("test@example.com")
                .password("password123")
                .nickname("tester")
                .profileImage("profile.jpg")
                .build();

        when(userService.findByEmail("test@example.com")).thenReturn(Optional.of(mock(User.class)));

        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            authController.signup(request);
        });

        assertEquals(ResponseCode.DUPLICATED_EMAIL.getCode(), exception.getResponseCode().getCode());
    }

    @Test
    @DisplayName("회원가입 실패 - 중복 닉네임")
    void testSignupFail_DuplicatedNickname() {
        SignupRequest request = SignupRequest.builder()
                .email("test@example.com")
                .password("password123")
                .nickname("tester")
                .profileImage("profile.jpg")
                .build();

        when(userService.findByEmail("test@example.com")).thenReturn(Optional.empty());
        when(userService.findByNickname("tester")).thenReturn(Optional.of(mock(User.class)));

        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            authController.signup(request);
        });

        assertEquals(ResponseCode.DUPLICATED_NICKNAME.getCode(), exception.getResponseCode().getCode());
    }

    @Test
    @DisplayName("로그인 성공")
    void testLoginSuccess() {
        LoginRequest request = LoginRequest.builder()
                .email("test@example.com")
                .password("password123")
                .build();

        Authentication auth = mock(Authentication.class);
        when(authenticationManager.authenticate(any())).thenReturn(auth);
        when(jwtTokenProvider.createToken(auth)).thenReturn("mock-token");

        ResponseEntity<ResponseDTO<LoginResponse>> response = authController.login(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        LoginResponse loginResponse = ((ResponseDTO<LoginResponse>) response.getBody()).getData();
        assertEquals(ResponseCode.SUCCESS.getCode(), Objects.requireNonNull(response.getBody()).getCode());
        assertNotNull(loginResponse);
        assertNotNull(loginResponse.getToken());
        assertFalse(loginResponse.getToken().isEmpty());
    }
}
