package ready_to_marry.paymentService.contract;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ContractResponse {
    private Long partnerId;
    private Long itemId;
    private int amount;
    private String contractContent;
}