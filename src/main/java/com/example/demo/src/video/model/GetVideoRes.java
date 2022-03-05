package com.example.demo.src.video.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetVideoRes {
    private Video videoInfo;
    private List<Episode> EpisodeInfo;
}
