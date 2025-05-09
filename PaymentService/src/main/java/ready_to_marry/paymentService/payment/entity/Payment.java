package ready_to_marry.paymentService.payment.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ready_to_marry.paymentService.common.util.BaseEntity;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Payment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 사용자의 주문 목록 조회 시 사용
    private Long userId;

    private Long itemId;

    private Long partnerId;

    @Column(length = 255)
    private String itemName;
    private int amount;

    // 결제 방식 ex) card, kakao
    private String paymentMethod;

    // 고유 번호 (아임포트에서 조회 및 환불 시 사용)
    private String paymentId;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }
}
