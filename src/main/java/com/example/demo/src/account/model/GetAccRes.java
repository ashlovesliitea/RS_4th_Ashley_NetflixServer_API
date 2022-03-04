package com.example.demo.src.account.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetAccRes {
    private int user_num;
    private String user_id;
    private String user_pw;
    private String user_phone;
    private String user_membership_name;
    private Date user_membership_date;
    private Timestamp user_join_date;
    private String user_payment_method;
    List <GetProfileRes> profileList;
}
