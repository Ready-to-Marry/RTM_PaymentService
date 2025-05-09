package ready_to_marry.paymentService.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ready_to_marry.paymentService.exception.ApiResponse;
import ready_to_marry.paymentService.exception.payment.PaymentException;
import ready_to_marry.paymentService.service.PaymentService;

import java.io.IOException;

@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/verify")
    public ResponseEntity<ApiResponse<Void>> verifyPayment(
            @RequestParam String paymentId,
            @RequestParam Long productId,
            @RequestParam Long memberId
    ) {
        try {
            // 결제 검증 처리
            paymentService.verifyPayment(paymentId, productId, memberId);

            // 성공적인 경우, 결제가 완료되었음을 알려주는 응답
            return ResponseEntity.ok(ApiResponse.success("결제가 완료되었습니다.", null));
        } catch (PaymentException e) {
            // 결제 관련 예외가 발생한 경우
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.fail(e.getMessage()));
        } catch (Exception e) {
            // 예상치 못한 예외가 발생한 경우
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.fail("서버 오류가 발생했습니다."));
        }
    }

    @DeleteMapping("/cancel/{id}")
    public ResponseEntity<ApiResponse<Void>> cancelPayment(
            @PathVariable Long id,
            @RequestParam Long userId) {
        try {
            paymentService.canclePayment(id, userId);
            return ResponseEntity.ok(new ApiResponse<>(true, "결제가 성공적으로 취소되었습니다.", null));
        } catch (PaymentException e) {
            // 실패 응답 처리
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.fail(e.getMessage()));  // ApiResponse.failure() 사용
        } catch (Exception e) {
            // 예기치 않은 오류 처리
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.fail("서버 오류가 발생했습니다."));  // ApiResponse.failure() 사용
        }
    }
}
