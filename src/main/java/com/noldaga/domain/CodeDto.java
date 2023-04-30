package com.noldaga.domain;


import com.noldaga.controller.response.CodeIdResponse;
import lombok.Getter;

@Getter
public class CodeDto {

    private Integer codeId;
    private String code;

    private CodeDto(Integer codeId, String code) {
        this.codeId = codeId;
        this.code = code;
    }

    public static CodeDto of(Integer codeId, String code) {
        return new CodeDto(codeId, code);
    }

    public static CodeIdResponse fromCodeDto(CodeDto codeDto) {
        return new CodeIdResponse(codeDto.getCodeId());
    }
}
