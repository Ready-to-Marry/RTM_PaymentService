package ready_to_marry.paymentService.payment.dto;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequestDto {
    private String paymentId;
    private Long itemId;
    private Long userId;
    private Long contractId;
    private Long partnerId;
}
