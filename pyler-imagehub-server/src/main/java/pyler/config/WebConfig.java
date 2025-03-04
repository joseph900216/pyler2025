package pyler.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import pyler.client.JwtTokenClient;
import pyler.contents.RequestMap;
import pyler.interceptor.JwtAuthInterceptor;

/**
 * Interceptor를 적용하기 위한 Webconfig 구성
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final JwtTokenClient jwtTokenClient;

    public WebConfig(JwtTokenClient jwtTokenClient) {
        this.jwtTokenClient = jwtTokenClient;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new JwtAuthInterceptor(jwtTokenClient))
                .addPathPatterns(RequestMap.api + RequestMap.v1 + "/**") //인증 Interceptor 적용 될 url
                .excludePathPatterns( //인증 Interceptor 제외 url
                        RequestMap.api + RequestMap.v1 + RequestMap.user + "/login",
                        RequestMap.api + RequestMap.v1 + RequestMap.user + "/add"
                );
    }
}
