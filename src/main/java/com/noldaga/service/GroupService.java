package com.noldaga.service;


import com.noldaga.domain.FeedDto;
import com.noldaga.domain.GroupDto;
import com.noldaga.domain.entity.Feed;
import com.noldaga.domain.entity.Group;
import com.noldaga.domain.entity.GroupMember;
import com.noldaga.domain.entity.User;
import com.noldaga.exception.ErrorCode;
import com.noldaga.exception.SnsApplicationException;
import com.noldaga.repository.GroupMemberRepository;
import com.noldaga.repository.GroupRepository;
import com.noldaga.repository.UserRepository;
import com.noldaga.util.ConstUtil;
import com.noldaga.util.S3Uploader;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final GroupMemberRepository groupMemberRepository;
    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final S3Uploader s3Uploader;

    @Transactional
    public GroupDto createGroup(GroupDto groupDto, MultipartFile img, String username) throws IOException {
        String url = "";
        if(img == null) {
            url = ConstUtil.GROUP_DEFAULT_IMG_URL;
        } else {
            url = s3Uploader.upload(img, "/group/img");
        }

        //저장정보
        String name = groupDto.getName();
        String intro = groupDto.getIntro();
        int open = groupDto.getOpen();
        String profile_url = url;
        String pw = groupDto.getPw();

        //회원가입된 user인지 확인
        User user = userRepository.findByUsername(username).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", username)));

        //그룹 생성
        Group group = groupRepository.save(Group.of(name, profile_url, pw, intro, open, user));
        GroupDto groupDto2 = GroupDto.fromEntity(group);
        return groupDto2;
    }

    @Transactional
    public List<Group> getGroupList(String username) {

        //회원가입된 user인지 확인
        User user = userRepository.findByUsername(username).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", username)));

        return groupRepository.findAllByUser(user, Sort.by(Sort.Direction.DESC, "id"));
    }

    @Transactional
    public GroupDto getGroup(Long id, String username) {
        //유저확인
        User user = userRepository.findByUsername(username).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", username)));

        Group group = groupRepository.findById(id).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.GROUP_NOT_FOUND, String.format("%s not founded", id)));


        GroupDto groupDto = GroupDto.fromEntity(group);

        return groupDto;
    }

    @Transactional
    public GroupDto updateGroup(Long id, GroupDto groupDto, MultipartFile img, String username) throws IOException {

        //유저확인
        User user = userRepository.findByUsername(username).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", username)));

        //그룹확인
        Group group = groupRepository.findById(id).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.GROUP_NOT_FOUND, String.format("%s not founded", id)));

        //권한있나확인
        if (group.getUser() != user) {
            throw new SnsApplicationException(ErrorCode.INVALID_PERMISSION, String.format("%s has no permission with %s", username, id));
        }



        String url = "";
        if(img == null) {
            url = ConstUtil.GROUP_DEFAULT_IMG_URL;
        } else {
            url = s3Uploader.upload(img, "/group/img");
        }

        group.change(groupDto.getName(), url, groupDto.getPw(), groupDto.getIntro(), groupDto.getOpen());
        groupRepository.save(group);

        GroupDto updatedGroup = GroupDto.fromEntity(group);

        return updatedGroup;
    }

    @Transactional
    public void deleteGroup(Long id, String username) throws UnsupportedEncodingException {
        //유저확인
        User user = userRepository.findByUsername(username).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", username)));

        //그룹확인
        Group group = groupRepository.findById(id).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.FEED_NOT_FOUND, String.format("%s not founded", id)));

        //그룹장 여부 확인
        if (group.getUser() != user) {
            throw new SnsApplicationException(ErrorCode.INVALID_PERMISSION, String.format("%s has no permission with %s", username, id));
        }



        //그룹에 속한 멤버들 가져오기
        List<User> members = groupMemberRepository.findAllByGroup(group);

        //그룹 멤버들 삭제
        for(User member : members) {
            groupMemberRepository.deleteByUser(member);
        }

        //그룹삭제
        groupRepository.deleteById(id);
    }


}
