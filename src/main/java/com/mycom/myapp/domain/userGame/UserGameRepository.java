package com.mycom.myapp.domain.userGame;

import com.mycom.myapp.common.entity.Game;
import com.mycom.myapp.common.entity.User;
import com.mycom.myapp.common.entity.UserGame;
import com.mycom.myapp.common.enums.MatchStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

import com.mycom.myapp.common.entity.UserGame;

public interface UserGameRepository extends JpaRepository<UserGame, Long> {
    List<UserGame> findByGame(Game game);
    List<UserGame> findByUserAndMatchStatus(User user, MatchStatus matchStatus);
    Optional<UserGame> findByUserAndGame(User user, Game game);
    List<UserGame> findByUser(User user);

    @Query("""
SELECT new com.mycom.myapp.domain.userGame.UserGameDto(
    g.id, g.location, g.time, g.againstPeople)
FROM UserGame ug
JOIN ug.game g
WHERE ug.user = :user
""")
    List<UserGameDto> findDtoByUser(@Param("user") User user);

    @Query("""
SELECT new com.mycom.myapp.domain.userGame.UserGameDto(
    g.id, g.location, g.time, g.againstPeople)
FROM UserGame ug
JOIN ug.game g
WHERE ug.user = :user AND ug.matchStatus = :status
""")
    List<UserGameDto> findDtoByUserAndMatchStatus(@Param("user") User user, @Param("status") MatchStatus status);

	boolean existsByUserIdAndGameId(Long userId, Long gameId);
}
