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

    @Column(name = "group_member_favor", nullable = false, columnDefinition = "int default 0")
    private int favor; // 즐겨찾기 : 1

    public GroupMember(Group group, User user, int favor) {
        this.group = group;
        this.user = user;
        this.favor = favor;
    }

    public static GroupMember of(Group group, User user, int favor) {
        return new GroupMember(group, user, favor);
    }

    public void favorChange(int favor){
        this.favor = favor;
    }
}
