package com.example.demo.src.video.model.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Time;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class Video {
    private String video_title;
    private String video_date;
    private int video_pg;
    private String video_plot;
    private int series_num;
    private int video_series_status;
    private Time running_time;
    private String video_trailer_url;
    private String video_thumbnail_url;
    private List<String> actor_list;
    private List<String> char_list;
    private List<String> genre_list;
}
