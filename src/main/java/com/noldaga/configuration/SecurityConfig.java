package com.noldaga.configuration;


import com.noldaga.domain.userdto.UserDto;
import com.noldaga.domain.userdto.security.KakaoOAuth2Response;
import com.noldaga.exception.ErrorCode;
import com.noldaga.exception.SnsApplicationException;
import com.noldaga.repository.UserRepository;
import com.noldaga.service.AnonymousService;
import com.noldaga.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.util.UUID;

@Configuration
public class SecurityConfig {

    @Value("${jwt.secret-key}")
    private String key;
    @Value("${jwt.token.expired-time-ms}")
    private Long expiredTimeMs;


    @Bean
    public BCryptPasswordEncoder encodePassword() {
        return new BCryptPasswordEncoder();
    }

    //시큐리티가 사용하는거 (jwt토큰필터)
    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository) {//UserDetailsService.loadByUsername() 의 로직
        return username -> userRepository
                .findByUsername(username).map(UserDto::fromEntity).orElseThrow(() ->
                        new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", username))
                );
    }

    //OAuth2가 사용하는거
    @Bean
    public OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService
            (UserService userService, BCryptPasswordEncoder encoder) { //OAuth2UserService.loadUser() 의 로직

        final DefaultOAuth2UserService delegate = new DefaultOAuth2UserService(); //이녀석은 빈 자동등록 아니여서 주입받는 것이 아니라 new로 생성해줘야함

        return userRequest -> {
            //카카오 정보를 얻어옴
            OAuth2User oAuth2User = delegate.loadUser(userRequest);

            //카카오 정보를 parsing
            KakaoOAuth2Response kakaoResponse = KakaoOAuth2Response.from(oAuth2User.getAttributes());
            String registrationId = userRequest.getClientRegistration().getRegistrationId(); //.yaml 에 있는내용.(kakao)
            String providerId = String.valueOf(kakaoResponse.getId());
            String username = registrationId + "_" + providerId;
            String dummyPassword = encoder.encode("dummy" + UUID.randomUUID());

            return userService.loadUserByUsername(username).orElseGet(() ->
                    userService.join(username, dummyPassword, kakaoResponse.nickname(), kakaoResponse.email())
            );


            //카카오 정보로 db 에서 유저를 load 하고, db에 가입되어있지않으면 가입시킴 : 인증을 카카오에서 대신 해주고 이후 내부 로직들은 db정보들을 이용함
        };
    }


//    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler(){
        return ((request, response, exception) -> response.sendRedirect(""));
    }


}
