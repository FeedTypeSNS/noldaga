package com.noldaga.domain.entity;


import com.noldaga.domain.userdto.Gender;
import com.noldaga.domain.userdto.UserRole;
import com.noldaga.util.ConstUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@EntityListeners(AuditingEntityListener.class)
@Getter //Dto 만들때 쓰임
@ToString
@Table(name="users") //db 테이블 만들때 예약어는 피해야하는것을 염두해야함
@Entity
@SQLDelete(sql = "update users set deleted_at = now() where user_id=? ") //소프트 delete를 위함...
@Where(clause = "deleted_at is NULL") // 조회시 이 조건이 자동 추가
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name="user_id", length = 50)
    private Long id;

    @Column(nullable=false ,updatable = false,unique = true)
    private String username;

    @Column(nullable = false,length = 100)
    private String nickname;
    @Setter @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @DateTimeFormat(iso=DateTimeFormat.ISO.DATE)
    private LocalDate birthday;


    private String profileImageUrl;
    private String profileMessage;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<UserTag> userTags;

    @Column(length = 100,nullable = false)
    private String email;
    @Setter
    private Long totalFollower=Long.valueOf(0);
    @Setter
    private Long totalFollowing=Long.valueOf(0);

    @Setter
    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private UserRole role = UserRole.USER;

    @DateTimeFormat(iso=DateTimeFormat.ISO.DATE_TIME)
    @CreatedDate
    @Column(nullable = false , updatable = false)
    private LocalDateTime createdAt;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime modifiedAt;

    @DateTimeFormat(iso=DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime deletedAt;

    protected User(){
    }


    private User(String username, String password, String nickname, String email) {
        this.profileImageUrl = ConstUtil.USER_DEFAULT_IMG_URL;
        this.username = username;
        this.nickname = nickname;
        this.password = password;
        this.email = email;
    }

    public static User of(String username, String password) {
        return new User(username, password,null,null);
    }

    public static User of(String username, String password, String nickname, String email) {
        return new User(username, password, nickname, email);
    }

    public void changePassword(String newPassword){
        this.password = newPassword;
    }

    public void modifyProfile(String nickname,String message, String imageUrl){
        this.nickname = nickname;
        this.profileMessage = message;
        this.profileImageUrl = imageUrl;
    }

    public void modifyInfo(String nickname, Gender gender, LocalDate birthday) {
        this.nickname = nickname;
        this.gender = gender;
        this.birthday = birthday;
    }

    public void modifyEmail(String email){
        this.email = email;
    }

    public void modifyPassword(String newPassword) {
        this.password = newPassword;
    }
}
