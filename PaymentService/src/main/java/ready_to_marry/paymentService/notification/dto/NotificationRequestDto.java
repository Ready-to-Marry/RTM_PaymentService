package ready_to_marry.paymentService.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationRequestDto {
    private String targetToken;
    private String title;
    private String message;
    private String userId;
    private int amount;
    private Long contractId;
}
