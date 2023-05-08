package com.noldaga.domain.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

//@Builder
//@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor
@Where(clause = "del_date is null")
@SQLDelete(sql = "UPDATE feed SET del_date = current_timestamp WHERE feed_id = ?")
@Table(name="feed")
@Entity
public class Feed {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="feed_id")
    private Long id;

    @NotEmpty
    @Column(nullable = false, length = 100, name="title")
    private String title;

    @NotEmpty
    @Column(nullable=false, columnDefinition = "TEXT")//TEXT 타입으로 컬럼이 생성됨(그냥 String 디폴트보다 더 길게 저장가능)
    private String content;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

//    Group_id는 selectbox에서 선택하는 값으로 group을 저장하려면 다시 조회해서 들고와야해서 일단 id만 저장
//    @OneToOne
//    @JoinColumn(name="group_id")
//    private Group group;

    @Column(nullable = false, name="group_id")
    private Long groupId;

    @Column(nullable = false, name="open_range") //전체 0 부분공개 1
    private int range;

    @DateTimeFormat(iso=DateTimeFormat.ISO.DATE_TIME)
    @CreatedDate
    @Column(nullable = false , updatable = false, name="reg_date")
    private LocalDateTime regDate;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @LastModifiedDate
    @Column(nullable = false, name="mod_date")
    private LocalDateTime modDate;

    @Column(name = "del_date")
    private LocalDateTime delDate;

    @Column(name="total_view")
    private long totalView; //default로 0들어가게 long설정

    @Column(name="total_comment")
    private long commentCount; //default로 0들어가게 long설정

    @Column(name="total_like")
    private long likeCount; //default로 0들어가게 long설정

 //   @JsonIgnoreProperties({"feed,user"})
    @OneToMany(mappedBy = "feed", fetch = FetchType.LAZY)
    private List<Comment> comment;

    @OneToMany(mappedBy = "feed", fetch = FetchType.EAGER)
    private List<FeedTag> feedTags;

//    @OneToMany(mappedBy = "feed")
//    private List<FeedLike> feedLikes;

    private Feed(String title, String content, long groupId, int range, User user) {
        this.title = title;
        this.content = content;
        this.groupId = groupId;
        this.range = range;
        this.user = user;
    }

    public static Feed of(String title, String content, long groupId, int range, User user) {
        return new Feed(title, content, groupId, range, user);
    }

    public void change(String title, String content, long groupId, int range){
        this.title = title;
        this.content = content;
        this.groupId = groupId;
        this.range = range;
    }

    public void plusViewCount(){
        this.totalView+=1;
    }

    public void plusCommentCount(){
        this.commentCount+=1;
    }

    public void minusCommentCount(){
        this.commentCount-=1;
    }

    public void plusLikeCount(){
        this.likeCount+=1;
    }

    public void minusLikeCount(){
        this.likeCount-=1;
    }
}
