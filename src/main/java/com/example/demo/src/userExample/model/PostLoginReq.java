package com.example.demo.src.userExample.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostLoginReq {
    private String id;
    private String password;
}
