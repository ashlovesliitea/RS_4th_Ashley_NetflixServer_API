package com.example.demo.src.account;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.account.model.entity.Account;
import com.example.demo.src.account.model.response.GetAccRes;
import com.example.demo.src.account.model.request.PostAuthReq;
import com.example.demo.src.account.model.response.PostAuthRes;
import com.example.demo.utils.JwtService;
import com.example.demo.utils.SHA256;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

@Service
@RequestMapping("/app/accounts")
public class AccountProvider {
        private final AccountDao accountDao;
        private final JwtService jwtService;

    @Autowired
    public AccountProvider(AccountDao accountDao, JwtService jwtService) {
        this.accountDao = accountDao;
        this.jwtService = jwtService;
    }


    public GetAccRes getAccountByUserNum(int userNum) {
            //DB 조회 관련 오류는 DatabaseExceptionHandler로 처리했음. ExceptionAdvice 확인
            GetAccRes acc = accountDao.getAccount(userNum);
            return acc;

    }

    public List<GetAccRes> getAccounts() {

            List<GetAccRes> acc = accountDao.getAccounts();
            return acc;

    }

    public GetAccRes getAccountById(String UserId){

            GetAccRes getAccRes = accountDao.getAccountById(UserId);
            return getAccRes;

    }

  //userId 존재하는지 확인
    public int checkUserId(String userId){

            return accountDao.checkUserId(userId);

    }

    public int getUserNum(String userId){

        return accountDao.getUserNum(userId);

    }
    //userNum으로 유저 존재하는지 확인
    public int checkUserNum(int userNum){
            return accountDao.checkUserNum(userNum);
    }

    public int checkAccountStatus(String userId){
            return accountDao.checkAccountStatus(userId);

    }

    public PostAuthRes accountAuth(PostAuthReq postAuthReq) throws BaseException{
        Account acc=accountDao.getPwd(postAuthReq.getUser_id());
        String encryptedPwd;

        encryptedPwd= new SHA256().encrypt(postAuthReq.getUser_pw());
        //encrypt 에러 처리-General Security Exception

        if(acc.getUser_pw().equals(encryptedPwd)){
            int userNum=acc.getUser_num();
            String userId=acc.getUser_id();
            String jwt=jwtService.createJwt(userNum,userId);
            return new PostAuthRes(userNum,jwt);
        }
        else{
            throw new BaseException(FAILED_TO_LOGIN);
        }
    }
}
