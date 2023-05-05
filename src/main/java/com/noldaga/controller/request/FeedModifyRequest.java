package com.noldaga.controller.request;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Getter
@NoArgsConstructor
public class FeedModifyRequest {

    private String title;
    private String content;
    private Long groupId;
    private int range;

}
