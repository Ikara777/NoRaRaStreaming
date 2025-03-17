package com.shop.service;


import com.shop.dto.LiveRoomSetDto;
import com.shop.dto.LiveRoomSetImgDto;
import com.shop.entity.*;
import com.shop.repository.LiveRoomSetImgRepository;
import com.shop.repository.MemberImgRepository;
import com.shop.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class LiveRoomSetImgService {

    @Value("${itemImgLocation}")
    private String itemImgLocation;

    private final MemberRepository memberRepository;
    private final LiveRoomSetImgRepository liveRoomSetImgRepository;
    private final FileService fileService;


    public void saveLiveRoomSetImg(LiveRoomSetImg liveRoomSetImg, MultipartFile LiveRoomSetImgFile)throws Exception {

        //이미지 엔티티 업로드
        if(LiveRoomSetImgFile != null && !LiveRoomSetImgFile.isEmpty()) {
            String oriImgName = LiveRoomSetImgFile.getOriginalFilename();
            String imgName = fileService.uploadFile(itemImgLocation, oriImgName, LiveRoomSetImgFile.getBytes());
            String imgUrl = "/images/item/" + imgName;

            liveRoomSetImg.updateItemImg(oriImgName, imgName, imgUrl);
        }else{
            liveRoomSetImg.setOriImgName(null);
            liveRoomSetImg.setImgName(null);
            liveRoomSetImg.setImgUrl(null);
        }

        liveRoomSetImgRepository.save(liveRoomSetImg);
    }

    public void updateLiveRoomSetImg(Long memberImgId, MultipartFile LiveRoomSetImgFile) throws Exception{

        if(!LiveRoomSetImgFile.isEmpty()){


            LiveRoomSetImg savedLiveRoomSetImg = liveRoomSetImgRepository.findById(memberImgId). // 아이템 세이브에서 세이브 객체를 뺀다.
                    orElseThrow(EntityNotFoundException::new);


            // 기존에 등록된 상품 이미지 파일이 있는 경우 파일 삭제
            if(!StringUtils.isEmpty(savedLiveRoomSetImg.getImgName())){
                fileService.deleteFile(itemImgLocation+"/"+ savedLiveRoomSetImg.getImgName()); //원래 있던걸 지우고 다시 쓴다.
            }


            String oriImgName = LiveRoomSetImgFile.getOriginalFilename();
            String imgName = fileService.uploadFile(itemImgLocation, oriImgName, // 업로드에서 다시 파일을 쓴다.
                    LiveRoomSetImgFile.getBytes());
            String imgUrl = "/images/item/"+imgName; // 해당 주소를 통해 수정된 아이템이 표시됨


            savedLiveRoomSetImg.updateItemImg(oriImgName, imgName, imgUrl); //업데이트 이미로 바꾸고 영속성 객체에서 감지하고 값 들어온거만 바꿈
        }
    }


    //사용자의 이미지를 여러장 뺄 수 있는 기능
    public List<LiveRoomSetImg> getStreamImgs(List<Member> memberList) {

        // 스트리머 리스트에서 stream(스트리머 닉네임)만 추출
        List<String> streamerNicknames = memberList.stream()
                .map(Member::getNickname) // Follow 객체에서 stream 변수 추출
                .collect(Collectors.toList());


        // 닉네임 리스트로 Member 리스트 조회
        List<Member> streamerList = memberRepository.findByNicknameIn(streamerNicknames);


        List<Long> streamerListId = streamerList.stream()
                .map(Member::getId)
                .collect(Collectors.toList());


        List<LiveRoomSetImg> LiveRoomImgs = liveRoomSetImgRepository.findByMemberIdIn(streamerListId);

        return LiveRoomImgs;
    }

    @Transactional(readOnly = true)
    public LiveRoomSetImgDto getLiveRoomImgSet(Long memberId) {
        LiveRoomSetImg liveRoomSetImg = liveRoomSetImgRepository.findByMemberId(memberId);
        if (liveRoomSetImg == null) {
            // 빈 DTO 반환
            return new LiveRoomSetImgDto();
        }

        // 찾는 결과가 null이 아닌 경우
        LiveRoomSetImgDto dto = new LiveRoomSetImgDto();
        dto.setMemberId(liveRoomSetImg.getId());
        dto.setImgName(liveRoomSetImg.getImgName());
        dto.setImgUrl(liveRoomSetImg.getImgUrl());
        dto.setOriImgName(liveRoomSetImg.getOriImgName());

        return dto;
    }

}
