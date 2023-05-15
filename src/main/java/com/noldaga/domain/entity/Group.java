package com.noldaga.domain.entity;

import lombok.*;

import javax.persistence.*;

@Entity(name = "group_table")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_id")
    private Long id;

    @Column(name = "group_name", length = 20, nullable = false)
    private String name;

    @Column(name = "group_open", nullable = false)
    private int open; //그룹 : 공개 1, 비공개 0

    @Column(name = "group_pw", length = 20)
    private String pw;

    @Column(name = "group_profile_url", length = 300)
    private String profile_url;

    @Column(name = "group_intro")
    private String intro;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user; // 관리자(그룹을 생성한 사람)

    public Group(String name, String profile_url, String pw, String intro, int open, User user) {
        this.name = name;
        this.profile_url = profile_url;
        this.pw = pw;
        this.intro = intro;
        this.open = open;
        this.user = user;
    }

    public static Group of(String name, String profile_url, String pw, String intro, int open, User user) {
        return new Group(name, profile_url, pw, intro, open, user);
    }

    public void change(String name, String profile_url, String pw, String intro, int open){
        this.name = name;
        this.profile_url = profile_url;
        this.pw = pw;
        this.intro = intro;
        this.open = open;
    }
}

