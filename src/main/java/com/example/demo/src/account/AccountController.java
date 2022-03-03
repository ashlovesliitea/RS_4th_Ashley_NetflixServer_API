package com.example.demo.src.account;


import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.account.model.Account;
import com.example.demo.src.account.model.GetAccRes;
import com.example.demo.src.account.model.PostAccReq;
import com.example.demo.src.account.model.PostAccRes;
import com.example.demo.src.userExample.model.GetUserRes;
import com.example.demo.src.userExample.model.PostUserReq;
import com.example.demo.src.userExample.model.PostUserRes;
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
    private final AccountDao accountDao;

    @Autowired //생성자 주입을 권장함.
    public AccountController(AccountProvider accountProvider, AccountService accountService, AccountDao accountDao) {
        this.accountProvider = accountProvider;
        this.accountService = accountService;
        this.accountDao = accountDao;
    }

    /**
     * 계정 조회 API
     * [GET] /accounts
     * 모든 계정을 조회하는 API
     * * @return BaseResponse<List<GetAccRes>>
     * */

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
     * 계정 조회 API
     * [POST] /accounts
     * 회원가입 API
     * [POST] /accounts
     * @return BaseResponse<>
     * */
    @ResponseBody
    @PostMapping("")
    public BaseResponse<PostAccRes> createUser(@RequestBody PostAccReq postAccReq) {
        // TODO: email 관련한 짧은 validation 예시입니다. 그 외 더 부가적으로 추가해주세요!
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
     * 계정 조회 API
     * [GET] /accounts
     * 회원 번호로 회원 정보+가지고 있는 프로필 정보 조회하는 API
     * [GET] /accounts/:userNum
     * @return BaseResponse<>
     * */

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


}
