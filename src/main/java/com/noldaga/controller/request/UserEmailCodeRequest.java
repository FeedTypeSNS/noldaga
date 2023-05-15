package com.noldaga.controller.request;


import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class UserEmailCodeRequest {

    @NotNull
    @NotBlank
    private String password;

    @NotNull
    @NotBlank
    @Email
    private String email;
}
