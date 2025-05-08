package ready_to_marry.paymentService.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
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

    // 결제 정보 가져오는 함수
    public Map<String, Object> getPaymentInfo(String jwtToken, String impUid) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);

        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<Map> response = restTemplate.exchange(
                "https://api.portone.io/payments/" + impUid,
                HttpMethod.GET,
                request,
                Map.class
        );

        if (response.getStatusCode().is2xxSuccessful()) {
            Map<String, Object> body = response.getBody();
            if (body != null && body.containsKey("payment")) {
                return (Map<String, Object>) body.get("payment");
            } else {
                throw new RuntimeException("결제 정보가 응답에 존재하지 않습니다.");
            }
        } else {
            throw new RuntimeException("결제 조회 실패: " + response.getStatusCode());
        }
    }

    // 결제 취소 함수
    public void cancelAllPayment(String jwtToken, String impUid) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> cancelData = new HashMap<>();
        cancelData.put("reason", "결제 금액 불일치로 인한 취소");

        HttpEntity<Map<String, String>> request = new HttpEntity<>(cancelData, headers);

        ResponseEntity<Map> response = restTemplate.exchange(
                "https://api.portone.io/payments/" + impUid + "/cancel",
                HttpMethod.POST,
                request,
                Map.class
        );

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("결제 취소 실패: " + response.getStatusCode());
        }
    }
}
