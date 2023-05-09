//package com.noldaga.domain.userdto.security;
//
//import com.noldaga.domain.entity.User;
//import com.noldaga.domain.userdto.UserDto;
//import com.noldaga.domain.userdto.UserRole;
//import lombok.Getter;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.oauth2.core.user.OAuth2User;
//
//import java.time.LocalDateTime;
//import java.util.Collection;
//import java.util.List;
//import java.util.Map;
//
//
//@Getter
//public class UserDetailsImpl implements UserDetails , OAuth2User {
//
//    private Long id;
//    private String username;
//    private String password;
//    private String nickname;
//
//    private String email;
//    private UserRole role;
//
//    //oAuth2User 에서 추가
//    Map<String, Object> oAuth2Attributes;
//
//    private LocalDateTime createdAt;
//    private LocalDateTime modifiedAt;
//
//    private LocalDateTime deletedAt;
//
//    private UserDetailsImpl(Long id, String username, String password,String nickname,String email, UserRole role,
//                            LocalDateTime createdAt, LocalDateTime modifiedAt, LocalDateTime deletedAt, Map<String,Object> oAuth2Attributes) {
//        this.id = id;
//        this.username = username;
//        this.password = password;
//        this.nickname = nickname;
//        this.email = email;
//        this.role = role;
//        this.createdAt = createdAt;
//        this.modifiedAt = modifiedAt;
//        this.deletedAt = deletedAt;
//        this.oAuth2Attributes = oAuth2Attributes;
//    }
//
//    public static UserDetailsImpl of(Long id, String username, String password,String nickname,String email, UserRole role,
//                                     LocalDateTime createdAt, LocalDateTime modifiedAt, LocalDateTime deletedAt,
//                                     Map<String,Object> oAuth2Attributes){
//
//        return new UserDetailsImpl(id, username, password, nickname, email, role,
//                createdAt, modifiedAt, deletedAt,
//                oAuth2Attributes
//        );
//    }
//
//    public static UserDetailsImpl fromEntity(User user) {
//        return new UserDetailsImpl(
//                user.getId(),
//                user.getUsername(),
//                user.getPassword(),
//                user.getNickname(),
//                user.getEmail(),
//                user.getRole(),
//                user.getCreatedAt(),
//                user.getModifiedAt(),
//                user.getDeletedAt(),
//                Map.of()
//        );
//    }
//
//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return List.of(new SimpleGrantedAuthority(this.getRole().toString()));
//    }
//
//    //todo 하드 딜리트를 하게되면 true로 바꿔줘야함
//    @Override
//    public boolean isAccountNonExpired() {
//        return this.deletedAt == null;
////        return true;
//    }
//
//    @Override
//    public boolean isAccountNonLocked() {
//        return this.deletedAt == null;
////        return true;
//    }
//
//    @Override
//    public boolean isCredentialsNonExpired() {
//        return this.deletedAt == null;
////        return true;
//    }
//
//    @Override
//    public boolean isEnabled() {
//        return this.deletedAt == null;
////        return true;
//    }
//
//    //Oauth
//    @Override
//    public Map<String, Object> getAttributes() {
//        return oAuth2Attributes;
//    }
//
//    @Override
//    public String getName() {
//        return this.username;
//    }
//
//    public static UserDetailsImpl fromDto(UserDto userDto) {
//        return UserDetailsImpl.of(
//                userDto.getId(),
//                userDto.getUsername(),
//                userDto.getPassword(),
//                userDto.getNickname(),
//                userDto.getEmail(),
//                userDto.getRole(),
//                userDto.getCreatedAt(),
//                userDto.getModifiedAt(),
//                userDto.getDeletedAt(),
//                Map.of()
//        );
//    }
//}
