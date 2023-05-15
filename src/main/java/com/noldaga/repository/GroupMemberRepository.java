package com.noldaga.repository;

import com.noldaga.domain.entity.Follow;
import com.noldaga.domain.entity.Group;
import com.noldaga.domain.entity.GroupMember;
import com.noldaga.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface GroupMemberRepository extends JpaRepository<GroupMember, Long> {
    @Query("SELECT gm.group FROM group_member gm JOIN gm.group g WHERE gm.user = :user")
    List<Group> findAllByUser(User user);

    @Query("SELECT gm.group FROM group_member gm JOIN gm.group g WHERE gm.user = :user and gm.favor = 1")
    List<Group> findAllByUserAndFavor(User user);

    @Query("SELECT gm.user FROM group_member gm JOIN gm.user u WHERE gm.group = :group")
    List<User> findAllByGroup(Group group);

    void deleteByUser(User user);

    Optional<GroupMember> findByGroupAndUser(Group group, User user);

    boolean existsAllByGroupAndUser(Group group, User user);
}
