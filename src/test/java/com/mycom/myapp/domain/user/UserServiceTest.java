package com.mycom.myapp.domain.user;

import com.mycom.myapp.common.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void getUserById_ValidId_ReturnsUser() {
        // given
        Long userId = 1L;
        User user = new User();
        user.setId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // when
        Optional<User> result = userService.getUserById(userId);

        // then
        assertTrue(result.isPresent());
        assertEquals(userId, result.get().getId());
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void getUserById_NullId_ThrowsException() {
        // given
        Long userId = null;

        // then
        assertThrows(IllegalArgumentException.class, () -> userService.getUserById(userId));
    }

    @Test
    void findByEmail_ReturnsUserOptional() {
        // given
        String email = "test@example.com";
        User user = new User();
        user.setEmail(email);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        // when
        Optional<User> result = userService.findByEmail(email);

        // then
        assertTrue(result.isPresent());
        assertEquals(email, result.get().getEmail());
        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    void findByNickname_ReturnsUserOptional() {
        // given
        String nickname = "tester";
        User user = new User();
        user.setNickname(nickname);

        when(userRepository.findByNickname(nickname)).thenReturn(Optional.of(user));

        // when
        Optional<User> result = userService.findByNickname(nickname);

        // then
        assertTrue(result.isPresent());
        assertEquals(nickname, result.get().getNickname());
        verify(userRepository, times(1)).findByNickname(nickname);
    }

    @Test
    void save_CallsRepositorySave() {
        // given
        User user = new User();
        user.setEmail("save@test.com");

        // when
        userService.save(user);

        // then
        verify(userRepository, times(1)).save(user);
    }
}