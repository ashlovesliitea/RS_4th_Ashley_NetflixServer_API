package com.example.demo.src.userExample.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetUserRes {
    private int userIdx;
    private String userName;
    private String ID;
    private String email;
    private String password;
}
