package com.noldaga.domain.entity;

import lombok.Getter;

import javax.persistence.*;

@Table(name="hashTag")
@Getter
@Entity
public class HashTag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="hashtag_id")
    private Long id;

    @Column(nullable = false, length = 20, name="tag_name")
    private String tagName;
}
