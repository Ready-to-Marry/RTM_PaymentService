package ready_to_marry.paymentService.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ready_to_marry.paymentService.common.dto.ApiResponse;
import ready_to_marry.paymentService.common.dto.ErrorDetail;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // PaymentException을 처리하는 핸들러
    @ExceptionHandler(PaymentException.class)
    public ResponseEntity<ApiResponse<Void>> handlePaymentException(PaymentException ex) {
        // 비즈니스 오류의 경우
        ErrorDetail errorDetail = new ErrorDetail("payment", ex.getMessage());
        return ResponseEntity
                .badRequest()
                .body(ApiResponse.failure(1, ex.getMessage(), new ErrorDetail[]{errorDetail}));
    }

    // 일반적인 예외를 처리하는 핸들러
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGeneralException(Exception ex) {
        // 일반적인 예외의 경우
        ErrorDetail errorDetail = new ErrorDetail("general", "서버 오류가 발생했습니다.");
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.failure(1, "서버 오류가 발생했습니다.", new ErrorDetail[]{errorDetail}));
    }
}