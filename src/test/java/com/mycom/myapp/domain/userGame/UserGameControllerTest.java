package com.mycom.myapp.domain.userGame;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycom.myapp.common.auth.JwtTokenProvider;
import com.mycom.myapp.common.entity.User;
import com.mycom.myapp.common.enums.ResponseCode;

@ExtendWith(MockitoExtension.class)
//@WebMvcTest(UserGameController.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UserGameControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @MockitoBean
    private UserGameService userGameService;

    @MockitoBean
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private ObjectMapper objectMapper;

    private User mockUser;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();

        // Setup mock user
        mockUser = new User();
        mockUser.setId(1L);
        // Email 설정 (username 대신)
        mockUser.setEmail("test@example.com");

        // Configure JwtTokenProvider mock
        when(jwtTokenProvider.getUserFromSecurityContext()).thenReturn(mockUser);
    }

    @Test
    @DisplayName("Should participate in game successfully")
    @WithMockUser
    void participateGameSuccess() throws Exception {
        // Given
        Long gameId = 123L;
        doNothing().when(userGameService).participateGame(gameId);

        // When & Then
        mockMvc.perform(post("/api/user-game/{gameId}/participate", gameId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(ResponseCode.SUCCESS.toString()));

        // Verify service was called with correct parameter
        verify(userGameService, times(1)).participateGame(gameId);
    }

    @Test
    @DisplayName("Should return error when user is already participating")
    @WithMockUser
    void participateGameAlreadyParticipating() throws Exception {
        // Given
        Long gameId = 123L;
        doThrow(new IllegalStateException("이미 신청한 게임입니다."))
                .when(userGameService).participateGame(gameId);

        // When & Then
        mockMvc.perform(post("/api/user-game/{gameId}/participate", gameId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isInternalServerError());

        // Verify service was called
        verify(userGameService, times(1)).participateGame(gameId);
    }

    @Test
    @DisplayName("Should return error when game not found")
    @WithMockUser
    void participateGameNotFound() throws Exception {
        // Given
        Long gameId = 999L;
        doThrow(new com.mycom.myapp.common.error.exceptions.NotFoundException(ResponseCode.NOT_FOUND_GAME))
                .when(userGameService).participateGame(gameId);

        // When & Then
        mockMvc.perform(post("/api/user-game/{gameId}/participate", gameId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());

        // Verify service was called
        verify(userGameService, times(1)).participateGame(gameId);
    }

    @Test
    @DisplayName("Should return unauthorized when no authentication")
    void participateGameUnauthorized() throws Exception {
        // Given
        Long gameId = 123L;

        // When & Then
        mockMvc.perform(post("/api/user-game/{gameId}/participate", gameId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized());

        // Verify service was never called
        verify(userGameService, never()).participateGame(anyLong());
    }
}