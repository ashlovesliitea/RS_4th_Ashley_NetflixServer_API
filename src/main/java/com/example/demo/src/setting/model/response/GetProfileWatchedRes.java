package com.example.demo.src.setting.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Time;
import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
public class GetProfileWatchedRes {
    private Timestamp last_watched_date;
    private String profile_name;
    private String video_title;
    private int season_num;
    private String episode_title;
    private Time stop_point;

}
