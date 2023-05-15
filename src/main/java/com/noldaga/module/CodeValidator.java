package com.noldaga.module;

import com.noldaga.domain.CodeDto;
import com.noldaga.domain.CodeUserDto;
import lombok.RequiredArgsConstructor;


public interface CodeValidator {
    CodeDto generateCodeForJoin(String email);

    void validateCodeForJoin(Integer codeId, String codeRequest);

    String validateCodeForJoinAgain(Integer codeId, String codeRequest, String emailRequest);

//    void validateAuthenticatedEmail(Integer codeId, String email);

    CodeDto generateCodeForPassword(String emailAddress, String username);

    //이메일이 DELIMITER 를 포함하면  UserService.initPassword()에서 user not found Exception ErrorCode 발생
    CodeUserDto validateCodeForPassword(Integer codeId, String codeRequest);

    CodeDto generateCodeForEmailUpdate(String email);

    String validateCodeForEmailUpdate(Integer codeId, String codeRequest);

    String generateRandomString();

    CodeDto generateCodeForGroup(Long groupId);

    void validateCodeForGroup(Integer codeId, String code, Long groupId);
}
