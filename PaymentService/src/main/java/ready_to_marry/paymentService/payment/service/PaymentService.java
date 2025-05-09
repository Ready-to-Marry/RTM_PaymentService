package ready_to_marry.paymentService.payment.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ready_to_marry.paymentService.payment.entity.Payment;
import ready_to_marry.paymentService.payment.entity.PaymentStatus;
import ready_to_marry.paymentService.common.exception.payment.PaymentException;
import ready_to_marry.paymentService.payment.repository.PaymentRepository;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PortOneService portOneService; // JWT 발급을 위한 서비스
    private final PaymentRepository paymentRepository;

    @Transactional
    public void verifyPayment(String paymentId, Long itemId, Long userId) {
        ObjectMapper objectMapper = new ObjectMapper();
        String jwtToken = portOneService.getJwtToken();

        // impUid 기반 결제 정보 조회
        Map<String, Object> paymentInfo = portOneService.getPaymentInfo(jwtToken, paymentId);

        Map<String, Object> amountMap = (Map<String, Object>) paymentInfo.get("amount");
        int paidAmount = ((Number) amountMap.get("paid")).intValue();

        String pgResponseJson = (String) paymentInfo.get("pgResponse");
        Map<String, Object> pgResponse;
        try {
            pgResponse = objectMapper.readValue(pgResponseJson, Map.class);
        } catch (JsonProcessingException e) {
            throw new PaymentException("파싱 중 오류 발생.");
        }

        String paymentMethod = (String) pgResponse.get("payMethod");

        if (paymentMethod == null) {
            throw new PaymentException("결제 정보가 누락되었습니다.");
        }

        int productPrice = 1000; // DB 조회 로직으로 바꾸는 게 이상적

        if (paidAmount != productPrice) {
            portOneService.cancelAllPayment(jwtToken, paymentId);
            throw new PaymentException("지불한 금액 " + paidAmount + "와 데이터베이스 금액 " + productPrice + "가 일치하지 않음.");
        }

        Payment payment = Payment.builder()
                .userId(userId)
                .itemId(itemId)
                .partnerId(3L)
                .itemName("물건 이름")
                .amount(paidAmount)
                .paymentMethod(paymentMethod)
                .paymentId(paymentId)
                .status(PaymentStatus.COMPLETED)
                .build();
        paymentRepository.save(payment);

        // user api 호출해서 유저의 결제 목록 추가 로직 구현 예정

        // 추후 알림 로직 등 추가 가능
        // fcmService.sendMessageTo(product.getMember().getDeviceToken(),
        // "경매 종료!",
        // product.getTitle() + "의 경매가 종료되었어요!",
        // Long.toString(itemId),
        // "PRODUCT");
    }

    @Transactional
    public void canclePayment (Long id, Long userId) throws PaymentException {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new PaymentException("결제 정보가 존재하지 않습니다."));

        // 결제 상태 확인 (환불 가능 상태 체크)
        if (payment.getStatus() == PaymentStatus.REFUNDED) {
            throw new PaymentException("이미 환불된 결제입니다.");
        }

        // 결제 시스템에 환불 요청
        String jwtToken = portOneService.getJwtToken();
        portOneService.cancelAllPayment(jwtToken, payment.getPaymentId());

        payment.setStatus(PaymentStatus.REFUNDED);
        paymentRepository.save(payment);

        // 환불 완료 후 추가 로직 (예: 사용자 알림, 거래 내역 등)
        // 예: fcmService.sendMessageTo(...);
    }

}
