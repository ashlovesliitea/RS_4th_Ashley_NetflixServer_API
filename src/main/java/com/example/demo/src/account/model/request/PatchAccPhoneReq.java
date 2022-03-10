package com.example.demo.src.account.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PatchAccPhoneReq {
    private int user_num;
    private int user_phone;
}
