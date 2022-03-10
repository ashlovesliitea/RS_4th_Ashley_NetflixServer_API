package com.example.demo.src.setting;

import com.example.demo.src.setting.model.request.PatchProfileLockReq;
import com.example.demo.src.setting.model.request.*;
import com.example.demo.src.setting.model.response.GetProfileRatedRes;
import com.example.demo.src.setting.model.response.GetProfileWatchedRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Timestamp;
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

    public int checkProfileWatchedList(PatchProfileWatchedReq patchProfileWatchedReq){
        String checkWatchedQuery = "select exists(select watchedlist_id from User_Watchedlist where profile_id = ? and video_id=? and season_num=? and episode_num=?)";
        Object[] checkWatchedParams = {patchProfileWatchedReq.getProfile_id(),patchProfileWatchedReq.getVideo_id(),patchProfileWatchedReq.getSeason_num(),patchProfileWatchedReq.getEpisode_num()};
        int Check= this.jdbcTemplate.queryForObject(checkWatchedQuery,
                int.class,
                checkWatchedParams);
        if(Check==0){
            return 0;
        }
        else{
            String findWatchedRecordQuery="select watchedlist_id from User_Watchedlist where profile_id = ? and video_id=? and season_num=? and episode_num=?";
            Object[] findWatchedRecordParams = {patchProfileWatchedReq.getProfile_id(),patchProfileWatchedReq.getVideo_id(),patchProfileWatchedReq.getSeason_num(),patchProfileWatchedReq.getEpisode_num()};
            return this.jdbcTemplate.queryForObject(findWatchedRecordQuery,int.class,findWatchedRecordParams);

        }

    }

    public int checkProfileWatchedList2(PostProfileWatchedReq postProfileWatchedReq){
        String checkWatchedQuery = "select exists(select watchedlist_id from User_Watchedlist where profile_id = ? and video_id=? and season_num=? and episode_num=?)";
        Object[] checkWatchedParams = {postProfileWatchedReq.getProfile_id(),postProfileWatchedReq.getVideo_id(),postProfileWatchedReq.getSeason_num(),postProfileWatchedReq.getEpisode_num()};
        return this.jdbcTemplate.queryForObject(checkWatchedQuery,
                int.class,
                checkWatchedParams);

    }

    public int checkProfileRatedList(PatchProfileRatedReq patchProfileRatedReq){
        String checkRatedQuery = "select exists(select ratinglist_id from User_Ratinglist where profile_id = ? and video_id=?)";
        Object[] checkRatedParams = {patchProfileRatedReq.getProfile_id(),patchProfileRatedReq.getVideo_id()};
        int Check= this.jdbcTemplate.queryForObject(checkRatedQuery,
                int.class,
                checkRatedParams);
        if(Check==0){
            return 0;
        }
        else{
            String findRatedRecordQuery="select ratinglist_id from User_Ratinglist where profile_id = ? and video_id=?)";
            return this.jdbcTemplate.queryForObject(findRatedRecordQuery,int.class,checkRatedParams);

        }

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

    public List<GetProfileRatedRes> getProfileRatedList(String ProfileId){
        String getProfileRatedListQuery="SELECT V.video_title,UR.rated_date,UR.rated\n" +
                "FROM User_Ratinglist UR\n" +
                "INNER JOIN Profile P ON P.profile_id=UR.profile_id\n" +
                "INNER JOIN (SELECT video_id,video_title FROM Video) V \n" +
                "\t\t\tON UR.video_id=V.video_id\n" +
                "WHERE P.profile_id=?";

        String getRatedListParam=ProfileId;
        return this.jdbcTemplate.query(getProfileRatedListQuery,
                (rs,rowNum)->new GetProfileRatedRes(
                        rs.getString("video_title"),
                        rs.getTimestamp("rated_date"),
                        rs.getInt("rated")
                ),getRatedListParam);

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

    public int modifyProfileLock(PatchProfileLockReq patchProfileLockReq){
        String ModifyProfileLockQuery="update Profile set profile_isLocked=?,profile_pw=? where profile_id=?";
        Object[] ModifyProfileLockParams=new Object[]{patchProfileLockReq.getProfile_isLocked(),patchProfileLockReq.getProfile_pw(),patchProfileLockReq.getProfile_id()};

        return this.jdbcTemplate.update(ModifyProfileLockQuery,ModifyProfileLockParams);}

    public int modifyProfilePlayback(PatchProfilePlayReq patchProfilePlayReq){
        String ModifyProfilePlayQuery="update Profile set profile_autoplay_next_episode=?,profile_autoplay_preview=? where profile_id=?";
        Object[] ModifyProfilePlayParams=new Object[]{patchProfilePlayReq.getProfile_autoplay_next_episode(),patchProfilePlayReq.getProfile_autoplay_preview(),patchProfilePlayReq.getProfile_id()};

        return this.jdbcTemplate.update(ModifyProfilePlayQuery,ModifyProfilePlayParams);}

    public int modifyProfileCommunication(PatchProfileCommReq patchProfileCommReq){
        String ModifyProfileCommQuery="update Profile set profile_communication_status=? where profile_id=?";
        Object[] ModifyProfileCommParams=new Object[]{patchProfileCommReq.getProfile_communication_status(),patchProfileCommReq.getProfile_id()};

        return this.jdbcTemplate.update(ModifyProfileCommQuery,ModifyProfileCommParams);}


    public int modifyProfileWatchedList(PatchProfileWatchedReq patchProfileWatchedReq,int watchedlist_id){
        String ModifyProfileWatchedQuery="update User_Watchedlist set stop_point=?,last_watched_date=? where Watchedlist_id=?";
        Object[] ModifyProfileWatchedParams=new Object[]{patchProfileWatchedReq.getStop_point(),new Timestamp(System.currentTimeMillis()),watchedlist_id};

        return this.jdbcTemplate.update(ModifyProfileWatchedQuery,ModifyProfileWatchedParams);}

    public int modifyProfileRatedList(PatchProfileRatedReq patchProfileRatedReq,int ratinglist_id){
        String ModifyProfileRatedQuery="update User_Ratinglist set rated=? where ratinglist_id=?";
        Object[] ModifyProfileRatedParams=new Object[]{patchProfileRatedReq.getRated(),ratinglist_id};

        return this.jdbcTemplate.update(ModifyProfileRatedQuery,ModifyProfileRatedParams);}

    public int createProfileRatedList(PostProfileRatedReq postProfileRatedReq){
        String createProfileRatedQuery = "insert into User_Ratinglist (Ratinglist_id,profile_id,video_id,rated_date,rated) VALUES (?,?,?,?,?)";
        String lastInsertedIdQuery = "select MAX(Ratinglist_id) from User_Ratinglist";
        int lastInsertedId = this.jdbcTemplate.queryForObject(lastInsertedIdQuery,int.class);

        Object[] createProfileRatedParams = new Object[]{lastInsertedId+1,postProfileRatedReq.getProfile_id(),postProfileRatedReq.getVideo_id(),new Timestamp(System.currentTimeMillis()),postProfileRatedReq.getRated()};
        this.jdbcTemplate.update(createProfileRatedQuery, createProfileRatedParams);


        return this.jdbcTemplate.queryForObject(lastInsertedIdQuery,int.class);
    }

    public int createProfileWatchedList(PostProfileWatchedReq postProfileWatchedReq){
        String createProfileWatchedQuery = "insert into User_Watchedlist (Watchedlist_id,profile_id,video_id,season_num,episode_num,stop_point,last_watched_date) VALUES (?,?,?,?,?,?,?)";
        String lastInsertedIdQuery = "select MAX(Watchedlist_id) from User_Watchedlist";
        int lastInsertedId = this.jdbcTemplate.queryForObject(lastInsertedIdQuery,int.class);

        Object[] createProfileWatchedParams = new Object[]{lastInsertedId+1,postProfileWatchedReq.getProfile_id(),postProfileWatchedReq.getVideo_id(),
        postProfileWatchedReq.getSeason_num(),postProfileWatchedReq.getEpisode_num(),postProfileWatchedReq.getStop_point(),new Timestamp(System.currentTimeMillis())};
        this.jdbcTemplate.update(createProfileWatchedQuery, createProfileWatchedParams);


        return this.jdbcTemplate.queryForObject(lastInsertedIdQuery,int.class);
    }
}

