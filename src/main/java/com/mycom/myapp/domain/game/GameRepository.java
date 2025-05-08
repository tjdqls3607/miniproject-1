package com.mycom.myapp.domain.game;

import com.mycom.myapp.common.entity.Game;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameRepository extends JpaRepository<Game, Long> {
}
