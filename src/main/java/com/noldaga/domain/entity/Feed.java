package com.noldaga.domain.entity;


import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@EntityListeners(AuditingEntityListener.class)
@Getter //Dto 만들때 쓰임
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name="feed") //db 테이블 만들때 예약어는 피해야하는것을 염두해야함
@Entity
public class Feed {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="feed_id")
    private Long id;

    @Setter
    @NotEmpty
    @Column(nullable = false, length = 100, name="title")
    private String title;

    @NotEmpty
    @Setter
    @Column(nullable=false, columnDefinition = "TEXT")//TEXT 타입으로 컬럼이 생성됨(그냥 String 디폴트보다 더 길게 저장가능)
    private String content;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

//    Group_id는 selectbox에서 선택하는 값으로 group을 저장하려면 다시 조회해서 들고와야해서 일단 id만 저장
//    @OneToOne
//    @JoinColumn(name="group_id")
//    private Group group;
    @Setter
    @Column(nullable = false, name="group_id")
    private long groupId;

    @Setter
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

    @Column(name="total_view")
    private long totalView;

    public void change(String title, String content, long groupId, int range){
        this.setTitle(title);
        this.setContent(content);
        this.setGroupId(groupId);
        this.setRange(range);
    }

}
