package com.noldaga.domain;


import com.noldaga.domain.entity.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

/**
 * 엔티티 클래스는 db에 저장할때만 사용하는것이 좋음 : 엔티티 객체의 영향을 준다라는것은 db에 영향을 준다는것을 의미함
 */
@Getter
public class UserDto {

    private Long id;
    private String username;
    private String password;
    private UserRole role;

    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    private LocalDateTime deletedAt;

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
                entity.getRole(),
                entity.getCreatedAt(),
                entity.getModifiedAt(),
                entity.getDeletedAt());
    }


}
