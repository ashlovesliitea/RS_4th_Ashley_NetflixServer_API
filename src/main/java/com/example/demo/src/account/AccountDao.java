package com.example.demo.src.account;


import com.example.demo.src.account.model.*;
import com.example.demo.src.userExample.model.PostUserReq;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

@Repository
public class AccountDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
            this.jdbcTemplate = new JdbcTemplate(dataSource);
    }


    @NotNull
    private GetAccRes getGetAccRes(ResultSet rs) throws SQLException {
        int userNum=rs.getInt("user_num");
        List<GetProfileRes> getProfileResList= getProfileList(userNum);
        //rs를 바탕으로 userNum을 추출하여 관련 프로필 리스트업
        GetAccRes getAccRes1 = new GetAccRes(
                rs.getInt("user_num"),
                rs.getString("user_id"),
                rs.getString("user_pw"),
                rs.getString("user_phone"),
                rs.getString("M.membership_title"),
                rs.getDate("membership_date"),
                rs.getTimestamp("user_join_date"),
                rs.getString("Payment_method")
                ,getProfileResList);

        return getAccRes1;
    }

   //모든 계정 출력하는 메소드
    public List<GetAccRes> getAccounts(){
        String getAccountsQuery="SELECT U.user_num,U.user_id,U.user_pw,U.user_phone,M.membership_title,U.membership_date,U.user_join_date,PM.Payment_method\n" +
                "FROM User U\n" +
                "INNER JOIN (SELECT membership_id,membership_title \n" +
                "FROM Membership) M On U.membership_id=M.membership_id\n" +
                "INNER JOIN (SELECT payment_id,payment_method FROM Payment) PM ON U.user_payment_id=PM.payment_id";

        List<GetAccRes> AccList= jdbcTemplate.query(getAccountsQuery,
                (rs, rowNum) -> getGetAccRes(rs));
        return AccList;
    }

    //회원 번호를 바탕으로 계정 정보 출력
    public GetAccRes getAccount(int userNum){
           String getAccountQuery=
           "SELECT U.user_num,U.user_id,U.user_pw,U.user_phone,M.membership_title,U.membership_date,U.user_join_date,PM.Payment_method\n" +
                   "FROM User U\n" +
                   "INNER JOIN (SELECT membership_id,membership_title \n" +
                   "FROM Membership) M On U.membership_id=M.membership_id\n" +
                   "INNER JOIN (SELECT payment_id,payment_method FROM Payment) PM ON U.user_payment_id=PM.payment_id\n" +
                   "WHERE U.user_num= ?";
           int getUserParams=userNum;

        return this.jdbcTemplate.queryForObject(getAccountQuery,
                (rs, rowNum) ->  {
                    return getGetAccRes(rs);
                },getUserParams);

    }

    //id를 바탕으로 계정 정보 출력
    public GetAccRes getAccountById(String userId){
        String getAccountQuery="SELECT U.user_num,U.user_id,U.user_pw,U.user_phone,U.membership_date,M.membership_title,PM.Payment_method\n" +
                "FROM User U\n" +
                "INNER JOIN (SELECT membership_id,membership_title \n" +
                "FROM Membership) M On U.membership_id=M.membership_id\n" +
                "INNER JOIN (SELECT payment_id,payment_method FROM Payment) PM ON U.user_payment_id=PM.payment_id\n" +
                "WHERE U.user_num= ?";
        String getUserParams=userId;

        GetAccRes getAccRes= jdbcTemplate.queryForObject(getAccountQuery,
                (rs, rowNum) -> {
                    return getGetAccRes(rs);
                },getUserParams);
        return getAccRes;

    }

    //회원번호와 관련된 모든 프로필 리스트 출력
    public List<GetProfileRes> getProfileList(int userNum){
        String getProfileListQuery="select * from Profile where user_num = ?";
        int getUserParams=userNum;
        return this.jdbcTemplate.query(getProfileListQuery,
                (rs, rowNum) -> new GetProfileRes(
                        rs.getInt("user_num"),
                        rs.getInt("profile_num"),
                        rs.getString("profile_id"),
                        rs.getString("profile_name"),
                        rs.getString("profile_image_url"),
                        rs.getInt("profile_isKid"),
                        rs.getInt("profile_status"),
                        rs.getInt("profile_language"),
                        rs.getInt("profile_isLocked"),
                        rs.getInt("profile_pw"),
                        rs.getInt("profile_viewingRestriction"),
                        rs.getInt("profile_autoplay_next_episode"),
                        rs.getInt("profile_autoplay_preview"),
                        rs.getInt("profile_communication_status")),getUserParams);
    }

    public int checkUserId(String userId){
        String checkIdQuery = "select exists(select user_id from User where user_id = ?)";
        String checkIdParams = userId;
        return this.jdbcTemplate.queryForObject(checkIdQuery,
                int.class,
                checkIdParams);

    }


    public int checkUserNum(int userNum){
        String checkNumQuery = "select exists(select user_num from User where user_num = ?)";
        int checkIdParams = userNum;
        return this.jdbcTemplate.queryForObject(checkNumQuery,
                int.class,
                checkIdParams);

    }

    public int createAccount(PostAccReq postAccReq){
        String createAccQuery = "insert into User (user_num, user_id, user_pw, nation_id,user_phone,membership_id,membership_date,user_join_date,user_payment_id) VALUES (?,?,?,?,?,?,?,?,?)";
        String lastInsertedIdQuery = "select MAX(user_num) from User";
        int lastInsertedId = this.jdbcTemplate.queryForObject(lastInsertedIdQuery,int.class);
        System.out.println("lastInsertedId = " + lastInsertedId);
        System.out.println("pw = " + postAccReq.getUser_pw());
        Object[] createAccParams = new Object[]{lastInsertedId+1,postAccReq.getUser_id(),postAccReq.getUser_pw(),postAccReq.getUser_nation_id(),postAccReq.getUser_phone()
                                    ,postAccReq.getUser_membership_id(),postAccReq.getUser_membership_date(),new Timestamp(System.currentTimeMillis()),postAccReq.getUser_payment_id()};
        this.jdbcTemplate.update(createAccQuery, createAccParams);


        return this.jdbcTemplate.queryForObject(lastInsertedIdQuery,int.class);
    }

    public String createProfile(PostProfileReq postProfileReq){
        String createProfileQuery = "insert into Profile (user_num,profile_num,profile_id,profile_name,profile_image_url,profile_isKid,profile_status," +
                "profile_language,profile_isLocked,profile_pw,profile_viewingRestriction,profile_autoplay_next_episode,profile_autoplay_preview,profile_communication_status) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        String lastInsertedProfileNumQuery = "select MAX(profile_num) from Profile where user_num=?";
        int lastInsertedProfileNum = this.jdbcTemplate.queryForObject(lastInsertedProfileNumQuery,int.class,postProfileReq.getUser_num());
        System.out.println("lastInsertedProfileNum = " + lastInsertedProfileNum);
        int newProfileNum=lastInsertedProfileNum+1;

        String newProfileId=postProfileReq.getUser_num()+"_"+newProfileNum;

        Object[] createProfileParams = new Object[]{postProfileReq.getUser_num(),newProfileNum,newProfileId,postProfileReq.getProfile_name(),postProfileReq.getProfile_image_url()
        ,postProfileReq.getProfile_isKid(),postProfileReq.getProfile_status(),postProfileReq.getProfile_language(),postProfileReq.getProfile_isLocked(),
        postProfileReq.getProfile_pw(),postProfileReq.getProfile_viewingRestriction(),postProfileReq.getProfile_autoplay_next_episode(),postProfileReq.getProfile_autoplay_preview(),
        postProfileReq.getProfile_communication()};
        this.jdbcTemplate.update(createProfileQuery, createProfileParams);

        String lastInsertedIdQuery = "select profile_id from Profile where profile_id=?";
        return this.jdbcTemplate.queryForObject(lastInsertedIdQuery,String.class,newProfileId);
    }

    public int ModifyAccountId(PatchAccIdReq patchAccIdReq){
        String ModifyAccountIdQuery="update User set user_id=? where user_num=?";
        Object[] ModifyAccIdParams = new Object[]{patchAccIdReq.getUser_id(),patchAccIdReq.getUser_num()};

        return this.jdbcTemplate.update(ModifyAccountIdQuery,ModifyAccIdParams);

    }

    public int ModifyAccountPW(PatchAccPWReq patchAccPWReq){
        String ModifyAccountPWQuery="update User set user_pw=? where user_num=?";
        Object[] ModifyAccPWParams = new Object[]{patchAccPWReq.getUser_pw(),patchAccPWReq.getUser_num()};

        return this.jdbcTemplate.update(ModifyAccountPWQuery,ModifyAccPWParams);

    }

    public int ModifyAccountPhone(PatchAccPhoneReq patchAccPhoneReq){
        String ModifyAccountPhoneQuery="update User set user_phone=? where user_num=?";
        Object[] ModifyAccPhoneParams = new Object[]{patchAccPhoneReq.getUser_phone(),patchAccPhoneReq.getUser_num()};

        return this.jdbcTemplate.update(ModifyAccountPhoneQuery,ModifyAccPhoneParams);

    }


    public int ModifyAccountMembership(PatchAccMemReq patchAccMemReq){
        String ModifyAccountMemQuery="update User set membership_id=? where user_num=?";
        Object[] ModifyAccMemParams = new Object[]{patchAccMemReq.getMembership_id(),patchAccMemReq.getUser_num()};

        return this.jdbcTemplate.update(ModifyAccountMemQuery,ModifyAccMemParams);

    }

    public int ModifyAccountPayment(PatchAccPayReq patchAccPayReq){
        String ModifyAccountPayQuery="update User set user_payment_id=? where user_num=?";
        Object[] ModifyAccPayParams = new Object[]{patchAccPayReq.getUser_payment_id(),patchAccPayReq.getUser_num()};

        return this.jdbcTemplate.update(ModifyAccountPayQuery,ModifyAccPayParams);

    }
}
