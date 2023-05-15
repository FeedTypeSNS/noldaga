package com.noldaga.repository;

import com.noldaga.domain.entity.Follow;
import com.noldaga.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {

    Optional<Follow> findByFollowingAndFollower(User following, User follower);

    List<Follow> findByFollower(User me);
    List<Follow> findByFollowing(User me);

   /* @Query(value = "SELECT f.following FROM Follow f WHERE f.follower = :follower_id")
    List<User> selectAllFollower(Long follower_id); //나를 팔로우하는 사람 리스트 반환
    @Query(value = "SELECT f.follower FROM Follow f WHERE f.following = :following_id")
    List<UserDto> selectAllFollowing(Long following_id); //내가 팔로우 하는 사람 리스트 반환*/

    @Query("select f from Follow f where f.follower.id=:followerId and f.following.id=:followingId")
    Optional<Follow> findByFollowerAndFollowing(Long followerId,Long followingId);

}
