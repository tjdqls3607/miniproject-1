package com.mycom.myapp.domain.userGame;

import com.mycom.myapp.common.auth.JwtTokenProvider;
import com.mycom.myapp.common.entity.User;
import com.mycom.myapp.common.enums.MatchStatus;
import com.mycom.myapp.domain.userGame.UserGameDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mycom.myapp.common.enums.ResponseCode;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user-game")
@Slf4j
public class UserGameController {

    private final UserGameService userGameService;
    private final JwtTokenProvider jwtTokenProvider;

    // 매칭 삭제 (주최자용)
//    @DeleteMapping("/{gameId}")
//    public String deleteMatch(@PathVariable Long gameId, @RequestParam Long userId) {
//        userGameService.deleteMatch(gameId, userId);
//        return "매칭이 삭제되었습니다.";
//    }

    // 매칭 삭제 (주최자용)
    @DeleteMapping("/{gameId}")
    public ResponseEntity<?> deleteMatch(@PathVariable Long gameId) {
        userGameService.deleteMatch(gameId);
        return ResponseEntity.ok("매칭이 취소되었습니다.");
    }

    // 매칭 참가 취소 (참가자용)
    @PostMapping("/{gameId}/cancel")
    public ResponseEntity<String> cancelParticipation (@PathVariable Long gameId) {
        userGameService.cancelParticipation(gameId);
        return ResponseEntity.ok("매칭을 취소하였습니다.");
    }

//    @PostMapping("/{gameId}/cancel")
//    public String cancelParticipation(@PathVariable Long gameId, @RequestParam Long userId) {
//        userGameService.cancelParticipation(gameId, userId);
//        return "참가가 취소되었습니다.";
//    }

    // 내가 참가한 매칭 목록 조회 (DTO 리턴)
    @GetMapping("/my-participations")
    public List<UserGameDto> getMyParticipations(@RequestParam Long userId,
                                                 @RequestParam(required = false) MatchStatus status,
                                                 @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)LocalDateTime before,
                                                 @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)LocalDateTime after) {
        return userGameService.getMyParticipations(userId, status, before, after);
    }

    // 내가 주최한 매칭 목록 조회 (DTO 리턴)
    @GetMapping("/my-created")
    public List<UserGameDto> getMyCreatedMatches(@RequestParam Long userId) {
        User user = jwtTokenProvider.getUserFromSecurityContext();
        return userGameService.getMyCreatedMatches(userId);
    }


	@PostMapping("/{gameId}/participate")
	public ResponseEntity<?> participateGame(@PathVariable("gameId") Long gameId){
		log.error("gameid=" + gameId);
		userGameService.participateGame(gameId);
		return ResponseEntity.ok(ResponseCode.SUCCESS);
	}
}
