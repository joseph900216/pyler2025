package pyler.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import pyler.client.JwtTokenClient;

/**
 * JWt client bean 생명주기 관리
 */
@Configuration
@ConditionalOnProperty(name = "jwt.client.enabled", havingValue = "true", matchIfMissing = false)
public class JwtClientConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public JwtTokenClient jwtTokenClient(RestTemplate restTemplate) {
        return new JwtTokenClient(restTemplate);
    }
}
