package com.noldaga.domain.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Table(name="feedLike")
@Getter
@NoArgsConstructor
@Entity
public class FeedLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="feed_like_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name="feed_id")
    private Feed feed;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    private FeedLike(Feed feed, User user) {
        this.feed = feed;
        this.user = user;
    }

    public static FeedLike of(Feed feed, User user) {
        return new FeedLike(feed, user);
    }
}
