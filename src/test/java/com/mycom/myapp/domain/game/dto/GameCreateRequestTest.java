package com.mycom.myapp.domain.game.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
@ExtendWith(MockitoExtension.class)
public class GameCreateRequestTest {
    private static Validator validator;

    @BeforeAll
    static void setupValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testValidGameCreateRequest() {
        GameCreateRequest request = GameCreateRequest.builder()
                .location("서울 월드컵 경기장")
                .time(LocalDateTime.now().plusDays(1))
                .deadline(LocalDateTime.now().plusHours(12))
                .participantMin(5)
                .participantMax(10)
                .againstPeople("상대 설명")
                .gameInfo("게임 정보")
                .gameNoti("공지")
                .build();

        Set<ConstraintViolation<GameCreateRequest>> violations = validator.validate(request);
        assertTrue(violations.isEmpty(), "Validation should pass with valid fields.");
    }

    @Test
    void testInvalidGameCreateRequest_missingFields() {
        GameCreateRequest request = GameCreateRequest.builder()
                .location(" ")  // NotBlank 위반
                .time(null)     // NotNull 위반
                .deadline(null) // NotNull 위반
                .participantMin(0) // NotNull OK, but 0이 괜찮은 값인지 확인 필요
                .participantMax(10)
                .againstPeople("a".repeat(501)) // 500자 초과
                .gameInfo("a".repeat(1001))     // 1000자 초과
                .gameNoti("a".repeat(501))      // 500자 초과
                .build();

        Set<ConstraintViolation<GameCreateRequest>> violations = validator.validate(request);

        assertFalse(violations.isEmpty(), "Validation should fail for missing/invalid fields.");

        // 출력 확인용
        violations.forEach(v -> System.out.println(v.getPropertyPath() + " -> " + v.getMessage()));
    }
}
