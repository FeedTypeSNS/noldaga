package com.noldaga.service;

import com.noldaga.domain.FeedDto;
import com.noldaga.domain.entity.Feed;
import com.noldaga.domain.entity.FeedTag;
import com.noldaga.domain.entity.HashTag;
import com.noldaga.repository.FeedTagRepository;
import com.noldaga.repository.HashTagRepository;
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
public class HashTagService {

    @Autowired
    private HashTagRepository hashTagRepository;
    @Autowired
    private FeedTagRepository feedTagRepository;

    @Transactional
    public void extractHashTag(String content, Long feedId){
        Pattern hashTagPattern = Pattern.compile("#(\\S+)");
        Matcher matcher = hashTagPattern.matcher(content);

        List<String> hashTagArray = new ArrayList<>();

        while(matcher.find()){
            hashTagArray.add(matcher.group());
        }
        saveHashTag(hashTagArray, feedId);
    }
    @Transactional
    public void saveHashTag(List<String> hashTagArray, Long feedId){
        hashTagArray.stream()
                .map(hashTagName->
                    hashTagRepository.findByHashTagName(hashTagName).orElseGet(()->
                        hashTagRepository.save(new HashTag(hashTagName))))
                .forEach(hashTag ->
                    feedTagRepository.save(FeedTag.of(feedId,hashTag)));
    }
}
