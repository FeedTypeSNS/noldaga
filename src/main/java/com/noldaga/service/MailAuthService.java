package com.noldaga.service;


import com.noldaga.domain.CodeDto;
import com.noldaga.domain.CodeUserDto;
import com.noldaga.module.CodeValidator;
import com.noldaga.module.MailSender;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;

@Service
@RequiredArgsConstructor
public class MailAuthService {

    private final MailSender mailSender;
    private final CodeValidator codeValidator;



    public Integer sendCodeForJoin(String emailAddress) throws MessagingException, UnsupportedEncodingException {

        CodeDto codeDto = codeValidator.generateCodeForJoin(emailAddress);
        String message = "인증코드를 입력해주세요 " + codeDto.getCode();
        mailSender.sendEmail(emailAddress,message);
        return codeDto.getCodeId();
    }

    public void validateCodeForJoin(Integer codeId, String code) {
        codeValidator.validateCodeForJoin(codeId, code);

    }


    public void validateAuthenticatedEmail(Integer codeId,String email){
//        codeValidator.validateAuthenticatedEmail(codeId,email);
    }





    public Integer sendCodeForPassword(String emailAddress, String username) throws MessagingException, UnsupportedEncodingException {

        CodeDto codeDto = codeValidator.generateCodeForPassword(emailAddress,username);
        String message = "인증코드를 입력해주세요   " + codeDto.getCode();
        mailSender.sendEmail(emailAddress,message);
        return codeDto.getCodeId();
    }
    public CodeUserDto validateCodeForPassword(Integer codeId, String code) {
        return codeValidator.validateCodeForPassword(codeId, code);
    }







    public void sendPassword(String emailAddress, String newPassword) throws MessagingException, UnsupportedEncodingException {
        String message = "초기화된 비밀번호 입니다   " + newPassword;
        mailSender.sendEmail(emailAddress,message);
    }

    public void sendUsernames(String emailAddress, String usernames) throws MessagingException, UnsupportedEncodingException {
        String message = "해당 이메일로 가입된 아이디 목록입니다   " + usernames;
        mailSender.sendEmail(emailAddress,message);
    }

    public Integer sendCodeForEmail(String email) throws MessagingException, UnsupportedEncodingException {

        CodeDto codeDto = codeValidator.generateCodeForEmailUpdate(email);
        String message = "인증코드를 입력해주세요 " + codeDto.getCode();
        mailSender.sendEmail(email,message);
        return codeDto.getCodeId();
    }

    public String validateCodeForEmail(Integer codeId, String code) {
        return codeValidator.validateCodeForEmailUpdate(codeId, code);
    }

    public String validateCodeForJoinAgain(Integer codeId, String code, String email) {
        return codeValidator.validateCodeForJoinAgain(codeId,code,email);
    }
}
