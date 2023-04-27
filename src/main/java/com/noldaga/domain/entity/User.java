package com.noldaga.domain.entity;


import com.noldaga.domain.userdto.Gender;
import com.noldaga.domain.userdto.UserRole;
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

@EntityListeners(AuditingEntityListener.class)
@Getter //Dto 만들때 쓰임
@ToString
@Table(name="users") //db 테이블 만들때 예약어는 피해야하는것을 염두해야함
@Entity
@SQLDelete(sql = "update users set deleted_at = now() where user_id=? ") //소프트 delete를 위함...
@Where(clause = "deleted_at is NULL") // 조회시 이 조건이 자동 추가
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name="user_id")
    private Long id;

    @Column(nullable=false ,updatable = false,unique = true)
    private String username;

    private String nickname;
    @Setter @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @DateTimeFormat(iso=DateTimeFormat.ISO.DATE)
    private LocalDate birthday;


    private String profileImageUrl;
    private String profileMessage;
    private String email;
    private Long totalFollower;
    private Long totalFollowing;

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

    public User(Long id,String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    public static User of(String username, String password) {
        return new User(null,username, password);
    }

}
