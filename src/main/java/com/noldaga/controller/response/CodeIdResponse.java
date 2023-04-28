package com.noldaga.controller.response;


import com.noldaga.domain.CodeDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CodeIdResponse {

    private Integer codeId;

    public static CodeIdResponse fromCodeDto(CodeDto codeDto) {
        return new CodeIdResponse(codeDto.getCodeId());
    }

}
