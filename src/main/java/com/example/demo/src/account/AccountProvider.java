package com.example.demo.src.account;

import com.example.demo.config.BaseException;
import com.example.demo.src.account.model.Account;
import com.example.demo.src.account.model.GetAccRes;
import com.example.demo.src.userExample.model.GetUserRes;
import com.example.demo.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

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

    public GetAccRes getAccountByUserNum(int userNum) throws BaseException{
        try {
            GetAccRes acc = accountDao.getAccount(userNum);
            return acc;
        } catch (Exception exception) {
            System.out.println("exception = " + exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetAccRes> getAccounts() throws BaseException{
        try {
            List<GetAccRes> acc = accountDao.getAccounts();
            return acc;
        } catch (Exception exception) {
            System.out.println("exception = " + exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public GetAccRes getAccountById(String UserId) throws BaseException{
        try{
            GetAccRes getAccRes = accountDao.getAccountById(UserId);
            return getAccRes;
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

  //userId 존재하는지 확인
    public int checkUserId(String userId) throws BaseException{
        try{
            return accountDao.checkUserId(userId);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }
    //userNum으로 유저 존재하는지 확인
    public int checkUserNum(int userNum) throws BaseException{
        try{
            return accountDao.checkUserNum(userNum);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
