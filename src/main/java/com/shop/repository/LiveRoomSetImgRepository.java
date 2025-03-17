package com.shop.repository;

import com.shop.entity.LiveRoomSetImg;
import com.shop.entity.LiveRoomSetImg;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LiveRoomSetImgRepository extends JpaRepository<LiveRoomSetImg, Long> {

    List<LiveRoomSetImg> findByMemberIdIn(List<Long> memberId);
    LiveRoomSetImg findByMemberId(Long memberId);

}
