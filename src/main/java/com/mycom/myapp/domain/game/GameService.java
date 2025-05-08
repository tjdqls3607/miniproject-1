package com.mycom.myapp.domain.game;

import com.mycom.myapp.common.entity.Game;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class GameService {
    private final GameRepository gameRepository;

    public void save(Game game) {
        gameRepository.save(game);
    }
}
