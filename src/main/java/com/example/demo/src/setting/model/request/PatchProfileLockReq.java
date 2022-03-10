package com.example.demo.src.setting.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PatchProfileLockReq {
    private String profile_id;
    private int profile_isLocked;
    private int profile_pw;
}
