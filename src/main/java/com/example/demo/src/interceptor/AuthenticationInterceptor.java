package com.example.demo.src.interceptor;

import com.example.demo.config.BaseException;
import com.example.demo.src.annotation.NoAuth;
import com.example.demo.utils.JwtService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class AuthenticationInterceptor implements HandlerInterceptor {
    private final JwtService jwtService;
    private final ObjectMapper objectMapper; //자바 객체를 json으로 serialization

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,Object handler) throws Exception {
        boolean check=checkAnnotation(handler, NoAuth.class);
        if(check) return true;
        System.out.println("인터셉트중..");

        try{
            int userNumByJwt = jwtService.getUserNum();
            request.setAttribute("userNum",userNumByJwt);
        }catch(BaseException exception){
            String requestURI= request.getRequestURI();

            Map<String,String> map=new HashMap<>();
            map.put("requestURI","/app/accounts/auth?redirectURI="+requestURI);
            //redirectURI는 로그인 절차가 끝내고 다시 시도했던 페이지로 돌아가기 위해 JSON 정보에 포함시킨다.
            String json=objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(map);
            response.getWriter().write(json);

            return false;
        }
        return true;
    }

    private boolean checkAnnotation(Object handler,Class cls){
        HandlerMethod handlerMethod=(HandlerMethod) handler;
        if(handlerMethod.getMethodAnnotation(cls)!=null){ //해당 어노테이션이 존재하면 true.
            return true;
        }
        return false;
    }
}
