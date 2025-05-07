package ready_to_marry.paymentService.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ready_to_marry.paymentService.service.PortOneService;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PortOneService portOneService;

    @GetMapping("/test-portone")
    public ResponseEntity<String> testPortOne() {
        try {
            String token = portOneService.getJwtToken();
            return ResponseEntity.ok("정상적으로 토큰 발급됨: " + token);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("토큰 발급 실패: " + e.getMessage());
        }
    }
}
