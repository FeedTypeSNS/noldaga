package com.noldaga.domain.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Getter
@NoArgsConstructor
@Entity
public class UserTag {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_tag_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name="hashtag_id")
    private HashTag hashTag;

    private UserTag(User user, HashTag hashTag) {
        this.user = user;
        this.hashTag = hashTag;
    }

    public static UserTag of(User user, HashTag hashTag) {
        return new UserTag(user, hashTag);
    }
}
