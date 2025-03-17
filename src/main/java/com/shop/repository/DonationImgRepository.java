package com.shop.repository;

import com.shop.entity.Donation;
import com.shop.entity.DonationImg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DonationImgRepository extends JpaRepository<DonationImg, Long> {
    List<DonationImg> findByDonation(Donation donation);
    void deleteByDonation(Donation donation);
    
    // memberId로 모든 이미지 삭제
    void deleteAllByMember_Id(Long memberId);
    
    // donation으로 이미지 찾기
    List<DonationImg> findByDonation_Id(Long donationId);
}
