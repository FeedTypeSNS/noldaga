package com.noldaga.service;

import com.noldaga.domain.GroupDto;
import com.noldaga.domain.GroupMemberDto;
import com.noldaga.domain.entity.Group;
import com.noldaga.domain.entity.GroupMember;
import com.noldaga.domain.entity.User;
import com.noldaga.exception.ErrorCode;
import com.noldaga.exception.SnsApplicationException;
import com.noldaga.repository.GroupMemberRepository;
import com.noldaga.repository.GroupRepository;
import com.noldaga.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupMemberService {

    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final GroupMemberRepository groupMemberRepository;

    @Transactional
    public GroupMemberDto registerGroup(Long groupId, String username) {
        // 유저 정보
        User user = userRepository.findByUsername(username).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", username)));

        // 그룹 정보
        Group group = groupRepository.findById(groupId).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.FEED_NOT_FOUND, String.format("%s not founded", groupId)));

        //그룹 가입
        GroupMember groupMember = groupMemberRepository.save(GroupMember.of(group, user));
        GroupMemberDto groupMemberDto = GroupMemberDto.fromEntity(groupMember);

        return groupMemberDto;

    }

    @Transactional
    public List<Group> getGroupList2(String username) {
        // 유저 정보
        User user = userRepository.findByUsername(username).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", username)));

        return groupMemberRepository.findAllByUser(user);
    }

    @Transactional
    public void unregisterGroup(Long id, String username) {
        //유저확인
        User user = userRepository.findByUsername(username).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", username)));

        //그룹확인
        Group group = groupRepository.findById(id).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.FEED_NOT_FOUND, String.format("%s not founded", id)));

        //권한있나확인
        GroupMember groupMember = groupMemberRepository.findByGroupAndUser(group, user).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.FEED_NOT_FOUND, String.format("%s not founded", id)));

        groupMemberRepository.deleteById(groupMember.getId());
    }

}
