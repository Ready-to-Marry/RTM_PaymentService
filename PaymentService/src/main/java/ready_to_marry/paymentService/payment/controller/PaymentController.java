package ready_to_marry.paymentService.payment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ready_to_marry.paymentService.common.dto.ApiResponse;
import ready_to_marry.paymentService.payment.dto.PaymentRequestDto;
import ready_to_marry.paymentService.payment.service.PaymentService;

@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/verify")
    public ResponseEntity<ApiResponse<Void>> verifyPayment(@RequestBody PaymentRequestDto requestDto) {
        System.out.println("호출 완료");
        paymentService.verifyPayment(requestDto);
        return ResponseEntity.ok(ApiResponse.success(0, "결제가 완료되었습니다.", null));
    }

    @DeleteMapping("/cancel/{id}")
    public ResponseEntity<ApiResponse<Void>> cancelPayment(
            @PathVariable Long id,
            @RequestParam Long userId
    ) {
        paymentService.canclePayment(id, userId);
        return ResponseEntity.ok(ApiResponse.success(0, "결제가 성공적으로 취소되었습니다.", null));
    }
}
