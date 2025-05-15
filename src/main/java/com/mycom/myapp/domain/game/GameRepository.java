package com.mycom.myapp.domain.game;

import com.mycom.myapp.common.entity.Game;
import com.mycom.myapp.common.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GameRepository extends JpaRepository<Game, Long> {
    List<Game> findByHost(User host);
    List<Game> findByLocationLike(String location);
}
