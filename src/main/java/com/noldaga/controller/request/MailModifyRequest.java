package com.noldaga.controller.request;


import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter
@NoArgsConstructor
public class MailModifyRequest {
    @NotNull
    @NotBlank
    @Email
    private String email;

    @Positive
    private Integer codeId;
}
