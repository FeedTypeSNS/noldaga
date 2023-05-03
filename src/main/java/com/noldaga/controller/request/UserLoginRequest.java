package com.noldaga.controller.request;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor//테스트코드에서 사용
@Getter // @PostMapping 에서 json -> 자바객체로 바인딩 될때 getter 없어도 됨,  All생성자 or No생성자 둘중 하나 있으면됨 (ObjectMapper기반)
public class UserLoginRequest {

    private String username;
    private String password;
}
