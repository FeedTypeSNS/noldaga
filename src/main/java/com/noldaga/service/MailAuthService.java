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

    public Integer sendCode(String emailAddress) throws MessagingException, UnsupportedEncodingException {

        CodeDto codeDto = codeValidator.generateCode();
        String message = "인증코드를 입력해주세요 " + codeDto.getCode();
        mailSender.sendEmail(emailAddress,message);
        return codeDto.getCodeId();
    }

    public Integer sendCodeForPassword(String emailAddress, String username) throws MessagingException, UnsupportedEncodingException {

        CodeDto codeDto = codeValidator.generateCodeForPassword(emailAddress,username);
        String message = "인증코드를 입력해주세요 " + codeDto.getCode();
        mailSender.sendEmail(emailAddress,message);
        return codeDto.getCodeId();
    }

    public void sendUsername(String emailAddress, String username) throws MessagingException, UnsupportedEncodingException {
        String message = "해당 이메일로가입된 아이디 입니다 : " + username;
        mailSender.sendEmail(emailAddress,message);
    }

    public void validateCode(Integer codeId, String code) {
        codeValidator.validateCode(codeId, code);

    }
    public CodeUserDto validateCodeForPassword(Integer codeId, String code) {
        return codeValidator.validateCodeForPassword(codeId, code);
    }

    public void sendPassword(String emailAddress, String newPassword) throws MessagingException, UnsupportedEncodingException {
        String message = "초기화된 비밀번호 입니다 : " + newPassword;
        mailSender.sendEmail(emailAddress,message);
    }
}
