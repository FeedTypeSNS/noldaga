package com.noldaga.domain.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@EntityListeners(AuditingEntityListener.class) //자동으로 시간을 매핑하여 테이블에 넣어주는 jpa auditing 사용
@Getter //dto 생성시 사용
@ToString
@Entity
public class Follow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "follow_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "following_id", nullable = false)
    private User following;    //건 사람

    @ManyToOne
    @JoinColumn(name = "follower_id", nullable = false)
    private User follower;     //당한 사람

    @Setter
    @Column(columnDefinition = "BOOLEAN default false")
    private boolean bothFollow; //맞팔 여부

    protected Follow(){}

    public Follow(Long followId, User followingId, User followerId, Boolean both){
        this.id = followId;
        this.following = followingId;
        this.follower = followerId;
        this.bothFollow = both;
    }

    public static Follow of(User followingId, User followerId, Boolean both) {
        return new Follow(null, followingId, followerId, both);
    }


}
