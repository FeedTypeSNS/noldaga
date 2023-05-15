package com.noldaga.controller.request;


import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@Getter // @PostMapping 에서 json -> 자바객체로 바인딩 될때 getter 없어도 됨,  All생성자 or No생성자 둘중 하나 있으면됨 (ObjectMapper기반)
public class UsernameRequest {
    @NotNull
    @NotBlank
    private String username;
}
