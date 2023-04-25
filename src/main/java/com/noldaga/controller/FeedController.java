package com.noldaga.controller;


import com.noldaga.controller.request.FeedCreateRequest;
import com.noldaga.controller.request.FeedModifyRequest;
import com.noldaga.controller.response.FeedResponse;
import com.noldaga.controller.response.Response;
import com.noldaga.domain.FeedDto;
import com.noldaga.service.FeedService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/feeds")
@RequiredArgsConstructor
public class FeedController {

    private final FeedService feedService;

    @PostMapping
    public Response<Void> create(@RequestBody FeedCreateRequest request, Authentication authentication) {
        feedService.create(request.getTitle(), request.getContent(), authentication.getName());
        //authentication.getName()을 까보면 principal.getName() -> AbstractAuthenticationToken.getName() 참고하면 UserDetails 구현해주어야함

        return Response.success();
    }


    @PutMapping("/{feedId}")
    public Response<FeedResponse> modify(@PathVariable Long feedId, @RequestBody FeedModifyRequest request, Authentication authentication) {
        FeedDto feedDto = feedService.modify(request.getTitle(), request.getContent(), authentication.getName(), feedId);

        return Response.success(FeedResponse.fromFeedDto(feedDto));
    }


    @DeleteMapping("/{feedId}")
    public Response<Void> delete(@PathVariable Long feedId, Authentication authentication) {
        feedService.delete(authentication.getName(), feedId);
        return Response.success();
    }


    @GetMapping
    public Response<Page<FeedResponse>> list(Pageable pageable, Authentication authentication) {

        Page<FeedResponse> feedResponsePage = feedService.list(pageable).map(FeedResponse::fromFeedDto);
        return Response.success(feedResponsePage);

    }

    @GetMapping("/my")
    public Response<Page<FeedResponse>> myList(Pageable pageable, Authentication authentication) {

        Page<FeedResponse> feedResponsePage = feedService.myList(authentication.getName(), pageable).map(FeedResponse::fromFeedDto);
        return Response.success(feedResponsePage);
    }



}
