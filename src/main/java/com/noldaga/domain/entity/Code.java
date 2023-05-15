package com.noldaga.domain.entity;


import com.noldaga.domain.alarm.AlarmArgs;
import com.noldaga.domain.alarm.AlarmType;
import com.vladmihalcea.hibernate.type.json.JsonType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
@Getter
@EntityListeners(AuditingEntityListener.class)
@Entity
@Table(name="code")
public class Code {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="code_id")
    private Integer id;

    private String randomCode;

    private String email;

    private String username;

    private Long groupId;


    @DateTimeFormat(iso=DateTimeFormat.ISO.DATE_TIME)
    @CreatedDate
    @Column(nullable = false , updatable = false)
    private LocalDateTime createdAt;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime modifiedAt;


    protected Code(){}

    private Code(String randomCode, String email, String username) {
        this.randomCode = randomCode;
        this.email = email;
        this.username = username;
    }

    private Code(String randomCode,Long groupId){
        this.randomCode = randomCode;
        this.groupId = groupId;
    }

    public static Code of(String randomString, String email, String username){
        return new Code(randomString,email,username);
    }

    public static Code of(String randomString,String email){
        return new Code(randomString,email,null);
    }

    public static Code of(String randomString){
        return new Code(randomString, null, null);
    }

    public static Code of(String randomString,Long groupId){
        return new Code(randomString, groupId);
    }
}
