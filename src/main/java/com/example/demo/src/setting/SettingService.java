package com.example.demo.src.setting;

import com.example.demo.config.BaseException;
import static com.example.demo.config.BaseResponseStatus.*;
import com.example.demo.src.setting.model.PatchProfileLangReq;
import com.example.demo.src.setting.model.PatchProfileRestrictReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SettingService {
    private SettingDao settingDao;
    private SettingProvider settingProvider;

    @Autowired
    public SettingService(SettingDao settingDao, SettingProvider settingProvider) {
        this.settingDao = settingDao;
        this.settingProvider = settingProvider;
    }




    public void modifyProfileLanguage(PatchProfileLangReq patchProfileLangReq)throws BaseException{
        try{
            int result=settingDao.modifyProfileLanguage(patchProfileLangReq);
            System.out.println(patchProfileLangReq.getProfile_id());
            if(result==0){
                throw new BaseException(MODIFY_FAIL_PROFILE_LANGUAGE);
            }
        }
        catch(Exception e){
            throw new BaseException(DATABASE_ERROR);
        }
    }
    public void modifyProfileRestriction(PatchProfileRestrictReq patchProfileRestrictReq)throws BaseException{
        try{
            int result=settingDao.modifyProfileRestriction(patchProfileRestrictReq);

            if(result==0){
                throw new BaseException(MODIFY_FAIL_PROFILE_RESTRICTION);
            }
        }
        catch(Exception e){
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
