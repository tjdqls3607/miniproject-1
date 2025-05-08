package com.mycom.myapp.domain.user;

import com.mycom.myapp.common.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
