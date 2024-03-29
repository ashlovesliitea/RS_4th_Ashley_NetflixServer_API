package com.example.demo.src.account.social;

import com.example.demo.config.Constant;
import com.example.demo.src.account.AccountProvider;
import com.example.demo.src.account.model.entity.GoogleOAuthToken;
import com.example.demo.src.account.model.entity.GoogleUser;
import com.example.demo.src.account.model.response.GetSocialOAuthRes;
import com.example.demo.src.userExample.UserProvider;
import com.example.demo.utils.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class OAuthService {
    private final GoogleOauth googleOauth;
    private final HttpServletResponse response;
    private final AccountProvider accountProvider;
    private final JwtService jwtService;

    public void request(Constant.SocialLoginType socialLoginType) throws IOException {
        String redirectURL;
        switch (socialLoginType){
            case GOOGLE:{
                //각 소셜 로그인을 요청하면 소셜로그인 페이지로 리다이렉트 해주는 프로세스이다.
                redirectURL= googleOauth.getOauthRedirectURL();
            }break;
            default:{
                throw new IllegalArgumentException("알 수 없는 소셜 로그인 형식입니다.");
            }

        }

        response.sendRedirect(redirectURL);
    }

    public GetSocialOAuthRes oAuthLogin(Constant.SocialLoginType socialLoginType, String code) throws IOException {

        switch (socialLoginType){
            case GOOGLE:{
                //각 소셜 로그인을 요청하면 소셜로그인 페이지로 리다이렉트 해주는 프로세스이다.
                ResponseEntity<String> accessTokenResponse= googleOauth.requestAccessToken(code);
                GoogleOAuthToken oAuthToken=googleOauth.getAccessToken(accessTokenResponse);
               

                ResponseEntity<String> userInfoResponse=googleOauth.requestUserInfo(oAuthToken);
                GoogleUser googleUser= googleOauth.getUserInfo(userInfoResponse);

                String user_id=googleUser.getEmail();
                System.out.println("user_id = " + user_id);

                //우리 서버의 db와 대조하여 해당 user가 존재하는 지 확인한다.
                int user_num=accountProvider.getUserNum(user_id);
                if(user_num!=0){
                String jwtToken=jwtService.createJwt(user_num,user_id);
                GetSocialOAuthRes getSocialOAuthRes=new GetSocialOAuthRes(jwtToken,user_num);
                return getSocialOAuthRes;
                }

            }
            default:{
                throw new IllegalArgumentException("알 수 없는 소셜 로그인 형식입니다.");
            }

        }


    }

}
