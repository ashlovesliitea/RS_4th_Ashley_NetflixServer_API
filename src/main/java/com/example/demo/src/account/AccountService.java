package com.example.demo.src.account;


import com.example.demo.config.BaseException;
import com.example.demo.src.account.model.request.*;
import com.example.demo.src.account.model.response.PostAccRes;
import com.example.demo.src.account.model.response.PostProfileRes;
import com.example.demo.src.account.social.OAuthService;
import com.example.demo.utils.JwtService;
import com.example.demo.utils.SHA256;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import static com.example.demo.config.BaseResponseStatus.*;

@Service
public class AccountService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final AccountDao accountDao;
    private final AccountProvider accountProvider;
    private final OAuthService oAuthService;
    private final JwtService jwtService;


    @Autowired
    public AccountService(AccountDao accountDao, AccountProvider accountProvider, OAuthService oAuthService, JwtService jwtService) {
        this.accountDao = accountDao;
        this.accountProvider = accountProvider;
        this.oAuthService = oAuthService;
        this.jwtService = jwtService;
    }

    //POST
    public PostAccRes createAccount(PostAccReq postAccReq) throws BaseException {
        //중복
        if(accountProvider.checkUserId(postAccReq.getUser_id()) ==1){
            throw new BaseException(POST_ACCOUNTS_EXISTS_ID);
        }

        String pwd;

        //TODO: 암호화 에러 요인 확인 후 exceptionhandler 구현하기
        //java.security
        pwd = new SHA256().encrypt(postAccReq.getUser_pw());
        postAccReq.setUser_pw(pwd);

            int userNum = accountDao.createAccount(postAccReq);
            String userId=postAccReq.getUser_id();
            //jwt 발급.
            String jwt = jwtService.createJwt(userNum,userId);
            return new PostAccRes(jwt,userNum);

    }



    //POST
    public PostProfileRes createProfile(PostProfileReq postProfileReq) throws BaseException {

            String profile_id = accountDao.createProfile(postProfileReq);
            return new PostProfileRes(profile_id);

    }




    public void modifyAccountId(PatchAccIdReq patchAccIdReq) throws BaseException{

            int result=accountDao.ModifyAccountId(patchAccIdReq);
            if(result==0){
                //controller로 일단 던진 후 exceptionhandler에서 처리.
                throw new BaseException(MODIFY_FAIL_USER_ID);
            }


    }

    public void modifyAccountPW(PatchAccPWReq patchAccPWReq) throws BaseException{

            int result=accountDao.ModifyAccountPW(patchAccPWReq);
            if(result==0){
                throw new BaseException(MODIFY_FAIL_USER_PW);
            }


    }


    public void modifyAccountPhone(PatchAccPhoneReq patchAccPhoneReq) throws BaseException{

            int result=accountDao.ModifyAccountPhone(patchAccPhoneReq);
            if(result==0){
                throw new BaseException(MODIFY_FAIL_USER_PHONE_NUM);
            }


    }

    public void modifyAccountMembership(PatchAccMemReq patchAccMemReq) throws BaseException{

            int result=accountDao.ModifyAccountMembership(patchAccMemReq);
            if(result==0){
                throw new BaseException(MODIFY_FAIL_USER_MEMBERSHIP);
            }

    }

    public void modifyAccountPayment(PatchAccPayReq patchAccPayReq) throws BaseException{

            int result=accountDao.ModifyAccountPayment(patchAccPayReq);
            if(result==0){
                throw new BaseException(MODIFY_FAIL_USER_PAYMENT);
            }

    }
}
