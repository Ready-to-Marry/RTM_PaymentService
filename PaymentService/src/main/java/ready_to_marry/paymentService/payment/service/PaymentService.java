package ready_to_marry.paymentService.payment.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ready_to_marry.paymentService.payment.dto.PaymentRequestDto;
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
    public void verifyPayment(PaymentRequestDto requestDto) {
        ObjectMapper objectMapper = new ObjectMapper();
        String jwtToken = portOneService.getJwtToken();

        // impUid 기반 결제 정보 조회
        Map<String, Object> paymentInfo = portOneService.getPaymentInfo(jwtToken, requestDto.getPaymentId());

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

        int productPrice = 1000; // TODO: DB에서 productId로 가격 조회 로직 필요

        if (paidAmount != productPrice) {
            portOneService.cancelAllPayment(jwtToken, requestDto.getPaymentId());
            throw new PaymentException("지불한 금액 " + paidAmount + "와 데이터베이스 금액 " + productPrice + "가 일치하지 않음.");
        }

        Payment payment = Payment.builder()
                .userId(requestDto.getMemberId())
                .itemId(requestDto.getProductId())
                .partnerId(requestDto.getPartnerId())
                .reservationId(requestDto.getReservationId())
                .itemName("물건 이름") // TODO: DB에서 이름 조회 필요
                .amount(paidAmount)
                .paymentMethod(paymentMethod)
                .paymentId(requestDto.getPaymentId())
                .status(PaymentStatus.COMPLETED)
                .build();
        paymentRepository.save(payment);

        // TODO: 유저 API 호출, 알림 등 추가 가능
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
