package com.noldaga.controller;


import com.noldaga.controller.request.MailAuthRequest;
import com.noldaga.service.MailAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;

@RestController
@RequiredArgsConstructor
public class MailAuthController {

    private final MailAuthService mailAuthService;

    @PostMapping("/mail")
    public String email(@RequestBody MailAuthRequest request) throws MessagingException, UnsupportedEncodingException {

        mailAuthService.email(request.getRecipient());

        return "MailOK";
    }
}
