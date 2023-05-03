package com.noldaga.domain.entity;

import lombok.*;

import javax.persistence.*;

@Entity(name = "group_member")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class GroupMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_member_id")
    private Long id;

    // 그룹과 회원의 관계 정의
    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public GroupMember(Group group, User user) {
        this.group = group;
        this.user = user;
    }

    public static GroupMember of(Group group, User user) {
        return new GroupMember(group, user);
    }
}
