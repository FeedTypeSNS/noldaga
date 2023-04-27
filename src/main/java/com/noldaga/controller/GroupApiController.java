package com.noldaga.controller;


import com.noldaga.controller.response.Response;
import com.noldaga.domain.GroupDto;
import com.noldaga.domain.entity.Group;
import com.noldaga.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class GroupApiController {

    private final GroupService groupService;

    @PostMapping("/groups")
    public Response<GroupDto> createGroup(@RequestBody GroupDto groupDto, Authentication authentication) {
        GroupDto registeredGroup = groupService.createGroup(groupDto, authentication.getName());
        return Response.success(registeredGroup);
    }

    @GetMapping("/groups") // 자신이 그룹장인 그룹 리스트(로그인한 유저가 만든 그룹 리스트 보기)
    public Response<List<Group>> getGroupList(Authentication authentication) {
        List<Group> groupList = groupService.getGroupList(authentication.getName());
        return Response.success(groupList);
    }

    @GetMapping("/groups/{group_id}") // 그룹 상세보기, 그룹장이 아니더라도 볼 수 있음
    public Response<GroupDto> getGroup(@PathVariable Long group_id) {
        GroupDto groupDto = groupService.getGroup(group_id);
        return Response.success(groupDto);
    }

    @PutMapping("/groups/{group_id}")
    public Response<GroupDto> updateGroup(@PathVariable Long group_id, @RequestBody GroupDto groupDto, Authentication authentication) {
        GroupDto updatedGroup = groupService.updateGroup(group_id, groupDto, authentication.getName());

        return Response.success(updatedGroup);
    }


    @DeleteMapping("/groups/{group_id}")
    public Response<Void> deleteGroup(@PathVariable Long group_id,  Authentication authentication) {
        groupService.deleteGroup(group_id, authentication.getName());

        return Response.success();
    }

}