package com.noldaga.domain.userdto;


import com.noldaga.domain.entity.User;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 엔티티 클래스는 db에 저장할때만 사용하는것이 좋음 : 엔티티 객체의 영향을 준다라는것은 db에 영향을 준다는것을 의미함
 */
@Getter
public class UserDto {

    private Long id;
    private String username;
    private String password;

    private String nickname;
    private Gender gender;
    private LocalDate birthday;
    private String profileImageUrl;
    private String profileMessage;
    private String email;
    private Long totalFollower;
    private Long totalFollowing;
    private UserRole role = UserRole.USER;

    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private LocalDateTime deletedAt;

    private UserDto(Long id, String username, String password, String nickname,Gender gender, LocalDate birthday, String profileImageUrl, String profileMessage, String email, Long totalFollower, Long totalFollowing, UserRole role, LocalDateTime createdAt, LocalDateTime modifiedAt, LocalDateTime deletedAt) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.gender = gender;
        this.birthday = birthday;
        this.profileImageUrl = profileImageUrl;
        this.profileMessage = profileMessage;
        this.email = email;
        this.totalFollower = totalFollower;
        this.totalFollowing = totalFollowing;
        this.role = role;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
        this.deletedAt = deletedAt;
    }

    private UserDto(Long id, String username, String password, UserRole role, LocalDateTime createdAt, LocalDateTime modifiedAt, LocalDateTime deletedAt) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
        this.deletedAt = deletedAt;
    }

    public static UserDto fromEntity(User entity){
        return new UserDto(
                entity.getId(),
                entity.getUsername(),
                entity.getPassword(),
                entity.getNickname(),
                entity.getGender(),
                entity.getBirthday(),
                entity.getProfileImageUrl(),
                entity.getProfileMessage(),
                entity.getEmail(),
                entity.getTotalFollower(),
                entity.getTotalFollowing(),
                entity.getRole(),
                entity.getCreatedAt(),
                entity.getModifiedAt(),
                entity.getDeletedAt());
    }


}
