package com.noldaga.domain.entity;

import lombok.Getter;

import javax.persistence.*;

@Table(name="feedTag")
@Getter
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

}
