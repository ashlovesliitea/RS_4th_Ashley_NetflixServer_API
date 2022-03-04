package com.example.demo.src.account.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostProfileReq {
    private int user_num;
    private String profile_name;
    private String profile_image_url;
    private int profile_isKid;
    private int profile_status;
    private int profile_language;
    private int profile_isLocked;
    private int profile_pw;
    private int profile_viewingRestriction;
    private int profile_autoplay_next_episode;
    private int profile_autoplay_preview;
    private int profile_communication;
}
