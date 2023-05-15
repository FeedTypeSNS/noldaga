package com.noldaga.controller.request;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@Getter
@NoArgsConstructor
public class FeedCreateRequest {

    private String title;
    private String content;
    private Long groupId;
    private int range;
    private List<String> urls;
}
