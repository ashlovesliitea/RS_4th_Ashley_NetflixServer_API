package com.example.demo.src.setting;

import com.example.demo.src.setting.model.GetProfileWatchedRes;
import com.example.demo.src.setting.model.PatchProfileLangReq;
import com.example.demo.src.setting.model.PatchProfileRestrictReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class SettingDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public int checkProfileId(String profileId){
        String checkIdQuery = "select exists(select profile_id from Profile where profile_id = ?)";
        String checkIdParams = profileId;
        return this.jdbcTemplate.queryForObject(checkIdQuery,
                int.class,
                checkIdParams);

    }
    public List<GetProfileWatchedRes> getProfileWatchedList(String ProfileId){
        String getProfileWatchedListQuery="SELECT UW.last_watched_date,P.profile_name,V.video_title,UW.season_num,E.episode_title,UW.stop_point\n" +
                "FROM User_Watchedlist UW\n" +
                "INNER JOIN Profile P ON P.profile_id=UW.profile_id\n" +
                "INNER JOIN (SELECT video_id,video_title FROM Video) V \n" +
                "ON UW.video_id=V.video_id\n" +
                "INNER JOIN (SELECT video_id,season_num,episode_num,episode_title FROM Episode) E\n" +
                "ON UW.video_id=E.video_id and UW.season_num=E.season_num and UW.episode_num=E.episode_num\n" +
                "WHERE P.profile_id= ? \n" +
                "ORDER BY last_watched_date DESC";

        String getWatchedListParam=ProfileId;
        return this.jdbcTemplate.query(getProfileWatchedListQuery,
                (rs,rowNum)->new GetProfileWatchedRes(
                        rs.getTimestamp("last_watched_date"),
                        rs.getString("profile_name"),
                        rs.getString("video_title"),
                        rs.getInt("season_num"),
                        rs.getString("episode_title"),
                        rs.getTime("stop_point")
                ),getWatchedListParam);

    }
    public int modifyProfileLanguage(PatchProfileLangReq patchProfileLangReq){
        String ModifyProfileLangQuery="update Profile set profile_language=? where profile_id=?";
        Object[] ModifyProfileLangParams=new Object[]{patchProfileLangReq.getProfile_language(),patchProfileLangReq.getProfile_id()};

        return this.jdbcTemplate.update(ModifyProfileLangQuery,ModifyProfileLangParams);}

    public int modifyProfileRestriction(PatchProfileRestrictReq patchProfileRestrictReq){
        String ModifyProfileResQuery="update Profile set profile_viewingRestriction=?,profile_isKid=? where profile_id=?";
        Object[] ModifyProfileResParams=new Object[]{patchProfileRestrictReq.getProfile_viewingRestriction(),patchProfileRestrictReq.getProfile_isKid(),
                patchProfileRestrictReq.getProfile_id()};

        return this.jdbcTemplate.update(ModifyProfileResQuery,ModifyProfileResParams);}

}