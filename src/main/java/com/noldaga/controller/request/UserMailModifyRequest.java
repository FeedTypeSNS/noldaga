package com.noldaga.controller.request;


import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter
@NoArgsConstructor
public class UserMailModifyRequest {

    @NotNull
    @NotBlank
    private String password;

    @NotNull
    @Positive
    private Integer codeId;

    @NotNull
    @NotBlank
    private String code;
}
