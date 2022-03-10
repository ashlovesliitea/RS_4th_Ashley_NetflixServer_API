package com.example.demo.src.video.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Time;

@Getter
@Setter
@AllArgsConstructor
public class Episode {
    private int season_num;
    private int episode_num;
    private String episode_title;
    private Time running_time;
    private String episode_url;
    private String episode_plot;
}
