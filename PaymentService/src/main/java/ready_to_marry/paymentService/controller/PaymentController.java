package ready_to_marry.paymentService.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ready_to_marry.paymentService.service.PaymentService;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/verify")
    public ResponseEntity<Map<String, Object>> verifyPayment(
            @RequestParam String impUid,
            @RequestParam Long productId,
            @RequestParam Long memberId
    ) throws IOException {
        return paymentService.PaymentsVerify(impUid, productId, memberId);
    }
}
