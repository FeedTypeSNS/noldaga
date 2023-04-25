package com.noldaga.controller.request;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor//테스트코드에서 사용
@Getter//ObjectMapper 에서 자바객체를 json객체로 바꿀때 사용
@NoArgsConstructor //Json -> 자바객체 할때 사용됨 by jackson
public class UserLoginRequest {

    private String username;
    private String password;
}
