package com.example.demo.src.account.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;
import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
public class PostAccReq {
    private String user_id;
    private String user_pw;
    private int user_nation_id;
    private String user_phone;
    private int user_membership_id;
    private Date user_membership_date;
    private int user_payment_id;

}
