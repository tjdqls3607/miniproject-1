package com.mycom.myapp.domain.game;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.mycom.myapp.common.entity.Game;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class GameService {
    private final GameRepository gameRepository;
    
    public void save(Game game) {
        gameRepository.save(game);
    }
    
    public List<GameDto> listGame() {
    	List<Game> games = gameRepository.findAll();
    	
        
    	List<GameDto> gamesDtoList = new ArrayList<>();
    	games.forEach( game -> {
    		GameDto gameDto = GameDto.builder()
    				.location(game.getLocation())
    				.time(game.getTime())
    				.deadline(game.getDeadline())
    				.participantMin(game.getParticipantMin())
    				.participantMax(game.getParticipantMax())
    				.againstPeople(game.getAgainstPeople())
    				.gameInfo(game.getGameInfo())
    				.gameNoti(game.getGameNoti())
    				.build();
    		gamesDtoList.add(gameDto);
    		
    	});
    	return gamesDtoList;
    }
}
