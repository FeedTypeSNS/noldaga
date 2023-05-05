package com.noldaga.controller.request;

import com.noldaga.domain.userdto.Gender;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;


@Getter
@NoArgsConstructor
public class UserInfoModifyRequest {

    @NotNull
    @NotBlank
    private String nickname;
    private Gender gender;
    private LocalDate birthday;

}
