package com.example.demo.src.account.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostAccRes {
    private String jwt;
    private int userNum;
}
