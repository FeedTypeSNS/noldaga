package com.noldaga.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;


@RequiredArgsConstructor
@Service
public class MailSendService {

    private final JavaMailSender javaMailSender;

    @Value("${mail.sender}")
    private String sender;

    public void sendEmail(String recipientMailAddress,String text) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = createMessage(recipientMailAddress,text);
        javaMailSender.send(message);

    }


    private MimeMessage createMessage(String recipient,String text) throws UnsupportedEncodingException, MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        mimeMessage.addRecipients(Message.RecipientType.TO, recipient);//인증요청한 유저메일
        mimeMessage.setSubject("메일인증요청에관한 이메일입니다.");//제목
        StringBuilder sb = new StringBuilder();
        String msgText = "기본메세지";

        sb.append(msgText).append("\n").append("사이트 화면에서 인증코드를 입력해 주세요 : ").append(text);
        mimeMessage.setText(sb.toString(), "utf-8", "html");
        mimeMessage.setFrom(new InternetAddress(sender, "noldaga"));//송신자
        return  mimeMessage;
    }




}
