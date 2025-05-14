package com.mycom.myapp.domain.game;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.mycom.myapp.common.entity.Game;
import com.mycom.myapp.common.enums.ResponseCode;
import com.mycom.myapp.common.error.exceptions.NotFoundException;
import com.mycom.myapp.domain.game.dto.GameDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class GameService {
	private final GameRepository gameRepository;

	public Game save(Game game) {
		return gameRepository.save(game);
	}

	public List<GameDto> listGame() {
		List<Game> games = gameRepository.findAll();


		List<GameDto> gamesDtoList = new ArrayList<>();
		games.forEach( game -> {

			if(game.getDeadline().isAfter(LocalDateTime.now()) && game.getTime().isAfter(LocalDateTime.now())) { // 마감시간, 시작시간 지나지 않은 것만
				GameDto gameDto = GameDto.builder()
						.id(game.getId())
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
			}

		});

		gamesDtoList.sort(Comparator.comparing(GameDto::getTime));

		return gamesDtoList;
	}

	public GameDto detailGame(long gameId) {
		GameDto gameDto = new GameDto();

		Optional<Game> gameOptional = gameRepository.findById(gameId);
		gameOptional.ifPresentOrElse(
				game -> {
					gameDto.setId(game.getId());
					gameDto.setLocation(game.getLocation());
					gameDto.setTime(game.getTime());
					gameDto.setDeadline(game.getDeadline());
					gameDto.setParticipantMin(game.getParticipantMin());
					gameDto.setParticipantMax(game.getParticipantMax());
					gameDto.setAgainstPeople(game.getAgainstPeople());
					gameDto.setGameInfo(game.getGameInfo());
					gameDto.setGameNoti(game.getGameNoti());
				},
				() -> {
					throw new NotFoundException(ResponseCode.NOT_FOUND_GAME);
				}

		);

		return gameDto;
	}
}