package com.noldaga.domain.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;

@Table(name="storeFeed")
@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor
@Entity
public class StoreFeed {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="store_feed_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name="feed_id")
    private Feed feed;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    @DateTimeFormat(iso=DateTimeFormat.ISO.DATE_TIME)
    @CreatedDate
    @Column(nullable = false , updatable = false, name="reg_date")
    private LocalDateTime regDate;

    private StoreFeed(Feed feed, User user) {
        this.feed = feed;
        this.user = user;
    }

    public static StoreFeed of(Feed feed, User user) {
        return new StoreFeed(feed, user);
    }
}
