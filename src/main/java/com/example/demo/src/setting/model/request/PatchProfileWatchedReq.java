package com.example.demo.src.setting.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Time;

@Getter
@Setter
@AllArgsConstructor
public class PatchProfileWatchedReq {
    private String profile_id;
    private int video_id;
    private int season_num;
    private int episode_num;
    private Time stop_point;
}
