package com.noldaga.configuration.oauth;


import com.noldaga.util.JwtTokenUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final int A_MONTH = 60 * 60 * 24 * 30;
    @Value("${jwt.secret-key}")
    private String key;
    @Value("${jwt.token.expired-time-ms}")
    private Long expiredTimeMs;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        System.out.println("OAuth2SuccessHandler.onAuthenticationSuccess");

        String token = JwtTokenUtils.generateToken(authentication.getName(), key, expiredTimeMs);

        generateTokenCookie(response, token);

        response.sendRedirect("/");
    }

    private void generateTokenCookie(HttpServletResponse response, String token) {
        Cookie tokenCookie = new Cookie("tokenCookie", token);
        tokenCookie.setMaxAge(A_MONTH);
        response.addCookie(tokenCookie);
        tokenCookie.setHttpOnly(true); //자바스크립트로 조작 금지
        tokenCookie.setPath("/");
        response.addCookie(tokenCookie);
    }
}
