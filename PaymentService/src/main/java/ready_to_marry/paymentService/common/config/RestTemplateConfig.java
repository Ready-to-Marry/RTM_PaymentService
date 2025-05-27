package ready_to_marry.paymentService.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    // eureka 연동용
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}