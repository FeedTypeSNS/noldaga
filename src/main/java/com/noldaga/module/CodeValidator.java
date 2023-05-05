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


    public void validateCode(Integer codeId, String codeRequest) {
        String code_email = storage.get(codeId);
        if(code_email==null){
            throw new SnsApplicationException(ErrorCode.INVALID_CODE_ID);
        }
        String[] split = code_email.split("_");
        if (split[0].equals(codeRequest)) {
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
        String[] split = code_email_authFlag.split("_");
        if(split[1].equals(email+AUTH_FLAG)){
            storage.remove(codeId);
            return ;
        }
        throw new SnsApplicationException(ErrorCode.INVALID_EMAIL);
    }

    public CodeUserDto validateCodeForPassword(Integer codeId, String codeRequest) {
        String code_email_username = storage.get(codeId);
        if (code_email_username == null) {
            throw new SnsApplicationException(ErrorCode.INVALID_CODE_ID);
        }
        String[] split = code_email_username.split("_");
        if (split[0].equals(codeRequest)) {
            storage.remove(codeId);
            return CodeUserDto.of(split[1], split[2]);
        }
        throw new SnsApplicationException(ErrorCode.INVALID_CODE);
    }


    public CodeDto generateCode(String email) {
        String code = generateRandomCode();
        int key = keyGenerator.incrementAndGet();
        storage.put(key, code+"_"+email);
        return CodeDto.of(key, code);
    }

    public CodeDto generateCodeForPassword(String emailAddress, String username) {
        String code = generateRandomCode();
        int key = keyGenerator.incrementAndGet();
        storage.put(key, code + "_" + emailAddress + "_" + username);
        return CodeDto.of(key, code);
    }


    private String generateRandomCode(){
        return codeGenerator.generateRandomCode(CODE_SIZE);
    }


}
