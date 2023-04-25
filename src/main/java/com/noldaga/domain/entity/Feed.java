package com.noldaga.domain.entity;


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
import java.time.LocalDateTime;

@EntityListeners(AuditingEntityListener.class)
@Getter //Dto 만들때 쓰임
@ToString
@Table(name="feed") //db 테이블 만들때 예약어는 피해야하는것을 염두해야함
@Entity
@SQLDelete(sql = "update feed set deleted_at = now() where feed_id=? ") //소프트 delete를 위함...
@Where(clause = "deleted_at is NULL") // 조회시 이 조건이 자동 추가
public class Feed {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="feed_id")
    private Long id;

    @Setter
    @Column(nullable=false )
    private String title;

    @Setter
//    @Column(nullable = false,length = 1000)
    @Column(nullable=false, columnDefinition = "TEXT")//TEXT 타입으로 컬럼이 생성됨(그냥 String 디폴트보다 더 길게 저장가능)
    private String content;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

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

    protected Feed(){
    }

    public Feed(Long feedId,String title, String content,User user) {
        this.id = feedId;
        this.title = title;
        this.content = content;
        this.user = user;
    }

    public static Feed of(String title, String content,User user) {
        return new Feed(null,title, content,user);
    }
}
