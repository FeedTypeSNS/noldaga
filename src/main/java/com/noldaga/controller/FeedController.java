package com.noldaga.controller;


import com.noldaga.controller.request.FeedCreateRequest;
import com.noldaga.controller.request.FeedModifyRequest;
import com.noldaga.controller.response.FeedResponse;
import com.noldaga.controller.response.Response;
import com.noldaga.domain.FeedDto;
import com.noldaga.domain.UploadDto;
import com.noldaga.domain.UserSimpleDto;
import com.noldaga.domain.userdto.UserDto;
import com.noldaga.exception.ErrorCode;
import com.noldaga.exception.SnsApplicationException;
import com.noldaga.service.FeedService;
import com.noldaga.service.UserService;
import com.noldaga.util.ClassUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.coobird.thumbnailator.Thumbnailator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Log4j2
@RestController
@RequiredArgsConstructor
public class FeedController {

    @Value("${com.noldaga.upload.path}")
    private String path;

    private final FeedService feedService;
    private final UserService userService;

    //피드 조회시 로그인한 사용자 정보 가져옴
    @GetMapping("/api/feed/getuser")
    public UserSimpleDto getUserAtFeed(Authentication authentication) {
//        UserDto userDto = ClassUtils.getSafeCastInstance(authentication.getPrincipal(),UserDto.class).orElseThrow(()->
//                new SnsApplicationException(ErrorCode.INTERNAL_SERVER_ERROR,"Casting to UserDto class failed"));
        return UserSimpleDto.fromUserDto((UserDto) authentication.getPrincipal());
    }

    //피드 작성
    @PostMapping("/api/feed")
    public Response<FeedResponse> create(@RequestBody FeedCreateRequest request, Authentication authentication) {
        FeedDto feedDto = feedService.create(request, authentication.getName());
        //authentication.getName()을 까보면 principal.getName() -> AbstractAuthenticationToken.getName() 참고하면 UserDetails 구현해주어야함

        return Response.success(FeedResponse.fromFeedDto(feedDto));
    }

    //상세 페이지 피드 가져오기
    @GetMapping(value="/api/feed")
    public Response<FeedResponse> getFeed(Long id, Authentication authentication){
        FeedDto feedDto = feedService.getDetailFeed(id,authentication.getName());

        return Response.success(FeedResponse.fromFeedDto(feedDto));
    }

    //메인 페이지 피드 가져오기
    @GetMapping(value="/api/feeds/{page}")
    public Response<List<FeedResponse>> getmainFeeds(@PathVariable int page, Authentication authentication){//Authentication authentication
        List<FeedDto> feedDtoList = feedService.getMainFeed(page,authentication.getName());

        List<FeedResponse> feedResponseList = new ArrayList<>();
        feedDtoList.forEach(feedDto->{
            feedResponseList.add(FeedResponse.fromFeedDto(feedDto));
        });

        return Response.success(feedResponseList);
    }

    //그룹 페이지 피드 가져오기
    @GetMapping(value="/api/feeds/group/{group_id}/{page}")
    public Response<List<FeedResponse>> getGroupPageFeeds(@PathVariable Long group_id,@PathVariable int page, Authentication authentication){//Authentication authentication
        List<FeedDto> feedDtoList = feedService.getGroupFeed(page,authentication.getName(),group_id);

        List<FeedResponse> feedResponseList = new ArrayList<>();
        feedDtoList.forEach(feedDto->{
            feedResponseList.add(FeedResponse.fromFeedDto(feedDto));
        });

        return Response.success(feedResponseList);
    }

    //마이 페이지 피드 가져오기
    @GetMapping(value="/api/feeds/mypage/{page}")
    public Response<List<FeedResponse>> getMypageFeeds(Long user_id,@PathVariable int page, Authentication authentication){//Authentication authentication
        List<FeedDto> feedDtoList = feedService.getMyPageFeed(page,user_id,authentication.getName());

        List<FeedResponse> feedResponseList = new ArrayList<>();
        feedDtoList.forEach(feedDto->{
            feedResponseList.add(FeedResponse.fromFeedDto(feedDto));
        });

        return Response.success(feedResponseList);
    }

    //탐색 페이지 피드 가져오기
    @GetMapping(value="/api/feeds/explore/{page}")
    public Response<List<FeedResponse>> getExplorePageFeeds(@PathVariable int page, Authentication authentication){//Authentication authentication
        List<FeedDto> feedDtoList = feedService.getExploreFeed(page,authentication.getName());

        List<FeedResponse> feedResponseList = new ArrayList<>();
        feedDtoList.forEach(feedDto->{
            feedResponseList.add(FeedResponse.fromFeedDto(feedDto));
        });

        return Response.success(feedResponseList);
    }

    //저장한 피드 가져오기 - 본인만 볼 수 있음
    @GetMapping("/api/feeds/save/{page}")
    public Response<List<FeedResponse>> getMySavedFeeds(@PathVariable int page, Authentication authentication){//Authentication authentication
        List<FeedDto> feedDtoList = feedService.getMySavedFeed(page,authentication.getName());

        List<FeedResponse> feedResponseList = new ArrayList<>();
        feedDtoList.forEach(feedDto->{
            feedResponseList.add(FeedResponse.fromFeedDto(feedDto));
        });

        return Response.success(feedResponseList);
    }

    @PutMapping("/api/feed")
    public Response<FeedResponse> modifyFeed(@RequestBody FeedModifyRequest request, Long id, Authentication authentication){
        FeedDto feedDto = feedService.modify(request, id, authentication.getName());

        return Response.success(FeedResponse.fromFeedDto(feedDto));
    }

    @DeleteMapping("/api/feed")
    public Response<Void> deleteFeed(Long id, Authentication authentication){
        feedService.delete(id, authentication.getName());
        return Response.success();
    }

    @PostMapping("/api/upload")
    public Response<List<UploadDto>> imageUpload(@RequestParam("images") List<MultipartFile> files) throws IOException {

        log.info("업로드 시작");
        List<UploadDto> uploadDtoList = new ArrayList<>();

        files.forEach(file -> {
            String originalName = file.getOriginalFilename();
            String uuid = UUID.randomUUID().toString();
            Path savePath = Paths.get(path,uuid+"_"+originalName);
            boolean img = false;

            try {
                file.transferTo(savePath);

                if(Files.probeContentType(savePath).startsWith("image")){
                    img = true;
                    File thumbFile = new File(path,"s_"+uuid+"_"+originalName);
                    Thumbnailator.createThumbnail(savePath.toFile(), thumbFile, 200, 200);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            UploadDto uploadDto = UploadDto.builder()
                    .uuid(uuid)
                    .filename(originalName)
                    .img(img)
                    .build();

            log.info(uploadDto);
            log.info(uploadDto.getLink());
            uploadDtoList.add(uploadDto);
        });
        log.info(uploadDtoList);
        return Response.success(uploadDtoList);
    }

}
