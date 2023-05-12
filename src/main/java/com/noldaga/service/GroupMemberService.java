package com.noldaga.service;

import com.noldaga.domain.GroupDto;
import com.noldaga.domain.GroupMemberDto;
import com.noldaga.domain.alarm.AlarmArgs;
import com.noldaga.domain.alarm.AlarmType;
import com.noldaga.domain.alarm.GroupObject;
import com.noldaga.domain.alarm.UserObject;
import com.noldaga.domain.entity.Alarm;
import com.noldaga.domain.entity.Group;
import com.noldaga.domain.entity.GroupMember;
import com.noldaga.domain.entity.User;
import com.noldaga.exception.ErrorCode;
import com.noldaga.exception.SnsApplicationException;
import com.noldaga.repository.AlarmRepository;
import com.noldaga.repository.GroupMemberRepository;
import com.noldaga.repository.GroupRepository;
import com.noldaga.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GroupMemberService {

    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final GroupMemberRepository groupMemberRepository;

    private final AlarmRepository alarmRepository;

    @Transactional
    public GroupMemberDto registerGroup(Long groupId, String username) {
        // 유저 정보
        User user = userRepository.findByUsername(username).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", username)));

        // 그룹 정보
        Group group = groupRepository.findById(groupId).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.GROUP_NOT_FOUND, String.format("%s not founded", groupId)));

        //그룹 가입
        GroupMember groupMember = groupMemberRepository.save(GroupMember.of(group, user, 0));
        GroupMemberDto groupMemberDto = GroupMemberDto.fromEntity(groupMember);


        Long groupMasterId = group.getUser().getId();
        Alarm alarm = Alarm.of(groupMasterId, AlarmType.NEW_MEMBER,
                AlarmArgs.of(UserObject.from(user)),
                user);
        alarmRepository.save(alarm);

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
    public List<User> getGroupMemberList(Long groupId) {
        // 그룹 정보
        Group group = groupRepository.findById(groupId).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.GROUP_NOT_FOUND, String.format("%s not founded", groupId)));

        return groupMemberRepository.findAllByGroup(group);
    }

    @Transactional
    public void unregisterGroup(Long id, String username) {
        //유저확인
        User user = userRepository.findByUsername(username).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", username)));

        //그룹확인
        Group group = groupRepository.findById(id).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.GROUP_NOT_FOUND, String.format("%s not founded", id)));

        //가입여부확인
        GroupMember groupMember = groupMemberRepository.findByGroupAndUser(group, user).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.GROUP_NOT_FOUND, String.format("%s not founded", id)));

        groupMemberRepository.deleteById(groupMember.getId());
    }

    @Transactional
    public GroupMemberDto getGroupMember(Long id, String username) {
        //유저확인
        User user = userRepository.findByUsername(username).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", username)));

        //그룹확인
        Group group = groupRepository.findById(id).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.GROUP_NOT_FOUND, String.format("%s not founded", id)));

        //가입여부확인
        GroupMember groupMember = groupMemberRepository.findByGroupAndUser(group, user).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.GROUP_NOT_FOUND, String.format("%s not founded", id)));

        GroupMemberDto groupMemberDto = GroupMemberDto.fromEntity(groupMember);

        return groupMemberDto;
    }

    @Transactional
    public void expelGroup(Long group_id, Long user_id, String username) {
        //유저확인
        User user = userRepository.findById(user_id).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", user_id)));

        //그룹확인
        Group group = groupRepository.findById(group_id).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.GROUP_NOT_FOUND, String.format("%s not founded", group_id)));

        //가입여부확인
        GroupMember groupMember = groupMemberRepository.findByGroupAndUser(group, user).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.GROUP_NOT_FOUND, String.format("%s not founded", group_id)));

        groupMemberRepository.deleteById(groupMember.getId());



        Long bannedUserId = groupMember.getUser().getId();
        Alarm alarm = Alarm.of(bannedUserId, AlarmType.BANNED,
                AlarmArgs.of(GroupObject.from(group)),
                group);

        alarmRepository.save(alarm);

    }

    @Transactional
    public GroupMemberDto favorGroup(Long id, String username) {

        //유저확인
        User user = userRepository.findByUsername(username).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", username)));

        //그룹확인
        Group group = groupRepository.findById(id).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.GROUP_NOT_FOUND, String.format("%s not founded", id)));

        //가입여부확인
        GroupMember groupMember = groupMemberRepository.findByGroupAndUser(group, user).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.GROUP_NOT_FOUND, String.format("%s not founded", id)));



        groupMember.favorChange(1);
        groupMemberRepository.save(groupMember);

        GroupMemberDto favorGroup = GroupMemberDto.fromEntity(groupMember);

        return favorGroup;
    }

    @Transactional
    public GroupMemberDto unfavorGroup(Long id, String username) {

        //유저확인
        User user = userRepository.findByUsername(username).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", username)));

        //그룹확인
        Group group = groupRepository.findById(id).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.GROUP_NOT_FOUND, String.format("%s not founded", id)));

        //가입여부확인
        GroupMember groupMember = groupMemberRepository.findByGroupAndUser(group, user).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.GROUP_NOT_FOUND, String.format("%s not founded", id)));



        groupMember.favorChange(0);
        groupMemberRepository.save(groupMember);

        GroupMemberDto unfavorGroup = GroupMemberDto.fromEntity(groupMember);

        return unfavorGroup;
    }

    @Transactional
    public List<Group> getFavorGroupList(String username) {
        // 유저 정보
        User user = userRepository.findByUsername(username).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", username)));

        return groupMemberRepository.findAllByUserAndFavor(user);
    }

    @Transactional
    public List<Group> getUserFavorGroupList(Long user_id, String username) {
        // 유저 정보
        User user = userRepository.findById(user_id).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", user_id)));

        return groupMemberRepository.findAllByUserAndFavor(user);
    }
}
