package com.noldaga.domain.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Table(name="hashTag")
@Getter
@NoArgsConstructor
@Entity
public class HashTag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="hashtag_id")
    private Long id;

    @Column(nullable = false, length = 20, name="tag_name")
    private String tagName;

    @Column(name="total_use")
    private long totalUse; //default로 0들어가게 long설정

    public HashTag(String tagName){
        this.tagName = tagName;
    }

    public void plusTotalUse(){
        this.totalUse+=1;
    }
    public void minusTotalUse(){
        this.totalUse-=1;
    }
}
