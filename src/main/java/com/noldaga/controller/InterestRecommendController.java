package com.noldaga.controller;

import com.noldaga.controller.response.FeedResponse;
import com.noldaga.controller.response.InterestFeedResponse;
import com.noldaga.controller.response.Response;
import com.noldaga.domain.UserSimpleDto;
import com.noldaga.service.InterestRecommendService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Log4j2
@RestController
@RequiredArgsConstructor
public class InterestRecommendController {
    private final InterestRecommendService interestRecommendService;

    @GetMapping("/api/rec/{page}")
    public Response<List<InterestFeedResponse>> getUserAtFeed(@PathVariable int page, Authentication authentication) {
        List<InterestFeedResponse> responses = interestRecommendService.getNonInterst(page, authentication.getName());
        return Response.success(responses);
    }
    @GetMapping("/api/rec/num")
    public Response<Integer> getUserAtFeed(Authentication authentication) {
        int num = 0;
        return Response.success(num);
    }
}
