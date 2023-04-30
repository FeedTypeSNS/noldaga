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

    @ManyToOne
    @JoinColumn(name="feed_id")
    private Feed feed;

    @ManyToOne
    @JoinColumn(name="hashtag_id")
    private HashTag hashTag;

    private FeedTag(Feed feed, HashTag hashTag) {
        this.feed = feed;
        this.hashTag = hashTag;
    }

    public static FeedTag of(Feed feed, HashTag hashTag) {
        return new FeedTag(feed, hashTag);
    }
}
