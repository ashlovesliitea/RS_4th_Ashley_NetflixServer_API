package com.example.demo.src.account.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostAuthReq {
    private String user_id;
    private String user_pw;
}
