package com.noldaga.controller;

import com.noldaga.domain.GroupDto;
import com.noldaga.service.GroupService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Log4j2
@RequiredArgsConstructor
@Controller
public class GroupController {

    private final GroupService groupService;

    @GetMapping("/groups")
    public String chatMain(){
        return "groups";
    }

    @GetMapping("/group")
    public String groupView(Long id, Model model) {
        GroupDto groupDto = groupService.getGroup(id);

        log.info(groupDto);

        model.addAttribute("group", groupDto);

        return "group-details";
    }
}
