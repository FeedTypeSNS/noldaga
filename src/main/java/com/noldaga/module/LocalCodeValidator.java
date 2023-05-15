package com.noldaga.module;


import com.noldaga.domain.CodeDto;
import com.noldaga.domain.CodeUserDto;
import com.noldaga.exception.ErrorCode;
import com.noldaga.exception.SnsApplicationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

//@Component
@RequiredArgsConstructor
public class LocalCodeValidator implements CodeValidator {

    private final Map<Integer, String> storage = new ConcurrentHashMap<>();
    private final AtomicInteger keyGenerator = new AtomicInteger(0);

    private final CodeGenerator codeGenerator;

    private final int CODE_SIZE = 6;
    private final String AUTH_FLAG = "*";
    private final String DELIMITER = "/";


    @Override
    public CodeDto generateCodeForJoin(String email) {
        String code = generateRandomString();
        int key = keyGenerator.incrementAndGet();
        storage.put(key, code + DELIMITER + email);

        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.schedule(() -> {
            if (storage.containsKey(key)) {
                storage.remove(key);
            }
        }, 5, TimeUnit.MINUTES);
        executorService.shutdown();

        return CodeDto.of(key, code);
    }

    @Override
    public void validateCodeForJoin(Integer codeId, String codeRequest) {
        String code_email = storage.get(codeId);
        if (code_email == null) {
            throw new SnsApplicationException(ErrorCode.INVALID_CODE_ID);
        }
        String[] split = code_email.split(DELIMITER);
        String code = split[0];
        if (codeRequest.equals(code)) {
//            storage.put(codeId, code_email + AUTH_FLAG);
            return;
        }
        throw new SnsApplicationException(ErrorCode.INVALID_CODE);
    }


    @Override
    public String validateCodeForJoinAgain(Integer codeId, String codeRequest, String emailRequest) {
        String code_email = storage.get(codeId);
        if (code_email == null) {
            throw new SnsApplicationException(ErrorCode.INVALID_CODE_ID); //자바스크립트로 codeId 조작 안했으면 예외터질일은 없긴함
        }
        String[] split = code_email.split(DELIMITER);
        String code = split[0];
        String email = split[1];
        if (!codeRequest.equals(code)) {
            throw new SnsApplicationException(ErrorCode.INVALID_CODE); //자바스크립트로 조작 안했으면 여기서 에외 터질일은 없긴함
        }
        if (!emailRequest.equals(email)) {
            throw new SnsApplicationException(ErrorCode.INVALID_EMAIL); //이메일 인증후 이메일 바꿔서 회원가입 할려하면 예외발생
        }
        storage.remove(codeId);
        return email;
    }

//    @Override
//    public void validateAuthenticatedEmail(Integer codeId, String email) {
//        String code_email_authFlag = storage.get(codeId);
//        if (code_email_authFlag == null) {
//            throw new SnsApplicationException(ErrorCode.INVALID_CODE_ID);
//        }
//        String[] split = code_email_authFlag.split(DELIMITER);
//        String emailWithFlag = split[1];
//        if ((email + AUTH_FLAG).equals(emailWithFlag)) {
//            storage.remove(codeId);
//            return;
//        }
//        throw new SnsApplicationException(ErrorCode.INVALID_EMAIL);
//    }



    @Override
    public CodeDto generateCodeForPassword(String emailAddress, String username) {
        String code = generateRandomString();
        int key = keyGenerator.incrementAndGet();
        storage.put(key, code + DELIMITER + emailAddress + DELIMITER + username);
        return CodeDto.of(key, code);
    }


    //이메일이 DELIMITER 를 포함하면  UserService.initPassword()에서 user not found Exception ErrorCode 발생
    @Override
    public CodeUserDto validateCodeForPassword(Integer codeId, String codeRequest) {
        String code_email_username = storage.get(codeId);
        if (code_email_username == null) {
            throw new SnsApplicationException(ErrorCode.INVALID_CODE_ID);
        }
        String[] split = code_email_username.split(DELIMITER);
        String code = split[0];
        String email = split[1];
        String username = split[2];
        if (codeRequest.equals(code)) {
            storage.remove(codeId);
            return CodeUserDto.of(email, username);
        }
        throw new SnsApplicationException(ErrorCode.INVALID_CODE);
    }




    @Override
    public CodeDto generateCodeForEmailUpdate(String email) {
        String code = generateRandomString();
        int key = keyGenerator.incrementAndGet();
        storage.put(key, code + DELIMITER + email);
        return CodeDto.of(key, code);
    }

    @Override
    public String validateCodeForEmailUpdate(Integer codeId, String codeRequest) {
        String code_email = storage.get(codeId);
        if (code_email == null) {
            throw new SnsApplicationException(ErrorCode.INVALID_CODE_ID);
        }
        String[] split = code_email.split(DELIMITER);
        String code = split[0];
        String email = split[1];
        if (codeRequest.equals(code)) {
            storage.remove(codeId);
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


}


