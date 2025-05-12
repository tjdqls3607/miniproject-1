package com.mycom.myapp.domain.userGame;

import com.mycom.myapp.common.entity.Game;
import com.mycom.myapp.common.entity.User;
import com.mycom.myapp.common.entity.UserGame;
import com.mycom.myapp.common.enums.MatchStatus;
import com.mycom.myapp.domain.game.GameRepository;
import org.springframework.stereotype.Service;

import com.mycom.myapp.common.auth.JwtTokenProvider;
import com.mycom.myapp.common.entity.Game;
import com.mycom.myapp.common.entity.User;
import com.mycom.myapp.common.entity.UserGame;
import com.mycom.myapp.common.enums.MatchStatus;
import com.mycom.myapp.common.enums.ResponseCode;
import com.mycom.myapp.common.error.exceptions.NotFoundException;
import com.mycom.myapp.domain.game.GameRepository;
import com.mycom.myapp.domain.user.UserRepository;
import com.mycom.myapp.domain.userGame.UserGameDto;
import com.mycom.myapp.domain.userGame.UserGameRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
public class UserGameService {

    private final GameRepository gameRepository;
    private final UserRepository userRepository;
    private final UserGameRepository userGameRepository;

    // ✅ 주최자가 매칭 삭제
    @Transactional
    public void deleteMatch(Long gameId, Long userId) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new RuntimeException("게임이 존재하지 않습니다."));

        if (!game.getHost().getId().equals(userId)) {
            throw new RuntimeException("삭제 권한이 없습니다.");
        }

        game.softDelete(); // 소프트 삭제

        List<UserGame> participants = userGameRepository.findByGame(game);
        for (UserGame ug : participants) {
            ug.setMatchStatus(MatchStatus.CANCELLED);
        }
    }

    // ✅ 참가자가 매칭 취소
    @Transactional
    public void cancelParticipation(Long gameId, Long userId) {
        System.out.println("▶ [참가 취소 요청] userId=" + userId + ", gameId=" + gameId);

        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new RuntimeException("❌ 게임이 존재하지 않습니다. gameId=" + gameId));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("❌ 유저가 존재하지 않습니다. userId=" + userId));

        UserGame userGame = userGameRepository.findByUserAndGame(user, game)
                .orElseThrow(() -> new RuntimeException("❌ 해당 유저는 이 매칭에 참가하지 않았습니다."));

        userGame.setMatchStatus(MatchStatus.CANCELLED);
        System.out.println("✅ [참가 취소 완료] userGameId=" + userGame.getId());
    }

    // ✅ 내가 참가한 매칭 목록 조회 (DTO로 직접 조회)
    public List<UserGameDto> getMyParticipations(Long userId, MatchStatus status, LocalDateTime after, LocalDateTime before) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("유저가 존재하지 않습니다."));

        List<UserGameDto> dtos = (status != null)
                ? userGameRepository.findDtoByUserAndMatchStatus(user, status)
                : userGameRepository.findDtoByUser(user);

        // 날짜 필터
        if (after != null) {
            dtos = dtos.stream()
                    .filter(dto -> dto.getTime() != null && dto.getTime().isAfter(after))
                    .toList();
        }
        if (before != null) {
            dtos = dtos.stream()
                    .filter(dto -> dto.getTime() != null && dto.getTime().isBefore(before))
                    .toList();
        }

        return dtos;
    }

    // ✅ 내가 주최한 매칭 목록 조회 (그대로 사용 가능)
    public List<UserGameDto> getMyCreatedMatches(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("유저가 존재하지 않습니다."));

        List<Game> games = gameRepository.findByHost(user);

        return games.stream()
                .map(g -> new UserGameDto(g.getId(), g.getLocation(), g.getTime(), g.getAgainstPeople()))
                .toList();
    }
    private final JwtTokenProvider jwtTokenProvider;

    public void participateGame(Long gameId) {
    	User user = jwtTokenProvider.getUserFromSecurityContext();
//    	User user = userRepository.findById(11L)
//    			.orElseThrow(() -> new NotFoundException(ResponseCode.NOT_FOUND_USER));
    	Long userId = user.getId();
    	Game game = gameRepository.findById(gameId)
    			.orElseThrow(() -> new NotFoundException(ResponseCode.NOT_FOUND_GAME));

    	if(userGameRepository.existsByUserIdAndGameId(11L, gameId)) {
    		throw new IllegalStateException("이미 신청한 게임입니다.");
    	}

    	UserGame userGame = UserGame.builder()
    			.user(user)
    			.game(game)
    			.matchStatus(MatchStatus.COMPLETED)
    			.build();

    	userGameRepository.save(userGame);
    }
}
