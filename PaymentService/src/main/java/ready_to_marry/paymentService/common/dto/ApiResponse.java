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

    // meta X, errors X 생성자
    public ApiResponse(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    // meta O, errors X 생성자
    public ApiResponse(int code, String message, T data, Meta meta) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.meta = meta;
    }

    // meta X, errors O 생성자
    public ApiResponse(int code, String message, ErrorDetail[] errors) {
        this.code = code;
        this.message = message;
        this.errors = errors;
    }

    // 성공 응답을 생성하는 메서드 meta O
    public static <T> ApiResponse<T> success(int code, String message, T data, Meta meta) {
        return new ApiResponse<>(code, message, data, meta);
    }

    // 성공 응답 -> meta x
    public static <T> ApiResponse<T> success(int code, String message, T data) {
        return new ApiResponse<>(code, message, data);
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