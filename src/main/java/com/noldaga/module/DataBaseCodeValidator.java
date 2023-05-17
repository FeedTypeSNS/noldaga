package com.noldaga.module;

import com.noldaga.domain.CodeDto;
import com.noldaga.domain.CodeUserDto;
import com.noldaga.domain.entity.Code;
import com.noldaga.exception.ErrorCode;
import com.noldaga.exception.SnsApplicationException;
import com.noldaga.repository.CodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class DataBaseCodeValidator implements CodeValidator{


    private final CodeGenerator codeGenerator;
    private final CodeRepository codeRepository;

    private final int CODE_SIZE = 6;

    @Override
    public CodeDto generateCodeForJoin(String email) {
        String randomString = generateRandomString();
        Code code = Code.of(randomString, email);

        codeRepository.saveAndFlush(code);

        return CodeDto.of(code.getId(), code.getRandomCode());
    }

    @Override
    public void validateCodeForJoin(Integer codeId, String codeRequest) {
        Code code = codeRepository.findById(codeId).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.INVALID_CODE_ID));

        if (codeRequest.equals(code.getRandomCode())) {
            return;
        }
        throw new SnsApplicationException(ErrorCode.INVALID_CODE);
    }

    @Override
    public String validateCodeForJoinAgain(Integer codeId, String codeRequest, String emailRequest) {

        Code code = codeRepository.findById(codeId).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.INVALID_CODE_ID));


        String randomCode = code.getRandomCode();
        String email = code.getEmail();
        if (!codeRequest.equals(randomCode)) {
            throw new SnsApplicationException(ErrorCode.INVALID_CODE); //자바스크립트로 조작 안했으면 여기서 에외 터질일은 없긴함
        }
        if (!emailRequest.equals(email)) {
            throw new SnsApplicationException(ErrorCode.INVALID_EMAIL); //이메일 인증후 이메일 바꿔서 회원가입 할려하면 예외발생
        }
        codeRepository.delete(code);
        return email;
    }


    @Override
    public CodeDto generateCodeForPassword(String emailAddress, String username) {

        String randomString = generateRandomString();
        Code code = Code.of(randomString, emailAddress, username);
        codeRepository.saveAndFlush(code);
        int key = code.getId();
        return CodeDto.of(key, randomString);
    }

    @Override
    public CodeUserDto validateCodeForPassword(Integer codeId, String codeRequest) {

        Code code = codeRepository.findById(codeId).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.INVALID_CODE_ID));


        String randomCode = code.getRandomCode();
        String email = code.getEmail();
        String username = code.getUsername();
        if (codeRequest.equals(randomCode)) {
            codeRepository.delete(code);
            return CodeUserDto.of(email, username);
        }
        throw new SnsApplicationException(ErrorCode.INVALID_CODE);
    }

    @Override
    public CodeDto generateCodeForEmailUpdate(String email) {
        String randomString = generateRandomString();
        Code code = Code.of(randomString, email);
        codeRepository.saveAndFlush(code);
        int key = code.getId();
        return CodeDto.of(key, randomString);
    }

    @Override
    public String validateCodeForEmailUpdate(Integer codeId, String codeRequest) {

        Code code = codeRepository.findById(codeId).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.INVALID_CODE_ID));

        String randomCode = code.getRandomCode();
        String email = code.getEmail();
        if (codeRequest.equals(randomCode)) {
            codeRepository.delete(code);
            return email;
        }
        throw new SnsApplicationException(ErrorCode.INVALID_CODE);
    }

    @Override
    public String generateRandomString() {
        return codeGenerator.generateRandomString(CODE_SIZE);
    }

    @Override
    public CodeDto generateCodeForGroup(Long groupId) {
        return null;
    }

    @Override
    public void validateCodeForGroup(Integer codeId, String code, Long groupId) {

    }

//    @Override
//    public CodeDto generateCodeForGroup(Long groupId) {
//
//        String randomString = generateRandomString();
//        Code code = Code.of(randomString,groupId);
//        codeRepository.saveAndFlush(code);
//
//        return CodeDto.of(code.getId(), code.getRandomCode(), code.getGroupId());
//    }

//    @Override
//    public void validateCodeForGroup(Integer codeId, String randomCode, Long groupId) {
//        Code code = codeRepository.findById(codeId).orElseThrow(() ->
//                new SnsApplicationException(ErrorCode.INVALID_CODE_ID)
//        );
//
//        if (!code.getRandomCode().equals(randomCode)) {
//            throw new SnsApplicationException(ErrorCode.INVALID_CODE);
//        }
//
//        if (code.getGroupId() != groupId) {
//            throw new SnsApplicationException(ErrorCode.INVALID_GROUP_ID);
//        }

//    }

}
