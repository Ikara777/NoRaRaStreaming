package com.shop.service;


import com.shop.dto.DonationDto;
import com.shop.dto.LiveRoomSetDto;
import com.shop.entity.Donation;
import com.shop.entity.DonationImg;
import com.shop.entity.LiveRoomSet;
import com.shop.entity.Member;
import com.shop.repository.DonationImgRepository;
import com.shop.repository.DonationRepository;
import com.shop.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class DonationService {

    private final DonationRepository donationRepository;
    private final MemberRepository memberRepository;  // Member 엔티티 조회를 위해 추가
    private final DonationImgRepository donationImgRepository;
    private final DonationImgService donationImgService;

    public Donation saveDonation(Long memberId, DonationDto donationDto, MultipartFile imgFile) throws Exception {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("Member not found"));

        Donation donation = Donation.builder()
                .member(member)
                .amount(donationDto.getAmount())
                .message(donationDto.getMessage())
                .showTime(donationDto.getShowTime())
                .textColor(donationDto.getTextColor())
                .headColor(donationDto.getHeadColor())
                .build();

        Donation savedDonation = donationRepository.save(donation);

        // 이미지가 있는 경우 이미지도 저장
        if (imgFile != null && !imgFile.isEmpty()) {
            donationImgService.saveDonationImg(imgFile, savedDonation);
        }

        return savedDonation;
    }


    public List<Donation> getDonationsByMemberId(Long memberId) {
        return donationRepository.findByMemberId(memberId);
    }


    public void deleteAllByMemberId(Long memberId) {
        try {
            List<Donation> donations = donationRepository.findByMemberId(memberId);

            for (Donation donation : donations) {
                List<DonationImg> images = donationImgRepository.findByDonation(donation);

                for (DonationImg img : images) {
                    try {
                        donationImgService.deleteDonationImg(img);
                    } catch (Exception e) {
                        System.out.println("DonationService 이미지 삭제 중 오류 발생: " + e.getMessage());
                        // 이미지 삭제 실패해도 계속 진행
                    }
                }
            }
            // 도네이션 삭제
            donationRepository.deleteAllByMemberId(memberId);
        } catch (Exception e) {
            System.out.println("DonationService 도네이션 삭제 중 오류 발생: " + e.getMessage());
            throw new RuntimeException("도네이션 삭제 중 오류가 발생했습니다.", e);
        }
    }



    public List<DonationDto> getDonationDtosByMemberId(Long memberId) {
        List<Donation> donations = donationRepository.findByMemberId(memberId);
        List<DonationDto> dtos = new ArrayList<>();
        
        for (Donation donation : donations) {
            List<DonationImg> images = donationImgRepository.findByDonation(donation);
            DonationImg mainImage = images.isEmpty() ? null : images.get(0);
            dtos.add(DonationDto.fromEntity(donation, mainImage));
        }
        
        return dtos;
    }

}