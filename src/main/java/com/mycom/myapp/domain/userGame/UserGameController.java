package com.mycom.myapp.domain.userGame;

import com.mycom.myapp.domain.userGame.UserGameDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user-game")
@Slf4j
public class UserGameController {

    private final UserGameService userGameService;

    // ✅ 매칭 삭제 (주최자용)
    @DeleteMapping("/{gameId}")
    public String deleteMatch(@PathVariable Long gameId, @RequestParam Long userId) {
        userGameService.deleteMatch(gameId, userId);
        return "매칭이 삭제되었습니다.";
    }

    // ✅ 매칭 참가 취소 (참가자용)
    @PostMapping("/{gameId}/cancel")
    public String cancelParticipation(@PathVariable Long gameId, @RequestParam Long userId) {
        userGameService.cancelParticipation(gameId, userId);
        return "참가가 취소되었습니다.";
    }

    // ✅ 내가 참가한 매칭 목록 조회 (DTO 리턴)
    @GetMapping("/my-participations")
    public List<UserGameDto> getMyParticipations(@RequestParam Long userId) {
        return userGameService.getMyParticipations(userId);
    }

    // ✅ 내가 주최한 매칭 목록 조회 (DTO 리턴)
    @GetMapping("/my-created")
    public List<UserGameDto> getMyCreatedMatches(@RequestParam Long userId) {
        return userGameService.getMyCreatedMatches(userId);
    }
}
