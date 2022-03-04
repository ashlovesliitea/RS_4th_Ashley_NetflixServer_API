package com.example.demo.src.setting;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;

import static com.example.demo.config.BaseResponseStatus.*;

import com.example.demo.src.setting.model.GetProfileWatchedRes;
import com.example.demo.src.setting.model.PatchProfileLangReq;
import com.example.demo.src.setting.model.PatchProfileRestrictReq;
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
     * * @return BaseResponse<List<GetAccRes>>
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
     * 프로필 언어 설정 API
     * [PATCH] /language/{ProfileId}
     * * @return BaseResponse<List<GetAccRes>>
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

}
