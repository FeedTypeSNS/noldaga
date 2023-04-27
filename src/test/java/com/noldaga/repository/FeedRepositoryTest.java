package com.noldaga.repository;

import com.noldaga.domain.entity.Feed;
import com.noldaga.domain.entity.User;
import com.noldaga.repository.Feed.FeedRepository;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Log4j2
@SpringBootTest
public class FeedRepositoryTest {

    @Autowired
    public UserRepository urep;
    @Autowired
    public FeedRepository rep;

    @Test
    public void FeedSave(){
        User user = User.of("username", "1234");
        //String title, String content, long groupId, int range, User user
        Feed feed = Feed.of("title", "content", 0, 1, user);
        rep.save(feed);
    }

    @Test
    public void selectAll(){
        List<Feed> feeds = rep.findAll();
        for(Feed f : feeds){
            log.info(f);
        }
    }

    @Test
    public void selectOne(){
        Feed feed = rep.findById(1L).orElseThrow(()->{
            log.info("실패");
            return new IllegalArgumentException("sdfjsld");
        });
        log.info(feed);
    }

    @Test
    @Transactional
    public void selectOneWithComment(){
        Feed feed = rep.findById(582L).orElseThrow(()->{
            log.info("실패");
            return new IllegalArgumentException("sdfjsld");
        });
        log.info(feed);
    }

    @Test
    public void FeedUpdate(){
        Feed feed = rep.findById(1L).orElseThrow(()->{
            log.info("실패");
            return new IllegalArgumentException("sdfjsld");
        });
        feed.change("수정","수정", 0, 1);
        rep.save(feed);
        log.info(feed);
    }

    @Test
    public void FeedDelete() {
        rep.deleteById(2L);
    }

    @Test
    public void MainFeedTest(){
        Pageable pageable = PageRequest.of(0,10, Sort.by("modDate").descending());
        Page<Feed> result = rep.MainFeedWithFollow(0,pageable);
        log.info(result.getTotalPages());
        log.info(result.getSize());
        log.info(result.getNumber());
        log.info(result.hasPrevious()+" "+result.hasNext());
        result.getContent().forEach(feed -> log.info(feed));
    }

    @Test
    public void MyPageFeedTest(){
        Pageable pageable = PageRequest.of(0,10, Sort.by("modDate").descending());
        for(Feed f : rep.MyPageFeed(2,pageable)){
            log.info(f);
        }
    }

}
