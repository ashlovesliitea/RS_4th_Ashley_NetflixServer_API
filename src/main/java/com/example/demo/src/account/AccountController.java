package com.example.demo.src.account;


import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.config.Constant;
import com.example.demo.src.account.model.request.*;
import com.example.demo.src.account.model.response.*;
import com.example.demo.src.account.model.request.PostAuthReq;
import com.example.demo.src.account.social.OAuthService;
import com.example.demo.src.annotation.NoAuth;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static com.example.demo.config.Constant.*;
import static com.example.demo.config.BaseResponseStatus.*;
import static com.example.demo.utils.ValidationRegex.isRegexEmail;



@RestController
@RequestMapping("/app/accounts")
public class AccountController {

    final Logger logger= LoggerFactory.getLogger(this.getClass());

    private final AccountProvider accountProvider;
    private final AccountService accountService;
    private final JwtService jwtService;
    private final OAuthService oAuthService;
    private static final int user_activated=1;
    private static final int user_deactivated=0;

    @Autowired //생성자 주입을 권장함.
    public AccountController(AccountProvider accountProvider, AccountService accountService, JwtService jwtService, OAuthService oAuthService) {
        this.accountProvider = accountProvider;
        this.accountService = accountService;
        this.jwtService = jwtService;
        this.oAuthService = oAuthService;
    }

    /**
     * 계정 조회 API
     * [GET] /accounts
     * 모든 계정을 조회하는 API
     * * @return BaseResponse<List<GetAccRes>>
     * */
    @NoAuth
    @ResponseBody
    @GetMapping("")
    public BaseResponse <List<GetAccRes>> getAccounts(){

                List<GetAccRes> getAccRes = accountProvider.getAccounts();
                return new BaseResponse<>(getAccRes);

    }

    /**
     * [POST] /accounts
     * 회원가입 API
     * [POST] /accounts
     * @return BaseResponse<>
     * */
    @ResponseBody
    @PostMapping("")
    public BaseResponse<PostAccRes> createUser(@RequestBody PostAccReq postAccReq) throws BaseException {
        if(postAccReq.getUser_id()== null){
            return new BaseResponse<>(POST_ACCOUNTS_EMPTY_ID);
        }
        //이메일 정규표현
        if(!isRegexEmail(postAccReq.getUser_id())){
            return new BaseResponse<>(POST_ACCOUNTS_INVALID_ID);
        }

            PostAccRes postAccRes = accountService.createAccount(postAccReq);
            return new BaseResponse<>(postAccRes);

    }

    /**
     * [POST] /accounts/{userNum}/profiles
     * 프로필 생성 API
     * [POST] /accounts
     * @return BaseResponse<>
     * */
    @ResponseBody
    @PostMapping("/{userNum}/profiles")
    public BaseResponse<PostProfileRes> createProfile(@PathVariable("userNum") int userNum, @RequestBody PostProfileReq postProfileReq) throws BaseException{

           PostProfileRes postProfileRes = accountService.createProfile(postProfileReq);
            return new BaseResponse<>(postProfileRes);

    }

    /**
     * 계정 조회 API
     * [GET] /accounts
     * 회원 번호로 회원 정보+가지고 있는 프로필 정보 조회하는 API
     * [GET] /accounts/:userNum
     * @return BaseResponse<>
     * */
    @NoAuth
    @ResponseBody
    @GetMapping("/{userNum}")
    public BaseResponse<GetAccRes> getUser(@PathVariable("userNum") int userNum){
           GetAccRes getAccRes= accountProvider.getAccountByUserNum(userNum);
           return new BaseResponse<>(getAccRes);
    }

    /**
     * 유저 로그인
     * [POST] /accounts/auth
     * @return BaseResponse<String>
     */
    @NoAuth
    @ResponseBody
    @PostMapping("/auth")
    public BaseResponse<PostAuthRes> accountAuth(@RequestBody PostAuthReq postAuthReq) throws BaseException {
        if(postAuthReq.getUser_id()==null){
            return new BaseResponse<>(POST_USERS_EMPTY_EMAIL);
        }
        if(!isRegexEmail(postAuthReq.getUser_id())){
            return new BaseResponse<>(POST_USERS_INVALID_EMAIL);
        }
        int checkAccountStatus=accountProvider.checkAccountStatus(postAuthReq.getUser_id());
        if(checkAccountStatus==user_deactivated){
            //비교시에 상수처리
            return new BaseResponse<>(ACCOUNT_DEACTIVATED);
        }

        PostAuthRes postAuthRes=accountProvider.accountAuth(postAuthReq);
        return new BaseResponse<>(postAuthRes);


    }

