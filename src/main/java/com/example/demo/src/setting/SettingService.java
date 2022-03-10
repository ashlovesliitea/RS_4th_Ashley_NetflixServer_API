package com.example.demo.src.setting;

import com.example.demo.config.BaseException;
import static com.example.demo.config.BaseResponseStatus.*;

import com.example.demo.src.setting.model.request.PatchProfileLockReq;
import com.example.demo.src.setting.model.request.*;
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

    public void modifyProfileLock(PatchProfileLockReq patchProfileLockReq)throws BaseException{
        try{
            int result=settingDao.modifyProfileLock(patchProfileLockReq);

            if(result==0){
                throw new BaseException(MODIFY_FAIL_PROFILE_LOCK);
            }
        }
        catch(Exception e){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void modifyProfilePlayback(PatchProfilePlayReq patchProfilePlayReq)throws BaseException{
        try{
            int result=settingDao.modifyProfilePlayback(patchProfilePlayReq);

            if(result==0){
                throw new BaseException(MODIFY_FAIL_PROFILE_PLAYBACK);
            }
        }
        catch(Exception e){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void modifyProfileCommunication(PatchProfileCommReq patchProfileCommReq)throws BaseException{
        try{
            int result=settingDao.modifyProfileCommunication(patchProfileCommReq);

            if(result==0){
                throw new BaseException(MODIFY_FAIL_PROFILE_COMMUNICATION);
            }
        }
        catch(Exception e){
            throw new BaseException(DATABASE_ERROR);
        }
    }



    public void createProfileRatedList(PostProfileRatedReq postProfileRatedReq)throws BaseException{
        try{
            int result=settingDao.createProfileRatedList(postProfileRatedReq);
            if(result==0){
                throw new BaseException(CREATE_FAIL_PROFILE_RATED_LIST);
            }
        }
        catch(Exception e){
            throw new BaseException(DATABASE_ERROR);
        }
    }


    public void createProfileWatchedList(PostProfileWatchedReq postProfileWatchedReq)throws BaseException{
        try{
            int result=settingDao.createProfileWatchedList(postProfileWatchedReq);
            if(result==0){
                throw new BaseException(CREATE_FAIL_PROFILE_WATCHED_LIST);
            }
        }
        catch(Exception e){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void modifyProfileWatchedList(PatchProfileWatchedReq patchProfileWatchedReq, int watchedlist_id)throws BaseException{
        try{
            int result=settingDao.modifyProfileWatchedList(patchProfileWatchedReq,watchedlist_id);

            if(result==0){
                throw new BaseException(MODIFY_FAIL_PROFILE_WATCHED_LIST);
            }
        }
        catch(Exception e){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void modifyProfileRatedList(PatchProfileRatedReq patchProfileRatedReq, int ratinglist_id)throws BaseException{
        try{
            int result=settingDao.modifyProfileRatedList(patchProfileRatedReq,ratinglist_id);

            if(result==0){
                throw new BaseException(MODIFY_FAIL_PROFILE_RATED_LIST);
            }
        }
        catch(Exception e){
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
