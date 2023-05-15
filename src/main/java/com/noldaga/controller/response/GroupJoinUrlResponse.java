package com.noldaga.controller.response;


import com.noldaga.domain.CodeDto;
import com.noldaga.util.ConstUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GroupJoinUrlResponse {


    private String url;


    public static GroupJoinUrlResponse fromCodeDto (CodeDto codeDto){
        StringBuilder sb = new StringBuilder();
        Integer codeId = codeDto.getCodeId();
        String code = codeDto.getCode();
        Long groupId = codeDto.getGroupId();

        String url = sb.append(ConstUtil.DOMAIN_URL).append("/api/groups/member/join-by-link")
                .append("?codeId=").append(codeId)
                .append("&code=").append(code)
                .append("&groupId=").append(groupId).toString();

        return new GroupJoinUrlResponse(url);
    }
}
