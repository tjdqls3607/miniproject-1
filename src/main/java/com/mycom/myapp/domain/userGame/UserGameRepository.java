package com.mycom.myapp.domain.userGame;

import com.mycom.myapp.common.entity.Game;
import com.mycom.myapp.common.entity.User;
import com.mycom.myapp.common.entity.UserGame;
import com.mycom.myapp.common.enums.MatchStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserGameRepository extends JpaRepository<UserGame, Long> {
    List<UserGame> findByGame(Game game);
    List<UserGame> findByUserAndMatchStatus(User user, MatchStatus matchStatus);
    Optional<UserGame> findByUserAndGame(User user, Game game);
    List<UserGame> findByUser(User user);
}
