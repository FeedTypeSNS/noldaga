package com.noldaga.controller.request;


import lombok.Getter;
import lombok.ToString;

@ToString
@Getter //@PostMapping 일때 @RequestBody 로 json->자바객체 바인딩 할때 Getter 필요 없음 ,All생성자 or 기본 생성자 둘중 하나만 있으면됨 (ObjectMapper기반)
public class UserProfileModifyRequest {

    private String nickname;

    private String profileMessage;
    private String profileImageUrl;

}
