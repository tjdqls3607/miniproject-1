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
import com.mycom.myapp.domain.code.CodeService;
import com.mycom.myapp.domain.game.dto.GameDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class GameService {

	private final GameRepository gameRepository;
	private final CodeService codeService;

	public Game save(Game game) {
		return gameRepository.save(game);
	}

	public List<GameDto> listGame() {
		List<Game> games = gameRepository.findAll();
		List<GameDto> gamesDtoList = new ArrayList<>();

		games.forEach(game -> {
			if (game.getDeadline().isAfter(LocalDateTime.now()) && game.getTime().isAfter(LocalDateTime.now())) {
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
		Optional<Game> gameOptional = gameRepository.findById(gameId);

		return gameOptional.map(game -> {
			GameDto gameDto = new GameDto();
			gameDto.setId(game.getId());
			gameDto.setLocation(game.getLocation());
			gameDto.setTime(game.getTime());
			gameDto.setDeadline(game.getDeadline());
			gameDto.setParticipantMin(game.getParticipantMin());
			gameDto.setParticipantMax(game.getParticipantMax());
			gameDto.setAgainstPeople(game.getAgainstPeople());
			gameDto.setGameNoti(game.getGameNoti());

			// ✅ gameOptions를 코드 이름으로 변환 후 gameInfo에 세팅
			if (game.getGameOptions() != null) {
				List<String> codeList = List.of(game.getGameOptions().split(","));
				List<String> codeNames = codeList.stream()
						.map(code -> codeService.getCodeName("050", code))
						.toList();
				gameDto.setGameInfo(String.join(", ", codeNames));
			} else {
				gameDto.setGameInfo(null);
			}

			return gameDto;
		}).orElseThrow(() -> new NotFoundException(ResponseCode.NOT_FOUND_GAME));
	}

	public List<GameDto> searchGameLocation(String location) {
		List<Game> games = gameRepository.findByLocationLike("%" + location + "%");
		List<GameDto> gamesDtoList = new ArrayList<>();

		games.forEach(game -> {
			if (game.getDeadline().isAfter(LocalDateTime.now()) && game.getTime().isAfter(LocalDateTime.now())) {
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
}