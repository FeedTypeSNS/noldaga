package com.noldaga.service;

import com.noldaga.exception.ErrorCode;
import com.noldaga.exception.SnsApplicationException;
import com.noldaga.domain.userdto.UserDto;
import com.noldaga.domain.entity.User;
import com.noldaga.module.CodeGenerator;
import com.noldaga.repository.UserRepository;
import com.noldaga.util.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {


    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;
    private final CodeGenerator codeGenerator;
    private final int INIT_PASSWORD_SIZE =10;


    @Value("${jwt.secret-key}")
    private String key;
    @Value("${jwt.token.expired-time-ms}")
    private Long expiredTimeMs;


    //회원가입의 결과를 UserDto 로 넘겨줌 ( 플로우의 결과로서 Dto를 받기 때문에 받는 쪽에서 플로우가 잘 진행됐는지 파악 하기 쉬움)
    @Transactional//예외발생시 회원가입 되지않고 롤백 되어야함 (save 다음에 예외 터지면 롤백 되어야함)
    public UserDto join(String username, String password,String nickname,String email) {
        //username 이 이미 회원가입 되어있는지 확인
        userRepository.findByUsername(username).ifPresent(it -> {
            throw new SnsApplicationException(ErrorCode.DUPLICATED_USERNAME, String.format("%s is duplicated", it.getUsername()));
        });

        //username 이 회원가입이되어있지않아 유효하면 회원으로서 저장하는 로직
        User user = userRepository.save(User.of(username, encoder.encode(password),nickname,email));
//        User user = userRepository.save(User.of(username, password,nickname,email));

        return UserDto.fromEntity(user);

    }

    //토큰 반환
    public String login(String username, String password) {
        //회원으로 등록되어있는 username 인지 체크
        User user = userRepository.findByUsername(username).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", username)));

        //등록이 되어있다면 password가 일치하는지 체크
//        if (!user.getPassword().equals(password)){
        if (!encoder.matches(password, user.getPassword())) {
            throw new SnsApplicationException(ErrorCode.INVALID_PASSWORD);
        }

        //토큰생성
        String token = JwtTokenUtils.generateToken(username, key, expiredTimeMs);

        return token;
    }

    public void validateDuplication(String username){
        userRepository.findByUsername(username).ifPresent(it-> {
            throw new SnsApplicationException(ErrorCode.DUPLICATED_USERNAME, String.format("%s is duplicated", username));
        });
    }


    public UserDto findUsernameByEmail(String emailAddress) {
        return userRepository.findByEmail(emailAddress).map(UserDto::fromEntity).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s is invalid E-mailAddress", emailAddress)));
    }

    public String findEmail(String username) {
        UserDto userDto = userRepository.findByUsername(username).map(UserDto::fromEntity).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", username)));
        return userDto.getEmail();
    }

    public String initPassword(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", username)));

        String newPassword = codeGenerator.generateRandomCode(INIT_PASSWORD_SIZE);
        user.changePassword(encoder.encode(newPassword));

        return newPassword;
    }


//    public UserDto loadUserByUsername(String username) {
//        return userRepository.findByUsername(username).map(UserDto::fromEntity).orElseThrow(() ->
//                new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", username)));
//    }



}
