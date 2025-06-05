package ready_to_marry.paymentService.common.exception;

import lombok.Getter;

@Getter
public class InfrastructureException extends RuntimeException {

    private final int code;

    public InfrastructureException(ErrorCode errorCode, Throwable cause) {
        super(errorCode.getMessage(), cause);
        this.code = errorCode.getCode();
    }
}