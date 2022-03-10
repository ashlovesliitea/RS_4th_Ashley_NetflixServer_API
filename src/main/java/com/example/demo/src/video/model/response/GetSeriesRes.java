package com.example.demo.src.video.model.response;

import com.example.demo.src.video.model.entity.Video;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetSeriesRes {
    private Video video;
}
