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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
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

import com.mycom.myapp.common.auth.JwtTokenProvider;
import com.mycom.myapp.common.entity.User;
import com.mycom.myapp.common.enums.ResponseCode;
import com.mycom.myapp.common.error.exceptions.NotFoundException;

@ExtendWith(MockitoExtension.class)
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
    
    private User mockUser;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();

        // user 설정
        mockUser = new User();
        mockUser.setId(1L);
        mockUser.setEmail("test@example.com");
        
        // mock JWK 설정
        when(jwtTokenProvider.getUserFromSecurityContext()).thenReturn(mockUser);
    }

    @Test
    @DisplayName("경기 참여 성공")
    @WithMockUser
    void participateGameSuccess() throws Exception {
        Long gameId = 1L;
        doNothing().when(userGameService).participateGame(gameId);

        mockMvc.perform(post("/api/user-game/{gameId}/participate", gameId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(ResponseCode.SUCCESS.toString()));

        verify(userGameService, times(1)).participateGame(gameId);
    }

    @Test
    @DisplayName("이미 참여한 경기")
    @WithMockUser
    void participateGameAlreadyParticipating() throws Exception {
        Long gameId = 1L;
        doThrow(new IllegalStateException("이미 신청한 게임입니다."))
                .when(userGameService).participateGame(gameId);

        mockMvc.perform(post("/api/user-game/{gameId}/participate", gameId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());

        verify(userGameService, times(1)).participateGame(gameId);
    }

    @Test
    @DisplayName("존재하지 않은 경기")
    @WithMockUser
    void participateGameNotFound() throws Exception {
        Long gameId = 1L;
        doThrow(new NotFoundException(ResponseCode.NOT_FOUND_GAME))
                .when(userGameService).participateGame(gameId);

        mockMvc.perform(post("/api/user-game/{gameId}/participate", gameId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(userGameService, times(1)).participateGame(gameId);
    }

    @Test
    @DisplayName("제한된 user")
    void participateGame_Unauthorized() throws Exception {
        Long gameId = 1L;

        mockMvc.perform(post("/api/user-game/{gameId}/participate", gameId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());

        verify(userGameService, never()).participateGame(anyLong());
    }
}