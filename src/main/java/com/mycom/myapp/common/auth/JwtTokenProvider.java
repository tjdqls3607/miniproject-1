package com.mycom.myapp.common.auth;

import com.mycom.myapp.common.entity.User;
import com.mycom.myapp.common.enums.ResponseCode;
import com.mycom.myapp.common.error.exceptions.NotFoundException;
import com.mycom.myapp.common.error.exceptions.UnauthorizedException;
import com.mycom.myapp.domain.user.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtTokenProvider {
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String AUTH_ID = "id";

    private final String key;
    private final long accessTokenValidityMilliSeconds;
    private final UserService userService;
    public JwtTokenProvider(
            @Value("${jwt.secret_key}")
            String key,
            @Value("${jwt.access-token-validity-in-seconds}")
            long accessTokenValidityMilliSeconds,
            UserService userService) {
        this.key = key;
        this.accessTokenValidityMilliSeconds = accessTokenValidityMilliSeconds;
        this.userService = userService;
    }

    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(key);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String createAccessToken(Map<String, Object> claims,Long id) {
        long now = (new Date()).getTime();
        Date accessValidity = new Date(now + this.accessTokenValidityMilliSeconds* 1000);
        return Jwts.builder()
                .claims(claims)
                .subject(String.valueOf(id))
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(accessValidity)
                .signWith(getSignInKey(),Jwts.SIG.HS256)
                .compact();
    }

    // access Token 생성
    public String createToken(Authentication authentication) {
        CustomUserDetails oAuth2User = (CustomUserDetails) authentication.getPrincipal();

        Optional<User> user = userService.getUserById(oAuth2User.getUser().getId());
        if(user.isEmpty()) throw new NotFoundException(ResponseCode.NOT_FOUND_USER);

        Map<String, Object> claims = new HashMap<>();
        claims.put(AUTH_ID, user.get().getId());
        claims.put("roles", user.get().getRoles()); //추가
        String accessToken = createAccessToken(claims, user.get().getId());

        return accessToken;
    }

    // token이 유효한 지 검사
    public boolean validateToken(String token) throws AuthenticationException {
        try {
            Jwts.parser()
                    .verifyWith(getSignInKey())
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            throw new UnauthorizedException(ResponseCode.INVALID_TOKEN,e.getMessage());
        }
    }

    public Claims getClaimsFromToken(String token) {
        return Jwts.parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // token으로부터 Authentication 객체를 만들어 리턴하는 메소드
    public Authentication getAuthentication(String token) throws UnauthorizedException {
        try {
            Claims claims =  getClaimsFromToken(token);
            System.out.println("Claims: " + claims);
            System.out.println("AUTH_ID: " + AUTH_ID);
            System.out.println("claims.get(AUTH_ID): " + claims.get(AUTH_ID));
            if (claims == null) {
                throw new UnauthorizedException(ResponseCode.INVALID_TOKEN);
            }
            Long userId = Long.parseLong(claims.get(AUTH_ID).toString());
            System.out.println("Parsed userId: " + userId);

            Optional<User> user = userService.getUserById(userId);
            System.out.println(">>> user from getUserById: " + user);


            if (user.isEmpty()) {
                throw new UnauthorizedException(ResponseCode.INVALID_TOKEN);
            }

//            CustomUserDetails principal = new CustomUserDetails(user.get());

            // 추가
            List<String> roles = (List<String>) claims.get("roles");
            List<GrantedAuthority> authorities = roles.stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());

            CustomUserDetails principal = new CustomUserDetails(user.get());

            return new UsernamePasswordAuthenticationToken(principal, null, authorities);

        } catch (Exception e) {
            throw new UnauthorizedException(ResponseCode.INVALID_TOKEN);
        }
    }

    public User getUserFromSecurityContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails principalUserDetail = null;
        Optional<User> user;
        if (authentication != null) {
            principalUserDetail = (CustomUserDetails) authentication.getPrincipal();
        }
        if(principalUserDetail==null) throw new UnauthorizedException(ResponseCode.INVALID_TOKEN);

        user = userService.getUserById(principalUserDetail.getUser().getId());
        if(user.isEmpty()) throw new UnauthorizedException(ResponseCode.INVALID_TOKEN);

        return user.get();
    }
}