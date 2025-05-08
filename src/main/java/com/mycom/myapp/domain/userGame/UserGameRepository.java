package com.mycom.myapp.domain.userGame;

import com.mycom.myapp.common.entity.UserGame;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserGameRepository extends JpaRepository<UserGame, Long> {
}
