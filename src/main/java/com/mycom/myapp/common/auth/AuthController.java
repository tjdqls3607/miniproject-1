package com.mycom.myapp.common.auth;
import com.mycom.myapp.common.ResponseDTO;
import com.mycom.myapp.common.auth.dto.LoginRequest;
import com.mycom.myapp.common.auth.dto.LoginResponse;
import com.mycom.myapp.common.auth.dto.SignupRequest;
import com.mycom.myapp.common.entity.User;
import com.mycom.myapp.common.enums.ResponseCode;
import com.mycom.myapp.common.error.exceptions.BadRequestException;
import com.mycom.myapp.domain.user.UserRepository;
import com.mycom.myapp.domain.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/signup")
    public ResponseEntity<ResponseDTO<Void>> signup(@RequestBody SignupRequest request) {
        if (userService.findByEmail(request.getEmail()).isPresent()) {
            throw new BadRequestException(ResponseCode.DUPLICATED_EMAIL);
        }
        if(userService.findByNickname(request.getNickname()).isPresent()) {
            throw new BadRequestException(ResponseCode.DUPLICATED_NICKNAME);
        }
        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .nickname(request.getNickname())
                .profileImage(request.getProfileImage())
                .build();

        userService.save(user);
        return ResponseEntity.ok(ResponseDTO.success(ResponseCode.CREATED));
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseDTO<LoginResponse>> login(@RequestBody LoginRequest request) {
        System.out.println("로그인 요청 들어옴: \" + request.getEmail() + \" / \" + request.getPassword()");
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        String token = jwtTokenProvider.createToken(authentication);
        return ResponseEntity.ok(ResponseDTO.success(ResponseCode.SUCCESS, LoginResponse.builder().token(token).build()));
    }


    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(Authentication authentication) {
        String username = authentication.getName();
         if (authentication == null || !authentication.isAuthenticated()) {
             return ResponseEntity.status(401).body("인증이 필요합니다");
         }
        User user = userService.findByEmail(username).orElseThrow(() -> new RuntimeException("User not found"));
        return ResponseEntity.ok(Map.of(
                "userId", user.getId(),
                "nickname", user.getNickname()));
    }
}