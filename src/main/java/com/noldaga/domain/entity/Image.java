package com.noldaga.domain.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor
@Table(name="image")
@Entity
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="image_id")
    private Long id;

    @NotEmpty
    @Column(nullable = false, length = 100, name="file_url")
    private String url;

    @ManyToOne
    @JoinColumn(name="feed_id")
    private Feed feed;

    @Column(nullable = false, name="feed_seq")
    private int seq;

    private Image(String url, Feed feed, int seq) {
        this.url = url;
        this.feed = feed;
        this.seq = seq;
    }

    public static Image of(String url, Feed feed, int seq) {
        return new Image(url, feed, seq);
    }
}
