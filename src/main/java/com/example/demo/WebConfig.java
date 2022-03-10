package com.example.demo;

import com.example.demo.src.interceptor.AuthenticationInterceptor;
import com.example.demo.utils.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final JwtService jwtService;
    private final ObjectMapper objectMapper;


    @Override
    public void addInterceptors(InterceptorRegistry reg){
        reg.addInterceptor(new AuthenticationInterceptor(jwtService,objectMapper))
                .order(1)
                .addPathPatterns("**"); //interceptor 작업이 필요한 path를 모두 추가한다.
                //.excludePathPatterns("app/accounts","/app/accounts/auth","app/videos/**");
                // 인가작업에서 제외할 API 경로를 따로 추가할수도 있으나, 일일히 따로 추가하기 어려우므로 어노테이션을 따로 만들어 해결한다.
    }
}
