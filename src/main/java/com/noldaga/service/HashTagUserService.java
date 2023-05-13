package com.noldaga.service;

import com.noldaga.domain.entity.*;
import com.noldaga.exception.ErrorCode;
import com.noldaga.exception.SnsApplicationException;
import com.noldaga.repository.Feed.FeedRepository;
import com.noldaga.repository.FeedTagRepository;
import com.noldaga.repository.HashTag.HashTagRepository;
import com.noldaga.repository.HashTag.UserTagRepository;
import com.noldaga.repository.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Log4j2
@Service
public class HashTagUserService {

    @Autowired
    private HashTagRepository hashTagRepository;
    @Autowired
    private UserTagRepository userTagRepository;
    @Autowired
    private UserRepository userRepository;


    @Transactional//해시태그 저장 이거 쓰면 저장됨
    public void extractHashTag(String content, Long userId){
        //feedService에서 회원인지 확인해서 넘어왔기때문에 회원여부 확인안함

        Pattern hashTagPattern = Pattern.compile("#(\\S+)");
        Matcher matcher = hashTagPattern.matcher(content);

        List<String> hashTagArray = new ArrayList<>();

        while(matcher.find()){
            hashTagArray.add(matcher.group());
        }
        saveHashTag(hashTagArray, userId);
    }


    @Transactional
    public void saveHashTag(List<String> hashTagArray, Long userId){
        //피드확인
        User user = userRepository.findById(userId).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s is not founded", userId)));

        hashTagArray.stream()
                .map(hashTagName->
                        hashTagRepository.findByHashTagName(hashTagName).orElseGet(()->
                                hashTagRepository.save(new HashTag(hashTagName))))
                .forEach(hashTag ->{
                    hashTag.plusTotalUse();
                    userTagRepository.save(UserTag.of(user,hashTag));
                });
    }



    @Transactional
    public void deleteHashTag(Long userId){
        //피드확인
        User user = userRepository.findById(userId).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.FEED_NOT_FOUND, String.format("%s is not founded", userId)));

        List<UserTag> userTagList = userTagRepository.findByUserId(userId);
        userTagList.forEach(feedTag->feedTag.getHashTag().minusTotalUse());
        userTagRepository.deleteByUserId(userId);
    }
}
