package com.example.demo.src.setting.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
public class GetProfileRatedRes {
    private String video_title;
    private Timestamp rated_date;
    private int rated;
}
