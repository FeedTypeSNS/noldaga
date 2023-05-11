package com.noldaga.controller;

import com.noldaga.controller.response.Response;
import com.noldaga.domain.GroupDto;
import com.noldaga.domain.GroupMemberDto;
import com.noldaga.domain.entity.Group;
import com.noldaga.domain.entity.GroupMember;
import com.noldaga.domain.entity.User;
import com.noldaga.service.GroupMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class GroupMemberController {
    private final GroupMemberService groupMemberService;

    @PostMapping("/group/member/{group_id}")//그룹가입
    public Response<GroupMemberDto> registerGroup(@PathVariable Long group_id, Authentication authentication) {
        GroupMemberDto registerGroupMember = groupMemberService.registerGroup(group_id, authentication.getName());

        return Response.success(registerGroupMember);
    }

    @GetMapping("/groups/member") // 자신이 가입한 그룹 리스트(로그인한 유저가 가입한 그룹 리스트 보기)
    public Response<List<Group>> getGroupList2(Authentication authentication) {
        List<Group> groupList2 = groupMemberService.getGroupList2(authentication.getName());
        return Response.success(groupList2);
    }

    @GetMapping("/members/group/{group_id}") // 그룹에 가입된 멤버리스트
    public Response<List<User>> getGroupMemberList(@PathVariable Long group_id) {
        List<User> groupMemberList = groupMemberService.getGroupMemberList(group_id);
        return Response.success(groupMemberList);
    }

    @DeleteMapping("/group/member/{group_id}")
    public Response<Void> unregisterGroup(@PathVariable Long group_id,  Authentication authentication) {
        groupMemberService.unregisterGroup(group_id, authentication.getName());

        return Response.success();
    }

    @GetMapping("/group/member/{group_id}") // 그룹 join여부
    public Response<GroupMemberDto> getGroupMember(@PathVariable Long group_id, Authentication authentication) {
        GroupMemberDto groupMemberDto = groupMemberService.getGroupMember(group_id, authentication.getName());

        return Response.success(groupMemberDto);
    }

    @DeleteMapping("/group/member/{group_id}/{user_id}")//그룹 강퇴
    public Response<Void> expelGroup(@PathVariable Long group_id, @PathVariable Long user_id,  Authentication authentication) {
        groupMemberService.expelGroup(group_id, user_id, authentication.getName());

        return Response.success();
    }

    @GetMapping("/group/member/favor/{group_id}")
    public Response<GroupMemberDto> favorGroup(@PathVariable Long group_id, Authentication authentication) {
        GroupMemberDto favorGroupMember = groupMemberService.favorGroup(group_id, authentication.getName());

        return Response.success(favorGroupMember);
    }

    @GetMapping("/group/member/unfavor/{group_id}")
    public Response<GroupMemberDto> unfavorGroup(@PathVariable Long group_id, Authentication authentication) {
        GroupMemberDto unfavorGroupMember = groupMemberService.unfavorGroup(group_id, authentication.getName());

        return Response.success(unfavorGroupMember);
    }

    @GetMapping("/groups/member/favor") // 자신이 가입한 그룹 중 즐겨찾는 그룹 리스트
    public Response<List<Group>> getFavorGroupList(Authentication authentication) {
        List<Group> FavorGroupList = groupMemberService.getFavorGroupList(authentication.getName());
        return Response.success(FavorGroupList);
    }
}
