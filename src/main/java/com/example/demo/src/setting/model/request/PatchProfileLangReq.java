package com.example.demo.src.setting.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PatchProfileLangReq {
    private String profile_id;
    private String profile_language;
}
