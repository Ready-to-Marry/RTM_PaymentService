package ready_to_marry.paymentService.contract;

import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ready_to_marry.paymentService.common.dto.ApiResponse;
import ready_to_marry.paymentService.common.exception.ErrorCode;
import ready_to_marry.paymentService.common.exception.InfrastructureException;

@Component
@RequiredArgsConstructor
public class ContractClient {

    private final WebClient.Builder webClientBuilder;

    private static final String BASE_URL = "http://reservation-service";

    public ContractDetailRequest getContractDetail(Long contractId) {
        System.out.println(contractId);
        System.out.println(BASE_URL + "/contracts/detail/{contractId}");

        return webClientBuilder.build()
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/contracts/detail/{contractId}")
                        .build(contractId))
                .retrieve()
                .onStatus(
                        status -> status.isError(),
                        response -> response.bodyToMono(new ParameterizedTypeReference<ApiResponse<Void>>() {})
                                .flatMap(body -> {
                                    int code = body.getCode();
                                    String message = body.getMessage();
                                    System.out.println(BASE_URL + "/contracts/detail/{contractId}");

                                    if (code == 2301) {
                                        return Mono.error(new InfrastructureException(ErrorCode.DB_SAVE_FAILURE, new RuntimeException(message)));
                                    } else {
                                        return Mono.error(new RuntimeException("Unknown error: " + message));
                                    }
                                })
                )
                .bodyToMono(new ParameterizedTypeReference<ApiResponse<ContractDetailRequest>>() {})
                .map(ApiResponse::getData)
                .block();
    }

}
