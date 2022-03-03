package com.example.demo.src.account;


import com.example.demo.src.account.model.Account;
import com.example.demo.src.account.model.GetAccRes;
import com.example.demo.src.account.model.GetProfileRes;
import com.example.demo.src.account.model.PostAccReq;
import com.example.demo.src.userExample.model.PostUserReq;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
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
                rs.getInt("nation_id"),
                rs.getString("user_phone"),
                rs.getInt("membership_id"),
                rs.getDate("membership_date"),
                rs.getTimestamp("user_join_date"),
                rs.getInt("user_payment_id")
                ,getProfileResList);

        return getAccRes1;
    }

   //모든 계정 출력하는 메소드
    public List<GetAccRes> getAccounts(){
        String getAccountQuery="select * from User";

        List<GetAccRes> AccList= jdbcTemplate.query(getAccountQuery,
                (rs, rowNum) -> getGetAccRes(rs));
        return AccList;
    }

    //회원 번호를 바탕으로 계정 정보 출력
    public GetAccRes getAccount(int userNum){
           String getAccountQuery="select * from User where user_num= ?";
           int getUserParams=userNum;

        return this.jdbcTemplate.queryForObject(getAccountQuery,
                (rs, rowNum) ->  {
                    return getGetAccRes(rs);
                },getUserParams);

    }

    //id를 바탕으로 계정 정보 출력
    public GetAccRes getAccountById(String userId){
        String getAccountQuery="select * from User where user_id= ?";

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

    public int createAccount(PostAccReq postAccReq){
        String createAccQuery = "insert into User (user_num, user_id, user_pw, nation_id,user_phone,membership_id,membership_date,user_join_date,user_payment_id) VALUES (?,?,?,?,?,?,?,?,?)";
        String lastInsertedIdQuery = "select last_insert_id()";
        int lastInsertedId = this.jdbcTemplate.queryForObject(lastInsertedIdQuery,int.class);
        Object[] createAccParams = new Object[]{lastInsertedId+1,postAccReq.getUser_id(),postAccReq.getUser_pw(),postAccReq.getUser_nation_id(),postAccReq.getUser_phone()
                                    ,postAccReq.getUser_membership_id(),postAccReq.getUser_membership_date(),postAccReq.getUser_join_date(),postAccReq.getUser_payment_id()};
        this.jdbcTemplate.update(createAccQuery, createAccParams);


        return this.jdbcTemplate.queryForObject(lastInsertedIdQuery,int.class);
    }

}
