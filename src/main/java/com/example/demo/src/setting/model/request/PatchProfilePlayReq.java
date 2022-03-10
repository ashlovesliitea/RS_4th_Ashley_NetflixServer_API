package com.example.demo.src.setting.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PatchProfilePlayReq {
    private String profile_id;
    private int profile_autoplay_next_episode;
    private int profile_autoplay_preview;
}
