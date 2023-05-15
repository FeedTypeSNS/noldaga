package com.noldaga.domain;


import com.noldaga.controller.response.CodeIdResponse;
import lombok.Getter;

@Getter
public class CodeDto {

    private Integer codeId;
    private String code;

    private Long groupId;
    private CodeDto(Integer codeId, String code) {
        this.codeId = codeId;
        this.code = code;
    }

    private CodeDto(Integer codeId, String code, Long groupId) {
        this.codeId = codeId;
        this.code = code;
        this.groupId = groupId;
    }

    public static CodeDto of(Integer codeId, String code) {
        return new CodeDto(codeId, code);
    }

    public static CodeIdResponse fromCodeDto(CodeDto codeDto) {
        return new CodeIdResponse(codeDto.getCodeId());
    }

    public static CodeDto of(Integer codeId, String code, Long groupId) {
        return new CodeDto(codeId, code, groupId);
    }
}
