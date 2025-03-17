package com.shop.repository;


import com.shop.entity.Donation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface DonationRepository extends JpaRepository<Donation, Long> {
    List<Donation> findByMemberId(Long memberId);
    void deleteAllByMemberId(Long memberId);

    // ID로 삭제하도록 수정
    void deleteById(Long id);

    // memberId로 정렬된 목록 조회
    List<Donation> findByMemberIdOrderByIdAsc(Long memberId);
}
