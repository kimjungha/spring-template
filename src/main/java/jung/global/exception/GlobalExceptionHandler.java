package jung.global.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import jung.global.error.CommonErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(JsonProcessingException.class)
    public ResponseEntity<ErrorResponse> handleJsonProcessingException(JsonProcessingException e) {
        log.warn(e.getMessage(), e);
        ErrorResponse errorResponse = ErrorResponse.from(CommonErrorCode.JSON_CONVERT);
        return ResponseEntity
                .badRequest()
                .body(errorResponse);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException e){
        log.warn(e.getMessage(), e);
        ErrorResponse errorResponse = ErrorResponse.from(e.getErrorCode());
        return ResponseEntity
                .status(e.getErrorCode().getHttpStatus())
                .body(errorResponse);

    }

     // 403 Forbidden - 권한이 없는 경우
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException e) {
        log.warn(e.getMessage(), e);
        ErrorResponse errorResponse = ErrorResponse.from(CommonErrorCode.ACCESS_DENIED);
        return ResponseEntity
                .status(CommonErrorCode.ACCESS_DENIED.getHttpStatus())
                .body(errorResponse);
    }

    // 유효성 검증 실패 (DTO @Valid)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.warn(e.getMessage(), e);
        ErrorResponse errorResponse = ErrorResponse.from(CommonErrorCode.INVALID_NOT_VALID);
        return ResponseEntity
                .badRequest()
                .body(errorResponse);
    }
}
