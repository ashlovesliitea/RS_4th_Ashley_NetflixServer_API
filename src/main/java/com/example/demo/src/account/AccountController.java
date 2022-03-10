package com.example.demo.src.account;


import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.account.model.request.*;
import com.example.demo.src.account.model.response.GetAccRes;
import com.example.demo.src.account.model.response.PostAccRes;
import com.example.demo.src.account.model.response.PostProfileRes;
import com.example.demo.src.account.model.request.PostAuthReq;
import com.example.demo.src.account.model.response.PostAuthRes;
import com.example.demo.src.annotation.NoAuth;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;
import static com.example.demo.utils.ValidationRegex.isRegexEmail;



@RestController
@RequestMapping("/app/accounts")
public class AccountController {

    final Logger logger= LoggerFactory.getLogger(this.getClass());

    private final AccountProvider accountProvider;
    private final AccountService accountService;
    private final JwtService jwtService;
    private static final int user_activated=1;
    private static final int user_deactivated=0;

    @Autowired //생성자 주입을 권장함.
    public AccountController(AccountProvider accountProvider, AccountService accountService, JwtService jwtService) {
        this.accountProvider = accountProvider;
        this.accountService = accountService;
        this.jwtService = jwtService;
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
        try{
                List<GetAccRes> getAccRes = accountProvider.getAccounts();
                return new BaseResponse<>(getAccRes);
        }
        catch(BaseException be){
            return new BaseResponse<>((be.getStatus()));
        }
    }

    /**
     * [POST] /accounts
     * 회원가입 API
     * [POST] /accounts
     * @return BaseResponse<>
     * */
    @ResponseBody
    @PostMapping("")
    public BaseResponse<PostAccRes> createUser(@RequestBody PostAccReq postAccReq) {
        if(postAccReq.getUser_id()== null){
            return new BaseResponse<>(POST_ACCOUNTS_EMPTY_ID);
        }
        //이메일 정규표현
        if(!isRegexEmail(postAccReq.getUser_id())){
            return new BaseResponse<>(POST_ACCOUNTS_INVALID_ID);
        }
        try{
            PostAccRes postAccRes = accountService.createAccount(postAccReq);
            return new BaseResponse<>(postAccRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * [POST] /accounts/{userNum}/profiles
     * 프로필 생성 API
     * [POST] /accounts
     * @return BaseResponse<>
     * */
    @ResponseBody
    @PostMapping("/{userNum}/profiles")
    public BaseResponse<PostProfileRes> createProfile(@PathVariable("userNum") int userNum, @RequestBody PostProfileReq postProfileReq) {
        try{
           PostProfileRes postProfileRes = accountService.createProfile(postProfileReq);
            return new BaseResponse<>(postProfileRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
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
        try{
           GetAccRes getAccRes= accountProvider.getAccountByUserNum(userNum);
           return new BaseResponse<>(getAccRes);
        }
        catch(BaseException be){
            return new BaseResponse<>((be.getStatus()));
        }
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
        try{
            PostAuthRes postAuthRes=accountProvider.accountAuth(postAuthReq);
            return new BaseResponse<>(postAuthRes);

        }catch(BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }


    /**
     * 유저 ID(이메일)변경 API
     * [PATCH] /accounts/{userNum}/email
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PatchMapping("/{userNum}/email")
    public BaseResponse<String> modifyUserId(@PathVariable("userNum") int userNum, @RequestBody PatchAccIdReq patchAccIdReq){
        try {
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
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }


    /**
     * 유저 비밀번호 변경 API
     * [PATCH] /accounts/{userNum}/password
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PatchMapping("/{userNum}/password")
    public BaseResponse<String> modifyUserPW(@PathVariable("userNum") int userNum, @RequestBody PatchAccPWReq patchAccPWReq){
        try {
            accountService.modifyAccountPW(patchAccPWReq);

            String result = "";
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 유저 전화번호 변경 API
     * [PATCH] /accounts/{userNum}/phone
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PatchMapping("/{userNum}/phone")
    public BaseResponse<String> modifyUserPhone(@PathVariable("userNum") int userNum, @RequestBody PatchAccPhoneReq patchAccPhoneReq){
        try {
            accountService.modifyAccountPhone(patchAccPhoneReq);

            String result = "";
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 유저 멤버쉽 변경 API
     * [PATCH] /accounts/{userNum}/membership
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PatchMapping("/{userNum}/membership")
    public BaseResponse<String> modifyUserMembership(@PathVariable("userNum") int userNum, @RequestBody PatchAccMemReq patchAccMemReq){
        try {
            accountService.modifyAccountMembership(patchAccMemReq);

            String result = "";
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 유저 결제수단 변경 API
     * [PATCH] /accounts/{userNum}/payment
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PatchMapping("/{userNum}/payment")
    public BaseResponse<String> modifyUserPayment(@PathVariable("userNum") int userNum, @RequestBody PatchAccPayReq patchAccPayReq){
        try {
            accountService.modifyAccountPayment(patchAccPayReq);
            String result = "";
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }
}
