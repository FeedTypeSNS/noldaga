package com.noldaga.controller.request;


import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@AllArgsConstructor //test 코드에서 사용
@Getter // @PostMapping 에서 json -> 자바객체로 바인딩 될때 getter 없어도 됨,  All생성자 or No생성자 둘중 하나 있으면됨 (ObjectMapper기반)
public class UserJoinRequest {

    @NotNull
    @NotBlank
    private String username;

    @NotNull
    @NotBlank
    private String password;

    @NotNull
    @NotBlank
    private String nickname;

    @NotNull
    @NotBlank
    @Email
    private String email;

    @NotNull
    @NotBlank
    private Integer codeId;

    @NotNull
    @NotBlank
    private String code;
}
