package com.noldaga.module;

import com.noldaga.util.ConstUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;


@RequiredArgsConstructor
@Component
public class MailSender {

    private final JavaMailSender javaMailSender;


    private final String SLOGAN = "놀다가~";

    private final String EMAIL_LOGO_URL = ConstUtil.EMAIL_SERVICE_LOGO_URL;
    @Value("${mail.sender}")
    private String sender;

    public void sendEmail(String recipientMailAddress,String text) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = createMessage(recipientMailAddress,text);
        javaMailSender.send(message);

    }


    private MimeMessage createMessage(String recipient,String text) throws UnsupportedEncodingException, MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        mimeMessage.addRecipients(Message.RecipientType.TO, recipient);//인증요청한 유저메일
        mimeMessage.setSubject("안녕하세요 놀다가~ 입니다.");//제목


        mimeMessage.setText(getTotalMessage(text), "utf-8", "html");
        mimeMessage.setFrom(new InternetAddress(sender, "noldaga"));//송신자
        return  mimeMessage;
    }


    private String getTotalMessage(String text){

        return "<div style=\"text-align: center; background-color: #f5f5f5; padding: 20px;\">\n" +
                "  <h1 style=\"font-size: 24px; font-weight: bold; color: #333; margin-bottom: 20px;\">"+SLOGAN+"</h1>\n" +
                "  <div style=\"max-width: 600px; margin: 0 auto; background-color: #fff; border: 1px solid #ccc; border-radius: 4px; padding: 20px;\">\n" +
                "  <img src=\""+ EMAIL_LOGO_URL +"\"/>"+
                "  </div>\n <br>" +
                text+
                "</div>";

    }

}
