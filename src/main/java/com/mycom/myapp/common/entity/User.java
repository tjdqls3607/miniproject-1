package com.mycom.myapp.common.entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "User") // 테이블명은 PascalCase 유지
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class User extends BaseEntity{
    @Column(nullable = false, length = 50)
    private String nickname; // 닉네임

    @Column(nullable = false, unique = true, length = 100)
    private String email; // 이메일 (중복 방지를 위해 unique 설정)

    @Column(nullable = false)
    private String password; // 암호화된 패스워드

    @Column(length = 500)
    private String profileImage; // 프로필 이미지 URL

    // 권한 추가
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role")
    private List<String> roles = new ArrayList<>();

    // email admin123@example.com
    // password admin123
}
