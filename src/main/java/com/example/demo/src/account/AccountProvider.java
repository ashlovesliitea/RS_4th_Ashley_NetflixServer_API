package com.example.demo.src.account;

import com.example.demo.config.BaseException;
import com.example.demo.src.account.model.entity.Account;
import com.example.demo.src.account.model.response.GetAccRes;
import com.example.demo.src.account.model.request.PostAuthReq;
import com.example.demo.src.account.model.response.PostAuthRes;
import com.example.demo.utils.JwtService;
import com.example.demo.utils.SHA256;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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

    public int checkAccountStatus(String userId)throws BaseException{
        try{
            return accountDao.checkAccountStatus(userId);
        }
        catch(Exception e){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public PostAuthRes accountAuth(PostAuthReq postAuthReq) throws BaseException {
        Account acc=accountDao.getPwd(postAuthReq.getUser_id());
        String encryptedPwd;
        try{
            encryptedPwd= new SHA256().encrypt(postAuthReq.getUser_pw());

        }catch(Exception ignored) {
            throw new BaseException(PASSWORD_ENCRYPTION_ERROR);
        }

        if(acc.getUser_pw().equals(encryptedPwd)){
            int userNum=acc.getUser_num();
            String jwt=jwtService.createJwt(userNum);
            return new PostAuthRes(userNum,jwt);
        }
        else{
            throw new BaseException(FAILED_TO_LOGIN);
        }
    }
}
