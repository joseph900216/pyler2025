package pyler.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import pyler.contents.RequestMap;

/**
 * Interceptor를 적용하기 위한 Webconfig 구성
 */
@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final JwtAuthInterceptor jwtAuthInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtAuthInterceptor)
                .addPathPatterns(RequestMap.api + RequestMap.v1 + "/**") //인증 Interceptor 적용 될 url
                .excludePathPatterns( //인증 Interceptor 제외 url
                        RequestMap.api + RequestMap.v1 + RequestMap.user + "/login",
                        RequestMap.api + RequestMap.v1 + RequestMap.user + "/add"
                );
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
