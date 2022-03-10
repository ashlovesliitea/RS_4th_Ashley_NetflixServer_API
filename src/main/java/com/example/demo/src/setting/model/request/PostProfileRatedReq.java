package com.example.demo.src.setting.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostProfileRatedReq {
    private String profile_id;
    private int video_id;
    private int rated;
}