    /**
     * 유저 소셜 로그인으로 리다이렉트 해주는 url
      * [GET] /accounts/auth
     * @return void
     */
    @NoAuth
    @ResponseBody
    @GetMapping("/auth/{socialLoginType}") //GOOGLE이 들어올 것이다.
    public void socialLoginRedirect(@PathVariable(name="socialLoginType") String SocialLoginPath) throws IOException {
             SocialLoginType socialLoginType= SocialLoginType.valueOf(SocialLoginPath.toUpperCase());
             oAuthService.request(socialLoginType);
    }


    /**
     * Social Login API Server 요청에 의한 callback 을 처리
     * @param socialLoginPath (GOOGLE, FACEBOOK, NAVER, KAKAO)
     * @param code API Server 로부터 넘어오는 code
     * @return SNS Login 요청 결과로 받은 Json 형태의 String 문자열 (access_token, refresh_token 등)
     */

    @NoAuth
    @GetMapping(value = "/auth/{socialLoginType}/callback")
    public BaseResponse<GetSocialOAuthRes> callback(
            @PathVariable(name = "socialLoginType") String socialLoginPath,
            @RequestParam(name = "code") String code)throws IOException {
        System.out.println(">> 소셜 로그인 API 서버로부터 받은 code :"+ code);
        SocialLoginType socialLoginType= SocialLoginType.valueOf(socialLoginPath.toUpperCase());
        GetSocialOAuthRes getSocialOAuthRes=oAuthService.oAuthLogin(socialLoginType,code);
        return new BaseResponse<>(getSocialOAuthRes);
    }


    /**
     * 유저 ID(이메일)변경 API
     * [PATCH] /accounts/{userNum}/email
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PatchMapping("/{userNum}/email")
    public BaseResponse<String> modifyUserId(@PathVariable("userNum") int userNum, @RequestBody PatchAccIdReq patchAccIdReq) throws BaseException{

            /* 이 부분이 전부 interceptor로 대체되었다.
            jwt에서 idx 추출.
            int userNumByJwt = jwtService.getUserNum();
            //userIdx와 접근한 유저가 같은지 확인
            if(userNum != userNumByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            jwt로 대체
            int userNumFound = accountProvider.checkUserNum(userNum);
            if(userNumFound==0){
                return new BaseResponse<>(ACCOUNT_DOESNT_EXISTS);
            }
            */
            accountService.modifyAccountId(patchAccIdReq);

            String result = "";
            return new BaseResponse<>(result);

    }


    /**
     * 유저 비밀번호 변경 API
     * [PATCH] /accounts/{userNum}/password
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PatchMapping("/{userNum}/password")
    public BaseResponse<String> modifyUserPW(@PathVariable("userNum") int userNum, @RequestBody PatchAccPWReq patchAccPWReq) throws BaseException{

            accountService.modifyAccountPW(patchAccPWReq);
            String result = "";
            return new BaseResponse<>(result);

    }

    /**
     * 유저 전화번호 변경 API
     * [PATCH] /accounts/{userNum}/phone
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PatchMapping("/{userNum}/phone")
    public BaseResponse<String> modifyUserPhone(@PathVariable("userNum") int userNum, @RequestBody PatchAccPhoneReq patchAccPhoneReq) throws BaseException{

            accountService.modifyAccountPhone(patchAccPhoneReq);

            String result = "";
            return new BaseResponse<>(result);

    }

    /**
     * 유저 멤버쉽 변경 API
     * [PATCH] /accounts/{userNum}/membership
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PatchMapping("/{userNum}/membership")
    public BaseResponse<String> modifyUserMembership(@PathVariable("userNum") int userNum, @RequestBody PatchAccMemReq patchAccMemReq) throws BaseException{
            accountService.modifyAccountMembership(patchAccMemReq);

            String result = "";
            return new BaseResponse<>(result);

    }

    /**
     * 유저 결제수단 변경 API
     * [PATCH] /accounts/{userNum}/payment
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PatchMapping("/{userNum}/payment")
    public BaseResponse<String> modifyUserPayment(@PathVariable("userNum") int userNum, @RequestBody PatchAccPayReq patchAccPayReq) throws BaseException{
            accountService.modifyAccountPayment(patchAccPayReq);
            String result = "";
            return new BaseResponse<>(result);

    }
}
