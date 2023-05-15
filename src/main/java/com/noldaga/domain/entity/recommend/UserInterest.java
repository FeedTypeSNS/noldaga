package com.noldaga.domain.entity.recommend;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;

@Table
@Getter
@NoArgsConstructor
@Entity
public class UserInterest {
    @Id
    @Column(name="user_id")
    private Long userId; //회원 아이디

    @Column(name = "interest_hashtag_id")
    private String hastagId; //관심 해시태그

    @Column
    private String grade;

    @Column
    private LocalDateTime setInterestAt;

    private UserInterest(Long userId, String hastagId, String grade, LocalDateTime setInterestAt){
        this.userId = userId;
        this.hastagId = hastagId;
        this.grade = grade;
        this.setInterestAt = setInterestAt;
    }

    public static UserInterest of(Long userId, String hastagId, String grade, LocalDateTime setInterestAt){
        return new UserInterest(userId, hastagId, grade, setInterestAt);
    }
}
