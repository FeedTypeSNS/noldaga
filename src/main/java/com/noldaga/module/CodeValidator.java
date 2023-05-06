package com.noldaga.module;


import com.noldaga.domain.CodeDto;
import com.noldaga.domain.CodeUserDto;
import com.noldaga.exception.ErrorCode;
import com.noldaga.exception.SnsApplicationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@RequiredArgsConstructor
public class CodeValidator {
    //todo 시간 제한을 두어서 코드가 사라지게끔 해야함 -> redis에서 시간제한 해서 사용하면될듯
    private final Map<Integer, String> storage = new ConcurrentHashMap<>();
    private final AtomicInteger keyGenerator = new AtomicInteger(0);

    private final CodeGenerator codeGenerator;

    private final int CODE_SIZE =  6;
    private final String AUTH_FLAG="*";
    private final String DELIMITER="/";


    public void validateCode(Integer codeId, String codeRequest) {
        String code_email = storage.get(codeId);
        if(code_email==null){
            throw new SnsApplicationException(ErrorCode.INVALID_CODE_ID);
        }
        String[] split = code_email.split(DELIMITER);
        String code= split[0];
        if (codeRequest.equals(code)) {
            storage.put(codeId, code_email + AUTH_FLAG);
            return ;
        }
        throw new SnsApplicationException(ErrorCode.INVALID_CODE);
    }

    public void validateAuthenticatedEmail(Integer codeId,String email){
        String code_email_authFlag = storage.get(codeId);
        if(code_email_authFlag==null){
            throw new SnsApplicationException(ErrorCode.INVALID_CODE_ID);
        }
        String[] split = code_email_authFlag.split(DELIMITER);
        String emailWithFlag = split[1];
        if((email+AUTH_FLAG).equals(emailWithFlag)){
            storage.remove(codeId);
            return ;
        }
        throw new SnsApplicationException(ErrorCode.INVALID_EMAIL);
    }


    //이메일이 DELIMITER 를 포함하면 user not found Exception ErrorCode 발생
    public CodeUserDto validateCodeForPassword(Integer codeId, String codeRequest) {
        String code_email_username = storage.get(codeId);
        if (code_email_username == null) {
            throw new SnsApplicationException(ErrorCode.INVALID_CODE_ID);
        }
        String[] split = code_email_username.split(DELIMITER);
        String code = split[0];
        String email = split[1];
        String username=split[2];
        if (codeRequest.equals(code)) {
            storage.remove(codeId);
            return CodeUserDto.of(email, username);
        }
        throw new SnsApplicationException(ErrorCode.INVALID_CODE);
    }


    public CodeDto generateCode(String email) {
        String code = generateRandomString();
        int key = keyGenerator.incrementAndGet();
        storage.put(key, code+DELIMITER+email);
        return CodeDto.of(key, code);
    }

    public CodeDto generateCodeForPassword(String emailAddress, String username) {
        String code = generateRandomString();
        int key = keyGenerator.incrementAndGet();
        storage.put(key, code + DELIMITER + emailAddress + DELIMITER + username);
        return CodeDto.of(key, code);
    }


    private String generateRandomString(){
        return codeGenerator.generateRandomString(CODE_SIZE);
    }


}
