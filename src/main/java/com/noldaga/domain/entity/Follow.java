package com.noldaga.domain.entity;


import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Follow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "follow_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "following_id")
    private User following;    //건 사람

    @ManyToOne
    @JoinColumn(name = "follower_id")
    private User follower;     //당한 사람


    //private List<Users> followingList = new ArrayList<>(); //내가 팔로우 하는 사람 리스트
    //private List<Users> followerList = new ArrayList<>(); //나를 팔로우 하는 사람 리스트

}
