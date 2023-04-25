package com.noldaga.controller.response;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter //스프링이 json 으로 변환해줄때 필요
public class UserLoginResponse {

    private String token;

}
