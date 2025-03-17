package com.shop.repository;

import com.shop.entity.Follow;
import com.shop.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FollowRepository extends JpaRepository<Follow, Long> {

    boolean existsByUserIdAndStreamer(String userId, String streamer);
    void deleteByUserIdAndStreamer(String userId, String streamer);
    List<Follow> findByUserId(String userId);
    Long countByStreamer(String streamer);

}

