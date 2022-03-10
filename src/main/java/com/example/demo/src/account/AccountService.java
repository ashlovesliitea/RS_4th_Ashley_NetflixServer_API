package com.example.demo.src.account;


import com.example.demo.config.BaseException;
import com.example.demo.src.account.model.request.*;
import com.example.demo.src.account.model.response.PostAccRes;
import com.example.demo.src.account.model.response.PostProfileRes;
import com.example.demo.utils.JwtService;
import com.example.demo.utils.SHA256;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.example.demo.config.BaseResponseStatus.*;

@Service
public class AccountService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final AccountDao accountDao;
    private final AccountProvider accountProvider;
    private final JwtService jwtService;


    @Autowired
    public AccountService(AccountDao accountDao, AccountProvider accountProvider, JwtService jwtService) {
        this.accountDao = accountDao;
        this.accountProvider = accountProvider;
        this.jwtService = jwtService;
    }

    //POST
    public PostAccRes createAccount(PostAccReq postAccReq) throws BaseException {
        //중복
        if(accountProvider.checkUserId(postAccReq.getUser_id()) ==1){
            throw new BaseException(POST_ACCOUNTS_EXISTS_ID);
        }

        String pwd;
        try{
            //암호화
            pwd = new SHA256().encrypt(postAccReq.getUser_pw());
            postAccReq.setUser_pw(pwd);

        } catch (Exception ignored) {
            throw new BaseException(PASSWORD_ENCRYPTION_ERROR);
        }
        try{
            int userNum = accountDao.createAccount(postAccReq);
            //jwt 발급.
            String jwt = jwtService.createJwt(userNum);
            return new PostAccRes(jwt,userNum);
        } catch (Exception exception) {
            System.out.println("exception = " + exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    //POST
    public PostProfileRes createProfile(PostProfileReq postProfileReq) throws BaseException {

        try{
            String profile_id = accountDao.createProfile(postProfileReq);

            return new PostProfileRes(profile_id);
        } catch (Exception exception) {
            System.out.println("exception = " + exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }


    public void modifyAccountId(PatchAccIdReq patchAccIdReq) throws BaseException{
        try{
            int result=accountDao.ModifyAccountId(patchAccIdReq);
            if(result==0){
                throw new BaseException(MODIFY_FAIL_USER_ID);
            }

        }
        catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void modifyAccountPW(PatchAccPWReq patchAccPWReq) throws BaseException{
        try{
            int result=accountDao.ModifyAccountPW(patchAccPWReq);
            if(result==0){
                throw new BaseException(MODIFY_FAIL_USER_PW);
            }

        }
        catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }


    public void modifyAccountPhone(PatchAccPhoneReq patchAccPhoneReq) throws BaseException{
        try{
            int result=accountDao.ModifyAccountPhone(patchAccPhoneReq);
            if(result==0){
                throw new BaseException(MODIFY_FAIL_USER_PHONE_NUM);
            }

        }
        catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void modifyAccountMembership(PatchAccMemReq patchAccMemReq) throws BaseException{
        try{
            int result=accountDao.ModifyAccountMembership(patchAccMemReq);
            if(result==0){
                throw new BaseException(MODIFY_FAIL_USER_MEMBERSHIP);
            }

        }
        catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void modifyAccountPayment(PatchAccPayReq patchAccPayReq) throws BaseException{
        try{
            int result=accountDao.ModifyAccountPayment(patchAccPayReq);
            if(result==0){
                throw new BaseException(MODIFY_FAIL_USER_PAYMENT);
            }

        }
        catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
