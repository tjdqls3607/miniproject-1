package com.mycom.myapp.common.error.handler;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import jakarta.servlet.http.HttpServletRequest;
import com.mycom.myapp.common.ResponseDTO;
import com.mycom.myapp.common.enums.ResponseCode;
import com.mycom.myapp.common.error.exceptions.BaseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class GlobalExceptionHandler {
    private final ObjectMapper objectMapper;

    // validation 관련
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ResponseDTO> handleValidationException(MethodArgumentNotValidException ex) {
        BindingResult bindingResult = ex.getBindingResult();

        // BindingResult에서 오류 메시지들 추출
        List<String> errorMessages = bindingResult.getAllErrors().stream()
                .map(ObjectError::getDefaultMessage)
                .collect(Collectors.toList());

        // 오류가 발생하면, ResponseDTO에 오류 메시지들을 담아 응답
        return ResponseEntity.status(400).body(ResponseDTO.error(ResponseCode.INVALID_REQUEST, errorMessages));
    }

    // HttpMessageNotReadableException
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ResponseDTO<Void>> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        // 원인이 InvalidFormatException인지 검사
        if (e.getCause() instanceof InvalidFormatException) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ResponseDTO.error(ResponseCode.INVALID_PARSING_INPUT));
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ResponseDTO.error(ResponseCode.INVALID_REQUEST));
    }

    // custom exception 관련
    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ResponseDTO<Void>> handleBaseException(BaseException e) {
        return ResponseEntity.status(e.getHttpStatus()) // ✅ 예외의 HTTP 상태 코드 적용
                .body(new ResponseDTO<>(e.getResponseCode().getCode(), e.getMessage(), null, null));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ResponseDTO<Void>> handleAccessDenied(AccessDeniedException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(ResponseDTO.error(ResponseCode.FORBIDDEN));
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ResponseDTO<Void>> handleAuthentication(AuthenticationException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ResponseDTO.error(ResponseCode.UNAUTHORIZED));
    }

    // server error
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseDTO<Void>> handleServerError(Exception e) {
        int httpCode = 500;
        String error = e.getMessage();

        return ResponseEntity.status(httpCode)
                .body(ResponseDTO.error(e.getClass().getSimpleName(), error));
    }
}
