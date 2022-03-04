package com.example.demo.src.setting;


import com.example.demo.config.BaseException;
import com.example.demo.src.setting.model.GetProfileWatchedRes;
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

    public List<GetProfileWatchedRes> getProfileWatchedList(String profileId) throws BaseException{
        try{
            List<GetProfileWatchedRes> acc=settingDao.getProfileWatchedList(profileId);
            return acc;
        }
        catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
