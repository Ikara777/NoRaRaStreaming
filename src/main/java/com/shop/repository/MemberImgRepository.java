package com.shop.repository;

import com.shop.entity.Member;
import com.shop.entity.MemberImg;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberImgRepository extends JpaRepository<MemberImg, Long> {
    List<MemberImg> findByMemberIdIn(List<Long> memberIds);
    MemberImg findByMemberId(Long memberId); // 특정 Member 이미지 리스트 조회
}
