package ready_to_marry.paymentService.notification.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ready_to_marry.paymentService.notification.dto.NotificationRequestDto;

@Slf4j
@Service
public class NotificationService {
    private final WebClient webClient;

    public NotificationService(WebClient webClient) {
        this.webClient = webClient.mutate()
                .baseUrl("https://zbfu2a37ol.execute-api.ap-northeast-2.amazonaws.com")
                .build();
    }

    public void sendNotification(NotificationRequestDto requestDto) {
        try {
            webClient.post()
                    .uri("/send-notification")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(requestDto)
                    .retrieve()
                    .onStatus(status -> status.isError(), response ->
                            response.bodyToMono(String.class).flatMap(errorBody ->
                                    Mono.error(new RuntimeException("Notification API 오류: " + errorBody))
                            )
                    )
                    .bodyToMono(Void.class)
                    .block();
            log.info("🔔 알림 전송 성공: {}", requestDto);
        } catch (Exception e) {
            log.error("❌ 알림 전송 실패: {}", e.getMessage(), e);
        }
    }
}
