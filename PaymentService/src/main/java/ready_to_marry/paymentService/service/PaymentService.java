package ready_to_marry.paymentService.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ready_to_marry.paymentService.entity.Payment;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PortOneService portOneService; // JWT 발급을 위한 서비스

    @Transactional
    public ResponseEntity<Map<String, Object>> PaymentsVerify(String impUid, Long productId, Long memberId) throws IOException {
        String jwtToken = portOneService.getJwtToken(); // JWT 발급

//        // 상품 조회
//        Product product = productJpaRepository.findById(productId)
//                .orElseThrow(() -> new IllegalArgumentException(productId + "에 해당하는 물건이 없습니다."));

        // impUid 기반 결제 정보 조회
        Map<String, Object> paymentInfo = portOneService.getPaymentInfo(jwtToken, impUid);

        int paidAmount = ((Number) paymentInfo.get("amount")).intValue();
        int productPrice = 10000;

        if (paidAmount != productPrice) {
            portOneService.cancelAllPayment(jwtToken, impUid);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "지불한 금액 : " + paidAmount + "와 데이터베이스 금액 " + productPrice + "가 일치하지 않음.");
            return ResponseEntity.badRequest().body(response);
        }

        Payment payment = Payment.builder()
                .userId(1L)
                .itemId(2L)
                .partnerId(3L)
                .itemName("물건 이름")
                .amount(paidAmount)
                .paymentMethod((String) paymentInfo.get("payMethod"))
                .orderId((String) paymentInfo.get("orderId"))
                .impUid(impUid)
                .build();

//        paymentsRepository.save(payments);
//        productService.purchaseProductItem(productId, memberId);

        // 판매 완료 알림 로직
//        fcmService.sendMessageTo(product.getMember().getDeviceToken(),
//                "경매 종료!",
//                product.getTitle() + "의 경매가 종료되었어요!",
//                Long.toString(productId),
//                "PRODUCT");

        Map<String, Object> response = new HashMap<>();
        response.put("message", "결제가 완료되었습니다. 결제 금액 : " + paidAmount);
        return ResponseEntity.ok(response);
    }
}
