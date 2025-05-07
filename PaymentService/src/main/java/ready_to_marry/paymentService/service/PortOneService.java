package ready_to_marry.paymentService.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class PortOneService {

    @Value("${portone.api.secret}")
    private String API_SECRET;

    // 일부 API 에서만 사용하는 JWT Token 발급
    public String getJwtToken() {
        RestTemplate restTemplate = new RestTemplate();

        // 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 요청 body 설정
        String body = "{ \"apiSecret\": \"" + API_SECRET + "\" }";

        HttpEntity<String> request = new HttpEntity<>(body, headers);

        try {
            // API 호출
            ResponseEntity<Map> response = restTemplate.exchange(
                    "https://api.portone.io/login/api-secret",  // 토큰 발급 URL
                    HttpMethod.POST,                         // POST 요청
                    request,                                 // 요청 객체
                    Map.class                                // 응답 클래스 (JSON)
            );

            // 응답 상태 코드 확인
            if (response.getStatusCode() == HttpStatus.OK) {
                // 응답 본문 처리
                Map<String, Object> responseBody = response.getBody();
                if (responseBody != null && responseBody.containsKey("accessToken")) {
                    return (String) responseBody.get("accessToken");  // accessToken 반환
                } else {
                    // accessToken이 없을 경우
                    throw new RuntimeException("AccessToken not found in response");
                }
            } else {
                // 응답 코드가 OK가 아닌 경우
                throw new RuntimeException("API request failed with status: " + response.getStatusCode());
            }

        } catch (Exception e) {
            // 예외 처리
            e.printStackTrace();
            throw new RuntimeException("Failed to get JWT token", e);
        }
    }
}
