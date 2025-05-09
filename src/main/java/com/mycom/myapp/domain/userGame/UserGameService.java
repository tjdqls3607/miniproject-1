package com.mycom.myapp.domain.userGame;

import com.mycom.myapp.common.entity.Game;
import com.mycom.myapp.common.entity.User;
import com.mycom.myapp.common.entity.UserGame;
import com.mycom.myapp.common.enums.MatchStatus;
import com.mycom.myapp.domain.game.GameRepository;
import com.mycom.myapp.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

        // 소프트 삭제
        game.softDelete();

        // 이 매칭에 참여한 유저들의 상태도 비활성화
        List<UserGame> participants = userGameRepository.findByGame(game);
        for (UserGame ug : participants) {
            ug.setMatchStatus(MatchStatus.CANCELLED);
        }
    }

    // ✅ 참가자가 매칭 취소
    @Transactional
    public void cancelParticipation(Long gameId, Long userId) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new RuntimeException("게임이 존재하지 않습니다."));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("유저가 존재하지 않습니다."));

        UserGame userGame = userGameRepository.findByUserAndGame(user, game)
                .orElseThrow(() -> new RuntimeException("참여 내역이 없습니다."));

        userGame.setMatchStatus(MatchStatus.CANCELLED);
    }

    // ✅ 내가 참가한 매칭 목록 조회 (DTO로 변환)
    public List<UserGameDto> getMyParticipations(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("유저가 존재하지 않습니다."));

        List<UserGame> participations = userGameRepository.findByUserAndMatchStatus(user, MatchStatus.COMPLETED);

        return participations.stream()
                .map(ug -> {
                    Game g = ug.getGame();
                    return new UserGameDto(g.getId(), g.getLocation(), g.getTime(), g.getAgainstPeople());
                })
                .toList();
    }

    // ✅ 내가 주최한 매칭 목록 조회 (DTO로 변환)
    public List<UserGameDto> getMyCreatedMatches(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("유저가 존재하지 않습니다."));

        List<Game> games = gameRepository.findByHost(user);

        return games.stream()
                .map(g -> new UserGameDto(g.getId(), g.getLocation(), g.getTime(), g.getAgainstPeople()))
                .toList();
    }
}
