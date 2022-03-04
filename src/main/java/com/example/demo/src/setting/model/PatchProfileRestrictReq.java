package com.example.demo.src.setting.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PatchProfileRestrictReq {
    private String profile_id;
    private int profile_viewingRestriction;
    private int profile_isKid;
}
