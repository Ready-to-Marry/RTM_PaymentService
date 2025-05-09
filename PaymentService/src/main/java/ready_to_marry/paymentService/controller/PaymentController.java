package ready_to_marry.paymentService.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ready_to_marry.paymentService.exception.ApiResponse;
import ready_to_marry.paymentService.service.PaymentService;

import java.io.IOException;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/verify")
    public ResponseEntity<ApiResponse<Void>> verifyPayment(
            @RequestParam String paymentId,
            @RequestParam Long productId,
            @RequestParam Long memberId
    ) throws IOException {
        paymentService.verifyPayment(paymentId, productId, memberId);
        return ResponseEntity.ok(ApiResponse.success("결제가 완료되었습니다.", null));
    }
}
