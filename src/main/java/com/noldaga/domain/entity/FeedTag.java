package com.noldaga.domain.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Table(name="feedTag")
@Getter
@NoArgsConstructor
@Entity
public class FeedTag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="feed_tag_id")
    private Long id;

//    @ManyToOne
//    @JoinColumn(name="feed_id")
    //feed정보를 저장할 때 다시 가져오는게 효율적이지 못한 것 같아서 일단 id만 저장..변경가능
    @Column(name="feed_id")
    private Long feedId;

    @ManyToOne
    @JoinColumn(name="hashtag_id")
    private HashTag hashTag;

    private FeedTag(Long feedId, HashTag hashTag) {
        this.feedId = feedId;
        this.hashTag = hashTag;
    }

    public static FeedTag of(Long feedId, HashTag hashTag) {
        return new FeedTag(feedId, hashTag);
    }
}
