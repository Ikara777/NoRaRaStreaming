package com.shop.service;

import com.shop.dto.LiveRoomSetDto;
import com.shop.dto.LiveRoomSetImgDto;
import com.shop.dto.MemberFormDto;
import com.shop.entity.LiveRoomSet;
import com.shop.entity.LiveRoomSetImg;
import com.shop.entity.Member;
import com.shop.entity.MemberImg;
import com.shop.repository.LiveRoomSetImgRepository;
import com.shop.repository.LiveRoomSetRepository;
import com.shop.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor

public class LiveRoomSetService {

    private final LiveRoomSetRepository liveRoomSetRepository;
    private final MemberRepository memberRepository;
    private final LiveRoomSetImgRepository liveRoomSetImgRepository;


    public void createOrUpdateLiveRoomSet(Long memberId, LiveRoomSetDto liveRoomSetDto,
                                          MultipartFile liveRoomSetImgFile){
        // 멤버 조회
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("멤버를 찾을 수 없습니다."));

        // LiveRoomSet 조회
        LiveRoomSet liveRoomSet = liveRoomSetRepository.findByMemberId(memberId);

        if (liveRoomSet == null) {
            // 새로운 LiveRoomSet 생성
            liveRoomSetRepository.save(liveRoomSet);
        }

    }


    @Transactional(readOnly = true)
    public LiveRoomSetDto getLiveRoomSet(Long memberId) {
        LiveRoomSet liveRoomSet = liveRoomSetRepository.findByMemberId(memberId);
        LiveRoomSetImg liveRoomSetImg = liveRoomSetImgRepository.findByMemberId(memberId);
        if (liveRoomSet == null) {
            // 빈 DTO 반환
            return new LiveRoomSetDto();
        }

        // 찾는 결과가 null이 아닌 경우
        LiveRoomSetDto dto = new LiveRoomSetDto();
        dto.setMemberId(memberId);
        dto.setRoomName(liveRoomSet.getRoomName());
        dto.setRoomDtl(liveRoomSet.getRoomDtl());


        return dto;
    }

    @Transactional
    public Long updateLiveRoomSet(LiveRoomSetDto liveRoomSetDto) throws Exception {

        // 회원 정보 조회
        LiveRoomSet liveRoomSet = liveRoomSetRepository.findById(liveRoomSetDto.getMemberId())
                .orElseThrow(() -> new RuntimeException("회원 정보가 존재하지 않습니다."));

        // 회원 정보 업데이트
        liveRoomSet.updateLiveRoomSet(liveRoomSetDto);

        // DB에 저장
        liveRoomSetRepository.save(liveRoomSet);

        return liveRoomSet.getMemberId();


    }

    // rtmp 서버에서 streamKey를 받아와서 user 목록을 찾음
    public List<LiveRoomSet> findLiveRoomsList(List<Member> streamKeys) {

        List<Long> streamerId = streamKeys.stream()
                .map(Member::getId)
                .collect(Collectors.toList());


        return liveRoomSetRepository.findByMemberIdIn(streamerId);
    }



}