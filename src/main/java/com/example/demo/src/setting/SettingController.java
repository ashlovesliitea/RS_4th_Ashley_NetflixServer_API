package com.example.demo.src.setting;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;

import static com.example.demo.config.BaseResponseStatus.*;

import com.example.demo.src.setting.model.PatchProfileLockReq;
import com.example.demo.src.setting.model.PatchProfilePlayReq;
import com.example.demo.src.setting.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/app/settings")
public class SettingController {
    final Logger logger= LoggerFactory.getLogger(this.getClass());

    private final SettingProvider settingProvider;
    private final SettingService settingService;

    @Autowired
    public SettingController(SettingProvider settingProvider, SettingService settingService) {
        this.settingProvider = settingProvider;
        this.settingService = settingService;
    }

    /**
     * 프로필 언어 설정 API
     * [PATCH] /language/{ProfileId}
     * * @return BaseResponse<String>
     * */
    @ResponseBody
    @PatchMapping("/language/{ProfileId}")
    public BaseResponse<String> modifyProfileLanguage(@PathVariable("ProfileId") String ProfileId, @RequestBody PatchProfileLangReq patchProfileLangReq) {
        try {
            int profileIdFound=settingProvider.checkProfileId(ProfileId);
            if(profileIdFound==0){
                return new BaseResponse<>(PROFILE_ID_DOESNT_EXISTS);
            }
            settingService.modifyProfileLanguage(patchProfileLangReq);
            String result="";
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 프로필 설정 API
     * [PATCH] /restrictions/{ProfileId}
     * * @return BaseResponse<String>
     * */
    @ResponseBody
    @PatchMapping("/restrictions/{ProfileId}")
    public BaseResponse<String> modifyProfileRestriction(@PathVariable("ProfileId") String ProfileId, @RequestBody PatchProfileRestrictReq patchProfileRestrictReq) {
        try {
            int profileIdFound=settingProvider.checkProfileId(ProfileId);
            if(profileIdFound==0){
                return new BaseResponse<>(PROFILE_ID_DOESNT_EXISTS);
            }
            settingService.modifyProfileRestriction(patchProfileRestrictReq);
            String result="";
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 프로필 설정 API
     * [PATCH] /restrictions/{ProfileId}
     * * @return BaseResponse<String>
     * */
    @ResponseBody
    @PatchMapping("/lock/{ProfileId}")
    public BaseResponse<String> modifyProfileLock(@PathVariable("ProfileId") String ProfileId, @RequestBody PatchProfileLockReq patchProfileLockReq) {
        try {
            int profileIdFound=settingProvider.checkProfileId(ProfileId);
            if(profileIdFound==0){
                return new BaseResponse<>(PROFILE_ID_DOESNT_EXISTS);
            }
            settingService.modifyProfileLock(patchProfileLockReq);
            String result="";
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 프로필 설정 API
     * [PATCH] /playback/{ProfileId}
     * * @return BaseResponse<String>
     * */
    @ResponseBody
    @PatchMapping("/playback/{ProfileId}")
    public BaseResponse<String> modifyProfilePlayback(@PathVariable("ProfileId") String ProfileId, @RequestBody PatchProfilePlayReq patchProfilePlayReq) {
        try {
            int profileIdFound=settingProvider.checkProfileId(ProfileId);
            if(profileIdFound==0){
                return new BaseResponse<>(PROFILE_ID_DOESNT_EXISTS);
            }
            settingService.modifyProfilePlayback(patchProfilePlayReq);
            String result="";
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 프로필 설정 API
     * [PATCH] /communication/{ProfileId}
     * * @return BaseResponse<String>
     * */
    @ResponseBody
    @PatchMapping("/communication/{ProfileId}")
    public BaseResponse<String> modifyProfileCommunication(@PathVariable("ProfileId") String ProfileId, @RequestBody PatchProfileCommReq patchProfileCommReq) {
        try {
            int profileIdFound=settingProvider.checkProfileId(ProfileId);
            if(profileIdFound==0){
                return new BaseResponse<>(PROFILE_ID_DOESNT_EXISTS);
            }
            settingService.modifyProfileCommunication(patchProfileCommReq);
            String result="";
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 프로필이 시청한 영상 리스트 조회
     * [GET] /settings/videos/watched-list/{profileId}
     * 모든 계정을 조회하는 API
     * * @return BaseResponse<List<GetProfileWatchedRes>>
     * */

    @ResponseBody
    @GetMapping("/videos/watched-list/{profileId}")
    public BaseResponse <List<GetProfileWatchedRes>> getProfileWatchedList(@PathVariable("profileId")String profileId){
        try{
            List<GetProfileWatchedRes> ResList = settingProvider.getProfileWatchedList(profileId);
            return new BaseResponse<>(ResList);
        }
        catch(BaseException be){
            return new BaseResponse<>((be.getStatus()));
        }
    }


    /**
     * 프로필이 평가한 영상 리스트 조회
     * [GET] /settings/videos/rated-list/{profileId}
     * 모든 계정을 조회하는 API
     * * @return BaseResponse<List<GetProfileWatchedRes>>
     * */

    @ResponseBody
    @GetMapping("/videos/rated-list/{profileId}")
    public BaseResponse <List<GetProfileRatedRes>> getProfileRatedList(@PathVariable("profileId")String profileId){
        try{
            List<GetProfileRatedRes> ResList = settingProvider.getProfileRatedList(profileId);
            return new BaseResponse<>(ResList);
        }
        catch(BaseException be){
            return new BaseResponse<>((be.getStatus()));
        }
    }

    /**
     * [POST] /settings/videos/rated-list/{ProfileId}
     * 평가한 리스트 추가 API
     * @return BaseResponse<>
     * */
    @ResponseBody
    @PostMapping("/videos/rated-list/{ProfileId}")
    public BaseResponse<String> createProfileRatedList(@PathVariable("ProfileId") String profileId, @RequestBody PostProfileRatedReq postProfileRatedReq) {
        try{
            settingService.createProfileRatedList(postProfileRatedReq);
            String result="";
            return new BaseResponse<>(result);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * [POST] /settings/videos/watched-list/{ProfileId}
     * 프로필이 시청한 리스트 추가 API
     * @return BaseResponse<>
     * */
    @ResponseBody
    @PostMapping("/videos/watched-list/{ProfileId}")
    public BaseResponse<String> createProfileWatchedList(@PathVariable("ProfileId") String profileId, @RequestBody PostProfileWatchedReq postProfileWatchedReq) {
        try{
            int profileWatchedRecordNum=settingProvider.checkProfileWatchedList2(postProfileWatchedReq);
            if(profileWatchedRecordNum==1){
                return new BaseResponse<>(PROFILE_WATCHED_RECORD_ALREADY_EXISTS);
            }
            settingService.createProfileWatchedList(postProfileWatchedReq);
            String result="";
            return new BaseResponse<>(result);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 프로필이 시청한 리스트 설정 API
     * [PATCH] /settings/videos/watched-list/{ProfileId}
     * * @return BaseResponse<String>
     * */
    @ResponseBody
    @PatchMapping("/videos/watched-list/{ProfileId}")
    public BaseResponse<String> modifyProfileWatchedList(@PathVariable("ProfileId") String ProfileId, @RequestBody PatchProfileWatchedReq patchProfileWatchedReq){
        try {
            int profileWatchedRecordNum=settingProvider.checkProfileWatchedList(patchProfileWatchedReq);
            //정보를 토대로 레코드를 특정할 primary key를 찾음.
            if(profileWatchedRecordNum==0){
                return new BaseResponse<>(PROFILE_WATCHED_RECORD_DOESNT_EXISTS);
            }
            settingService.modifyProfileWatchedList(patchProfileWatchedReq,profileWatchedRecordNum);
            String result="";
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 프로필이 평가한 리스트 설정 API
     * [PATCH] /settings/videos/rated-list/{ProfileId}
     * * @return BaseResponse<String>
     * */
    @ResponseBody
    @PatchMapping("/videos/rated-list/{ProfileId}")
    public BaseResponse<String> modifyProfileRatedList(@PathVariable("ProfileId") String ProfileId, @RequestBody PatchProfileRatedReq patchProfileRatedReq){
        try {
            int profileRatedRecordNum=settingProvider.checkProfileRatedList(patchProfileRatedReq);
            //정보를 토대로 레코드를 특정할 primary key를 찾음.
            if(profileRatedRecordNum==0){
                return new BaseResponse<>(PROFILE_RATED_RECORD_DOESNT_EXISTS);
            }
            settingService.modifyProfileRatedList(patchProfileRatedReq,profileRatedRecordNum);
            String result="";
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

}
