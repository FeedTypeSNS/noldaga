package com.noldaga.controller;

import com.noldaga.controller.response.FeedLikeResponse;
import com.noldaga.controller.response.Response;
import com.noldaga.controller.response.StoreFeedResponse;
import com.noldaga.domain.StoreFeedDto;
import com.noldaga.service.StoreFeedService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Log4j2
@RestController
@RequiredArgsConstructor
public class StoreFeedController {

    private final StoreFeedService storeFeedService;

    @GetMapping("/api/feed/store/{feedId}")
    public int feedLikeCheck(@PathVariable Long feedId, Authentication authentication) {
        return storeFeedService.feedSaveChech(feedId, authentication.getName());
    }

    @PostMapping("/api/feed/store/{feedId}")
    public Response<StoreFeedResponse> store(@PathVariable Long feedId, Authentication authentication){
        StoreFeedDto storeFeedDto = storeFeedService.feedSave(feedId, authentication.getName());

        return Response.success(StoreFeedResponse.fromFeedLikeDto(storeFeedDto));
    }

    @DeleteMapping("/api/feed/store/{feedId}")
    public Response<Void> feedLikeDelete(@PathVariable Long feedId, Authentication authentication) {
        storeFeedService.feedSaveDelete(feedId, authentication.getName());
        //authentication.getName()을 까보면 principal.getName() -> AbstractAuthenticationToken.getName() 참고하면 UserDetails 구현해주어야함
        return Response.success();
    }
}
