package ready_to_marry.paymentService.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse<T> {
    private int code; // 0: 정상, 1 이상: 비즈니스 오류 코드
    private String message; // 메시지
    private T data; // 응답 데이터
    private Meta meta; // 페이징 정보 (선택적)
    private ErrorDetail[] errors; // 오류 상세 (선택적)

    // 성공 응답을 생성하는 메서드
    public static <T> ApiResponse<T> success(String message, T data, Meta meta) {
        return new ApiResponse<>(0, message, data, meta, null);
    }

    // 실패 응답을 생성하는 메서드 (비즈니스 오류)
    public static <T> ApiResponse<T> failure(int code, String message, ErrorDetail[] errors) {
        return new ApiResponse<>(code, message, null, null, errors);
    }

    // 실패 응답을 생성하는 메서드 (기본 오류)
    public static <T> ApiResponse<T> failure(int code, String message) {
        return new ApiResponse<>(code, message, null, null, null);
    }
}