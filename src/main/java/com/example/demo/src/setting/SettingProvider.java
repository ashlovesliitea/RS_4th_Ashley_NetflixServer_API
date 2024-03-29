package com.example.demo.src.setting;


import com.example.demo.config.BaseException;
import com.example.demo.src.setting.model.request.PatchProfileRatedReq;
import com.example.demo.src.setting.model.request.PatchProfileWatchedReq;
import com.example.demo.src.setting.model.request.PostProfileWatchedReq;
import com.example.demo.src.setting.model.response.GetProfileRatedRes;
import com.example.demo.src.setting.model.response.GetProfileWatchedRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

@Service
@RequestMapping("/app/settings")
public class SettingProvider {

    private final SettingDao settingDao;

    @Autowired
    public SettingProvider(SettingDao settingDao) {
        this.settingDao = settingDao;
    }

    public int checkProfileId(String profileId) throws BaseException{
        try{
            return settingDao.checkProfileId(profileId);
        }catch(Exception exception){
            System.out.println("exception = " + exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkProfileWatchedList(PatchProfileWatchedReq patchProfileWatchedReq) throws BaseException{
        try{
            return settingDao.checkProfileWatchedList(patchProfileWatchedReq);
        }catch(Exception exception){
            System.out.println("exception = " + exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkProfileWatchedList2(PostProfileWatchedReq postProfileWatchedReq) throws BaseException{
        try{
            return settingDao.checkProfileWatchedList2(postProfileWatchedReq);
        }catch(Exception exception){
            System.out.println("exception = " + exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkProfileRatedList(PatchProfileRatedReq patchProfileRatedReq) throws BaseException{
        try{
            return settingDao.checkProfileRatedList(patchProfileRatedReq);
        }catch(Exception exception){
            System.out.println("exception = " + exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }
    public List<GetProfileWatchedRes> getProfileWatchedList(String profileId) throws BaseException{
        try{
            List<GetProfileWatchedRes> acc=settingDao.getProfileWatchedList(profileId);
            return acc;
        }
        catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetProfileRatedRes> getProfileRatedList(String profileId) throws BaseException{
        try{
            List<GetProfileRatedRes> acc=settingDao.getProfileRatedList(profileId);
            return acc;
        }
        catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
