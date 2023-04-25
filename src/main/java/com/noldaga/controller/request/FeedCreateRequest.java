package com.noldaga.controller.request;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor//test 에서 사용
@Getter //serialize 에 사용
@NoArgsConstructor //@RequestBody 매커니즘 의 Json 데이터 -> 자바객체   과정에서 사용됨 by jackson
public class FeedCreateRequest {

    private String title;
    private String content;
    private Long groupId;
    private int range;
}
