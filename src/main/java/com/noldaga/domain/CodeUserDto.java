package com.noldaga.domain;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CodeUserDto {

    private String email;
    private String username;

    public static CodeUserDto of(String email, String username){
        return new CodeUserDto(email, username);
    }
}
