package com.shop.repository;

import com.shop.entity.LiveRoomSet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LiveRoomSetRepository extends JpaRepository<LiveRoomSet, Long> {
    List<LiveRoomSet> findByMemberIdIn(List<Long> memberId);
    LiveRoomSet findByMemberId(Long memberId);

}
