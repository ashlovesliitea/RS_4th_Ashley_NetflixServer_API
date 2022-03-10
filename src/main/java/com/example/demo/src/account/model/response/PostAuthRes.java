package com.example.demo.src.account.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostAuthRes {
    private int userIdx;
    private String jwt;
}
