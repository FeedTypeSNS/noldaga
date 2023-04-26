package com.noldaga.service;

import com.noldaga.controller.response.FollowResponse;
import com.noldaga.domain.UserDto;
import com.noldaga.domain.UserSimpleDto;
import com.noldaga.domain.entity.Follow;
import com.noldaga.domain.entity.User;
import com.noldaga.exception.ErrorCode;
import com.noldaga.exception.SnsApplicationException;
import com.noldaga.repository.FollowRepository;
import com.noldaga.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FollowService {
    private final FollowRepository followRepository;
    private final UserRepository userRepository;

    @Transactional
    public FollowResponse doFollow(String me, Long follow){
        User following = userRepository.findByUsername(me).get();
        User follower = userRepository.findById(follow).get();

        if(following == follower){
            throw new SnsApplicationException(ErrorCode.CAN_NOT_FOLLOW_SELF);
        } //스스로 팔로우 못함

        Optional<Follow> value = followRepository.findByFollowingAndFollower(following, follower);

        if(value.isPresent()){
            throw new SnsApplicationException(ErrorCode.ALREADY_FOLLOW, String.format("%s has already follow %s",following.getUsername(), follower.getUsername()));
        }else {
            Boolean both = false;
            Optional<Follow> bothF = followRepository.findByFollowingAndFollower(follower, following);
            if(bothF.isPresent()){
                both = true; //내가 팔로우 거는 사람이 나를 팔로우 하는 사람이면 둘다 팔로우 중..
                Follow setBoth = bothF.get();
                setBoth.setBothFollow(both);
                followRepository.saveAndFlush(setBoth); //둘다 맞팔인걸로 표시해줘야함
            }

            Long id = followRepository.save(Follow.of(following, follower, both)).getId();

            String msg = following.getUsername()+"님 "+follower.getUsername()+"님을 팔로우 성공했습니다.";
            FollowResponse fr = new FollowResponse(id, msg, UserDto.fromEntity(following), UserDto.fromEntity(follower), both);
            return fr;
            //팔로우 성공 시-> total follow 수 늘리기, 알람 보내주기
        }
    }

    @Transactional
    public FollowResponse unFollow(String me, Long follow){
        User following = userRepository.findByUsername(me).get();
        User follower = userRepository.findById(follow).get();

        if(following == follower){
            throw new SnsApplicationException(ErrorCode.CAN_NOT_UNFOLLOW_SELF);
        } //스스로 언팔로우 못함

        Optional<Follow> value = followRepository.findByFollowingAndFollower(following, follower);

        if(value.isPresent()){
            Optional<Follow> bothF = followRepository.findByFollowingAndFollower(follower, following);
            if(bothF.isPresent()) {
                Follow rFollow = bothF.get();
                rFollow.setBothFollow(false);
                followRepository.saveAndFlush(rFollow);
            } //둘다 팔로우 중일때 한명이 언팔하면 맞팔 false 해야됨..

            followRepository.delete(value.get());
            String msg = following.getUsername()+"님  "+follower.getUsername()+"님을 언팔로우 성공했습니다.";
            FollowResponse fr = new FollowResponse(msg);
            return fr;
        }else {
            throw new SnsApplicationException(ErrorCode.ALREADY_UNFOLLOW, String.format("%s has already unfollow %s",following.getUsername(), follower.getUsername()));
        }//이미 팔로우 중 아님
    }


    public List<UserSimpleDto> getFollowerList(String users){
        User user = userRepository.findByUsername(users).get();
        List<Follow> followList = followRepository.findByFollower(user);
        List<UserSimpleDto> fl = new ArrayList<>();
        for(int i=0; i<followList.size(); i++){
            fl.add(UserSimpleDto.fromEntity(followList.get(i).getFollowing()));
        }
        return fl;
    }//팔로우 리스트 보기
    public List<UserSimpleDto> getFollowingList(String users){
        User user = userRepository.findByUsername(users).get();
        List<Follow> followList = followRepository.findByFollowing(user);
        List<UserSimpleDto> fl = new ArrayList<>();
        for(int i=0; i<followList.size(); i++){
            fl.add(UserSimpleDto.fromEntity(followList.get(i).getFollower()));
        }
        return fl;
    }//팔로잉 리스트 보기

}
