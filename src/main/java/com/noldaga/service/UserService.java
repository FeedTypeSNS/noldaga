package com.noldaga.service;

import com.noldaga.domain.alarm.AlarmDto;
import com.noldaga.domain.entity.Alarm;
import com.noldaga.domain.userdto.Gender;
import com.noldaga.exception.ErrorCode;
import com.noldaga.exception.SnsApplicationException;
import com.noldaga.domain.userdto.UserDto;
import com.noldaga.domain.entity.User;
import com.noldaga.repository.AlarmRepository;
import com.noldaga.repository.UserRepository;
import com.noldaga.util.ConstUtil;
import com.noldaga.util.S3Uploader;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final MailAuthService mailAuthService;
    private final BCryptPasswordEncoder encoder;

    private final AlarmRepository alarmRepository;

    private final S3Uploader s3Uploader;

    @Value("${com.noldaga.upload.path}")
    private String directoryPath;

    public UserDto searchUserById(Long id){
        return userRepository.findById(id).map(UserDto::fromEntity).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%No user with %d UserId", id)));
    }

    public UserDto searchUserByUsername(String username){
        return userRepository.findByUsername(username).map(UserDto::fromEntity).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s is not founded", username)));
    }

    @Transactional
    public UserDto modifyMyProfile(MultipartFile multipartFile,String nickname,String message,String beforeUrl, String username) throws IOException {

        User user = userRepository.findByUsername(username).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s is not founded", username)));


        String afterUrl = beforeUrl;
        if(containImageFile(multipartFile)) {
            afterUrl =s3Uploader.upload(multipartFile,"/user");
            if (isNotDefaultImage(beforeUrl)) {
                s3Uploader.deleteImage(beforeUrl);
            }
        }

        user.modifyProfile(nickname,message,afterUrl);

        return UserDto.fromEntity(user);
    }

    private boolean isNotDefaultImage(String beforeUrl) {
        return beforeUrl != null && !beforeUrl.isEmpty() && !beforeUrl.equals(ConstUtil.USER_DEFAULT_IMG_URL);
    }

    private boolean containImageFile(MultipartFile multipartFile) {
        return multipartFile != null && !multipartFile.isEmpty();
    }


    @Transactional
    public UserDto modifyMyUserinfo(String nickname, Gender gender, LocalDate birthday, String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s is not founded", username)));

        user.modifyInfo(nickname,gender,birthday);
        return UserDto.fromEntity(user);
    }

    public Integer sendCodeToModifyEmail(String passwordInput, UserDto loginUserDto,String email) throws MessagingException, UnsupportedEncodingException {
        //사용자가 modifyMyEmail()에서 비번 틀려서 빠꾸 먹는것을 미리 검증해줌 ( 사용자 편하도록)
        if (!encoder.matches(passwordInput, loginUserDto.getPassword())) {
            throw new SnsApplicationException(ErrorCode.INVALID_PASSWORD);
        }
        Integer codeId = mailAuthService.sendCodeForEmail(email);
        return codeId;
    }

    @Transactional
    public UserDto modifyMyEmail(Integer codeId,String code,String passwordInput,UserDto loginUserDto){

        if (!encoder.matches(passwordInput, loginUserDto.getPassword())) {
            throw new SnsApplicationException(ErrorCode.INVALID_PASSWORD);
        }

        String emailAuthenticated = mailAuthService.validateCodeForEmail(codeId, code); //코드 대조후 전송했던 이메일을 꺼내옴

        String username = loginUserDto.getUsername();
        User user = userRepository.findByUsername(username).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s is not founded", username)));

        user.modifyEmail(emailAuthenticated);
        return UserDto.fromEntity(user);
    }

    @Transactional
    public UserDto modifyPassword(String newPassword,String currentPasswordInput,UserDto loginUserDto) {
        if (!encoder.matches(currentPasswordInput, loginUserDto.getPassword())) {
            throw new SnsApplicationException(ErrorCode.INVALID_PASSWORD);
        }

        String username= loginUserDto.getUsername();
        User user = userRepository.findByUsername(username).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s is not founded", username)));

        user.modifyPassword(encoder.encode(newPassword));

        return UserDto.fromEntity(user);
    }





    //카카오 로그인용
    @Transactional//예외발생시 회원가입 되지않고 롤백 되어야함 (save 다음에 예외 터지면 롤백 되어야함)
    public UserDto join(String username, String password,String nickname,String email) {
        //username 이 이미 회원가입 되어있는지 확인
        userRepository.findByUsername(username).ifPresent(it -> {
            throw new SnsApplicationException(ErrorCode.DUPLICATED_USERNAME, String.format("%s is duplicated", it.getUsername()));
        });

        //username 이 회원가입이되어있지않아 유효하면 회원으로서 저장하는 로직
        User user = userRepository.save(User.of(username, encoder.encode(password),nickname,email));

        return UserDto.fromEntity(user);

    }

    public Optional<UserDto> loadUserByUsername(String username){
        return userRepository.findByUsername(username).map(UserDto::fromEntity);
    }//OAuth2UserService 에서 사용되는 메서드: 인증용이여서 웬만하면 안쓰는쪽으로....



    @Transactional(readOnly = true)
    public Page<AlarmDto> getAlarms(Long userId, Pageable pageable) {


        return alarmRepository.findAllByToUserIdOrderByIdDesc(userId, pageable).map(AlarmDto::fromEntity);
    }


    @Transactional
    public void deleteAlarm(Long alarmId, UserDto loginUserDto) {
        Alarm alarm = alarmRepository.findById(alarmId).orElseThrow(() -> new SnsApplicationException(ErrorCode.ALARM_NOT_FOUND));
        if (loginUserDto.getId() != alarm.getToUserId()) {
            throw new SnsApplicationException(ErrorCode.INVALID_PERMISSION);
        }

        alarmRepository.deleteById(alarmId);
    }

    @Transactional
    public void readAlarm(Long loginUserId, Long alarmId){
        Alarm alarm = alarmRepository.findById(alarmId).orElseThrow(() -> new SnsApplicationException(ErrorCode.ALARM_NOT_FOUND));
        if (loginUserId != alarm.getToUserId()) {
            throw new SnsApplicationException(ErrorCode.INVALID_PERMISSION);
        }

        alarm.readAlarm();
    }


}
