package com.noldaga.service;

import com.noldaga.controller.response.FeedResponse;
import com.noldaga.controller.response.InterestFeedResponse;
import com.noldaga.domain.FeedDto;
import com.noldaga.domain.FeedTagDto;
import com.noldaga.domain.entity.*;
import com.noldaga.domain.entity.recommend.UserInterest;
import com.noldaga.exception.ErrorCode;
import com.noldaga.exception.SnsApplicationException;
import com.noldaga.repository.Feed.FeedRepository;
import com.noldaga.repository.FeedTagRepository;
import com.noldaga.repository.HashTag.HashTagRepository;
import com.noldaga.repository.ImageRepository;
import com.noldaga.repository.UserInterestRepository;
import com.noldaga.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class InterestRecommendService {
    private final UserRepository userRepository;
    private final FeedRepository feedRepository;
    private final UserInterestRepository userInterestRepository;
    private final HashTagRepository hashTagRepository;
    private final FeedTagRepository feedTagRepository;
    private final ImageRepository imageRepository;

    /*public List<FeedDto> getInterestFeed(int page, String username){
        User user = userRepository.findByUsername(username).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", username)));

        Pageable pageable = PageRequest.of(page,9);
        Page<Feed> feedListPagination = feedRepository.MainFeedWithFollow(user.getId(), pageable);

        Optional<UserInterest> interest = userInterestRepository.findByUserId(user.getId());

        if (interest.isEmpty()){

        }else {

        }



        List<FeedDto> feedDtoList = new ArrayList<>();
        return feedDtoList;
    }//흥미 기반 반환*/

    public List<InterestFeedResponse> getNonInterst(int page, String username){
        List<InterestFeedResponse> result = new ArrayList<>();

        Long totalCount = feedRepository.count(); //전체 게시글 개수

        List<int[]> arr = countForNoInterest(totalCount);
        for (int[] a : arr) { log.info(Arrays.toString(a)); }
        log.info("비율: "+Arrays.toString(arr.get(0)));
        //전체 리스트
        List<Feed> ShowLists = new ArrayList<>();
        //비율 나누기
        if (page>arr.size()) {
            result.add(InterestFeedResponse.fromEntity(null, null, null, "마지막 피드 입니다."));
        }else {
            int[] num = arr.get(page);

            Pageable pageable = PageRequest.of(page, 20);
            Page<Feed> feedListPagination = null;


            //좋아요가 높은 순
            if (num[0] != 0) {
                List<Feed> topLikeFeeds = feedRepository.findTopByNOrderByLikeCountDesc(num[0]);
                ShowLists.addAll(topLikeFeeds);
            }

            //를 제외한 많이 본 순
            if (num[1] != 0) {
                List<Feed> topViewedFeeds = feedRepository.findTopNOrderByTotalViewDesc(num[1])
                        .stream()
                        .filter(feed -> !ShowLists.contains(feed)) // 이미 조회한 피드는 제외
                        .collect(Collectors.toList());
                ShowLists.addAll(topViewedFeeds);
            }

            //를 제외한 랜덤 해시태그에서 2개 (해시 태그 없으면 랜덤 2개)
            if (num[2] != 0) {
                Optional<HashTag> randomTag = hashTagRepository.findRandomOne();
                List<Feed> randomHashFeed = new ArrayList<>();
                if (randomTag.isPresent()) {
                    List<FeedTag> hashFeed = feedTagRepository.findAllByHashRand(randomTag.get().getId(), num[2]);
                    if (hashFeed.size() > 0) {
                        for (int i = 0; i < hashFeed.size(); i++) {
                            randomHashFeed.add(hashFeed.get(i).getFeed());
                        }
                    } else {
                        randomHashFeed = getRandomFeed(num[2], ShowLists);
                    }
                } else {
                    randomHashFeed = getRandomFeed(num[2], ShowLists);
                }
                ShowLists.addAll(randomHashFeed);
            }

            //를 제외한 신규 게시글
            if (num[3] != 0) {
                List<Feed> recentFeeds = feedRepository.findFirstByOrderByModDateDesc()
                        .stream()
                        .filter(feed -> !ShowLists.contains(feed)) // 이미 조회한 피드는 제외
                        .collect(Collectors.toList());
                ShowLists.addAll(recentFeeds);
            }

            //를 제외한 팔로잉이 많은 사람 가장 최근 글
            if (num[4] != 0) {
                int n = 0;
                long i = 1;
                List<Feed> recentRegUserFeeds = new ArrayList<>();
                while (n == 0) {
                    Optional<User> user = userRepository.findNthByOrderByTotalFollowingDesc(i++);
                    recentRegUserFeeds = feedRepository.findFirstByUserOrderByModDateDesc(user.get())
                            .stream()
                            .filter(feed -> !ShowLists.contains(feed))
                            .collect(Collectors.toList());
                    if (recentRegUserFeeds.size() != 0) {
                        n++;
                    }
                }
                ShowLists.addAll(recentRegUserFeeds);
            }

            //를 제외한 팔로워가 많은 사람 가장 최근 글
            if (num[5] != 0) {
                List<Feed> firstRegUserFeeds = new ArrayList<>();
                int n = 0;
                long i = 1;
                while (n == 0) {
                    Optional<User> user = userRepository.findNthByOrderByTotalFollowerDesc(i++);
                    firstRegUserFeeds = feedRepository.findFirstByUserOrderByModDateDesc(user.get())
                            .stream()
                            .filter(feed -> !ShowLists.contains(feed))
                            .collect(Collectors.toList());
                    if (firstRegUserFeeds.size() != 0) {
                        n++;
                    }
                }
                ShowLists.addAll(firstRegUserFeeds);
            }

            //를 제외한 태그가 없는 게시글 중 랜덤
            if (num[6] != 0) {
                List<Feed> randomFeed = new ArrayList<>();
                randomFeed = getRandomFeed(num[6], ShowLists);
                ShowLists.addAll(randomFeed);
            }

            int thisTotal = 0;
            for (int j : num) {
                thisTotal += j;
            }
            if (thisTotal != ShowLists.size()) {
                int dif = Math.abs(thisTotal - ShowLists.size());
                List<Feed> randomFeed = getRandomFeed(dif, ShowLists);
                ShowLists.addAll(randomFeed);
            }

            for (int i = 0; i < ShowLists.size(); i++) {
                //FeedDto feedDto = FeedDto.fromEntity(ShowLists.get(i));
                Feed feed = ShowLists.get(i);
                List<FeedTag> feedTag = feedTagRepository.findByFeedId(feed.getId());

                String img = "";
                Optional<Image> imgs = imageRepository.findFirstByFeed(feed);
                log.info("이미지: " + imgs);
                if (imgs.isEmpty()) img = "https://kr.object.ncloudstorage.com/noldaga-s3/util/noldaga-nonImg-feed.png";
                else img = imgs.get().getUrl();
                InterestFeedResponse interFeed = InterestFeedResponse.fromEntity(feed, img, FeedTagDto.fromEntityList(feedTag), i+"번째 데이터");
                result.add(interFeed);
            }

            Collections.shuffle(result);
        }

        //log.info(result);

        return result;
    }

    private List<Feed> getRandomFeed(int n, List<Feed> ShowLists) {
        List<Feed> randomFeed = new ArrayList<>();
        randomFeed = feedRepository.findRandom(n)
                .stream()
                .filter(feed -> !ShowLists.contains(feed)) // 이미 조회한 피드는 제외
                .collect(Collectors.toList());

        return randomFeed;
    }

    private List<int[]> countForNoInterest(Long total){
        //int[] percentages = {30, 30, 10, 2, 2, 5, 20}; //퍼센트 비율
        List<int[]> arr = new ArrayList<int[]>();
        int done = (int) (total / 20);
        int num = (int) (total % 20);
        System.out.println("뽑아야 할 개수 : " + num);
        int[] maxA = {6, 6, 2, 1, 1, 2, 4};
        int[] rearA = {0, 0, 0, 0, 0, 0, 0};

        List<Integer> availableIndexes = new ArrayList<>();
        for (int i = 1; i <= 7; i++) {
            availableIndexes.add(i - 1); // 인덱스는 0부터 시작하기 때문에 -1 해줌
        }

        Random random = new Random();
        for (int i = 0; i < num; i++) {
            int availableIndex = random.nextInt(availableIndexes.size());
            int index = availableIndexes.get(availableIndex);

            rearA[index]++; // 해당 인덱스에 1 추가
            if (rearA[index] == maxA[index]) { // 해당 인덱스에 들어갈 수 있는 개수를 다 뽑았다면, 해당 인덱스는 더이상 뽑지 않음
                availableIndexes.remove(availableIndex);
            }
        }

        for(int i=0; i<done; i++) {
            arr.add(maxA);
        }
        arr.add(rearA);

        return arr;
    }//알고리즘에 맞는 비율로 전체 개수를 나눠 줌
       /*회원의 관심사가 없을 시 전체 피드 중에
       좋아요 높은 순 30%, 조회 수 높은 순 30%, 해시 태그 중 랜덤 10%, 최신 게시글 5%,
       팔로잉 많은 유저 최근 작성 2.5%, 팔로워 많은 유저 최근 작성  2.5%, 그 외 나머지를 랜덤한 순서로 20%의
       비율로 피드를 반환해 주는 형식으로 알고리즘을 생각해봤다.
       그러니 탐색페이지를 최대한으로 했을때 총 20개의 게시물을 받아온다 생각하면
       6, 6, 2, 1, 1, 2, 4개 씩 반환 해주면 된다.*/



}
