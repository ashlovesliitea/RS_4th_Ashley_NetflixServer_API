package com.example.demo.src.setting.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Time;
import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
public class PostProfileWatchedReq {
    private String profile_id;
    private int video_id;
    private int season_num;
    private int episode_num;
    private Time stop_point;
}
