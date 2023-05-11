package com.noldaga.service;


import com.noldaga.domain.CodeUserDto;
import com.noldaga.domain.entity.User;
import com.noldaga.domain.userdto.UserDto;
import com.noldaga.exception.ErrorCode;
import com.noldaga.exception.SnsApplicationException;
import com.noldaga.module.CodeGenerator;
import com.noldaga.repository.UserRepository;
import com.noldaga.util.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
public class AnonymousService {

    private final UserRepository userRepository;
    private final MailAuthService mailAuthService;
    private final BCryptPasswordEncoder encoder;
    private final CodeGenerator codeGenerator;

    @Value("${jwt.secret-key}")
    private String key;
    @Value("${jwt.token.expired-time-ms}")
    private Long expiredTimeMs;
    private final int INIT_PASSWORD_SIZE =10;
    private final int MAX_ACCOUNTS_PER_EMAIL=30;

    //회원가입 1 : 아이디 중복검사
    public void validateDuplication(String username){
        userRepository.findByUsername(username).ifPresent(it-> {
            throw new SnsApplicationException(ErrorCode.DUPLICATED_USERNAME, String.format("%s is duplicated", username));
        });
    }


    //회원가입 2 : 이메일 인증 : 이메일로 코드전송
    public Integer sendCodeForJoin(String email) throws MessagingException, UnsupportedEncodingException {
        if(MAX_ACCOUNTS_PER_EMAIL<= userRepository.countByEmail(email)){
            throw new SnsApplicationException(ErrorCode.EMAIL_LIMIT_EXCEEDED);
        }

        Integer codeId = mailAuthService.sendCodeForJoin(email);
        return codeId;
    }

    //회원가입 3 : 코드대조
    public void validateCodeForJoin(Integer codeId, String code){
        mailAuthService.validateCodeForJoin(codeId,code);
    }

    //회원가입의 결과를 UserDto 로 넘겨줌 ( 플로우의 결과로서 Dto를 받기 때문에 받는 쪽에서 플로우가 잘 진행됐는지 파악 하기 쉬움)
    //예외발생시 회원가입 되지않고 롤백 되어야함 (save 다음에 예외 터지면 롤백 되어야함)
    //회원가입 4 : 회원가입
    @Transactional
    public UserDto join(Integer codeId, String code, String email, String username, String password, String nickname){

        userRepository.findByUsername(username).ifPresent(it -> {
            throw new SnsApplicationException(ErrorCode.DUPLICATED_USERNAME, String.format("%s is duplicated", it.getUsername()));
        });

        //todo 테스트환경에서는 주석을 해주장
        //이거 주석 해야 포스트맨으로 인증코드없이 회원가입 가능 : 중복검사를 앞에서 안하고 뒤에서 해주게되면, 포스트맨으로 아이디중복으로 하고 코드 맞춰주면 회원가입은 안되고 코드만 사라짐
        String emailAuthenticated = mailAuthService.validateCodeForJoinAgain(codeId, code, email);
//        String emailAuthenticated ="asdf";



        //회원가입시 횟수 넘으면 이메일 코드조차 보낼수 없어서 이 로직은 무조건 false 이긴한데 안전빵으로 넣어줌
        // (코드가 있어야 회원가입 가능해서 코드없으면 포스트맨으로도 못함)
        if (userRepository.countByEmail(emailAuthenticated) >= MAX_ACCOUNTS_PER_EMAIL) {
            throw new SnsApplicationException(ErrorCode.EMAIL_LIMIT_EXCEEDED);
        }

        User user = userRepository.save(User.of(username, encoder.encode(password),nickname,emailAuthenticated));
        return UserDto.fromEntity(user);
    }


    //비번찾기 1 : 가입된 이메일로 코드전송
    public Integer sendCodeToFindPassword(String username) throws MessagingException, UnsupportedEncodingException {
        UserDto userDto = userRepository.findByUsername(username).map(UserDto::fromEntity).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", username)));

        Integer codeId = mailAuthService.sendCodeForPassword(userDto.getEmail(), username);
        return codeId;
    }


    //비번찾기 2 : 코드 검증후 비밀번호 초기화
    @Transactional
    public void initPassword(Integer codeId, String code) throws MessagingException, UnsupportedEncodingException {
        CodeUserDto codeUserDto = mailAuthService.validateCodeForPassword(codeId, code);

        String username =codeUserDto.getUsername();
        User user =userRepository.findByUsername(username).orElseThrow(()->
                new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", username)));
        String newPassword =codeGenerator.generateRandomString(INIT_PASSWORD_SIZE);
        user.changePassword(encoder.encode(newPassword));


        //메일 인증에 성공했다면(코드를 잘 입력했다면 여기서 에러날 일은 없긴함 : 네이버 서버만 문제 없으면)
        mailAuthService.sendPassword(codeUserDto.getEmail(),newPassword);
    }


    //아이디찾기
    public void findUsername(String email) throws MessagingException, UnsupportedEncodingException {
        List<String> usernameList = userRepository.findAllUsernameByEmail(email);
        if (usernameList.isEmpty()) {
            throw new SnsApplicationException(ErrorCode.ACCOUNT_NOT_FOUND, String.format("No Account associated withe email: %s", email));
        }

        String usernames = usernameList.stream().collect(Collectors.joining(" , "));
        mailAuthService.sendUsernames(email,usernames);
    }



    //로그인
    public String login(String username, String passwordInput){

        //회원으로 등록되어있는 username 인지 체크
        User user =userRepository.findByUsername(username).orElseThrow(()->
                new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", username)));

        //등록이 되어있다면 password가 일치하는지 체크
        if (!encoder.matches(passwordInput, user.getPassword())) {
            throw new SnsApplicationException(ErrorCode.INVALID_PASSWORD);
        }

        //토큰생성
        String token= JwtTokenUtils.generateToken(username,key,expiredTimeMs);

        return token;
    }


}
