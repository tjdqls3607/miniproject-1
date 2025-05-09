package com.mycom.myapp.domain.userGame;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mycom.myapp.common.entity.UserGame;

public interface UserGameRepository extends JpaRepository<UserGame, Long> {

	boolean existsByUserIdAndGameId(Long userId, Long gameId);
}
