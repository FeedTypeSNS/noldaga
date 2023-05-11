package com.noldaga.domain.userdto;


import com.noldaga.domain.entity.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 엔티티 클래스는 db에 저장할때만 사용하는것이 좋음 : 엔티티 객체의 영향을 준다라는것은 db에 영향을 준다는것을 의미함
 */
@Getter
public class UserDto implements UserDetails, OAuth2User {

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

    Map<String, Object> oAuth2Attributes;

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

        if(entity ==null) return null;
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


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(this.getRole().toString()));
    }

    //todo 하드 딜리트를 하게되면 true로 바꿔줘야함
    @Override
    public boolean isAccountNonExpired() {
        return this.deletedAt == null;
//        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.deletedAt == null;
//        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.deletedAt == null;
//        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.deletedAt == null;
//        return true;
    }

    //Oauth
    @Override
    public Map<String, Object> getAttributes() {
        return oAuth2Attributes;
    }

    @Override
    public String getName() {
        return this.username;
    }
}
