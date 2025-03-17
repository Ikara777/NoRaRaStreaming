package com.shop.service;

import com.shop.entity.Follow;
import com.shop.entity.Member;
import com.shop.repository.FollowRepository;
import com.shop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional
@RequiredArgsConstructor
public class FollowService{

    private final FollowRepository followRepository;
    private final MemberRepository memberRepository;

    //로그인한 유저가 새로운 스트리머를 팔로우 하는 명령
    public void follow(String userId, String streamer) {
        Follow follow = new Follow();

        Member member = memberRepository.findByUserId(userId);
        String name = member.getName();
        String nickname = member.getNickname();

        follow.setUserId(userId);
        follow.setStreamer(streamer);
        follow.setName(name);
        follow.setNickname(nickname);

        followRepository.save(follow);
    }

    //언팔로우 하는 대상의 아이디와 스트리머를 조회하여 해당 스트리머 삭제
    public void unfollow(String userId, String streamer) {
        followRepository.deleteByUserIdAndStreamer(userId, streamer);
    }

    //현제 팔로우 하고 있는 상대인지 조회함
    public boolean isFollowing(String userId, String streamer) {
        return followRepository.existsByUserIdAndStreamer(userId, streamer);
    }

    public List<Follow> followList(String userId){
        return followRepository.findByUserId(userId);
    }

    //스트리머의 팔로우 수를 계산하는 서비스 명령문 - Long을 반환
    public Long getFollowerCount(String nickname) {
        return followRepository.countByStreamer(nickname);
    }

}