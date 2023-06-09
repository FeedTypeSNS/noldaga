package com.noldaga.domain.entity;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@EntityListeners(value={AuditingEntityListener.class})
@NoArgsConstructor
@Getter
@Table(name="comment")
@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "feed_id")
    private Feed feed;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String content;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @Column(name = "reg_date")
    @CreatedDate
    private LocalDateTime regDate;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @LastModifiedDate
    @Column(name = "mod_date")
    private LocalDateTime modDate;

    //실제로 사용할 일은 없고 댓글 좋아요 생명주기 관리를 위해서 넣어줌
    @OneToMany(mappedBy = "comment", cascade = CascadeType.REMOVE)
    private List<CommentLike> commentLike;

    @Column(name="total_like")
    private long totalLike; //default로 0들어가게 long설정

    private Comment(String content, Feed feed, User user) {
        this.content = content;
        this.feed = feed;
        this.user = user;
    }

    public static Comment of(String content, Feed feed, User user) {
        return new Comment(content, feed, user);
    }

    public void change(String newContent) {
        this.content = newContent;
    }

    public void plusLikeCount(){
        this.totalLike+=1;
    }

    public void minusLikeCount(){
        this.totalLike-=1;
    }

    public String getFeedTitle(){
        return feed.getTitle();
    }

    public Long getFeedId(){
        return feed.getId();
    }
}

