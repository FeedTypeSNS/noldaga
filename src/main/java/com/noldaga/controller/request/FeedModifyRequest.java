package com.noldaga.controller.request;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor//test 에서 사용
@Getter //serialize 에 사용
@NoArgsConstructor //Json -> 자바객체 에서 사용됨 by jackson
public class FeedModifyRequest {

    private String title;
    private String content;
    private String groupId;
    private String range;

}
