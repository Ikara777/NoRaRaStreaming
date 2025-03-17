package com.shop.service;

import com.shop.dto.MemberImgDto;
import com.shop.entity.Follow;
import com.shop.entity.Member;
import com.shop.entity.MemberImg;
import com.shop.repository.MemberImgRepository;
import com.shop.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional

public class MemberImgService {

    @Value("${itemImgLocation}")
    private String itemImgLocation;

    private final MemberRepository memberRepository;
    private final MemberImgRepository memberImgRepository;
    private final FileService fileService;


    public void saveMemberImg(MemberImg memberImg, MultipartFile memberImgFile)throws Exception {

        //이미지 엔티티 업로드
        if(memberImgFile != null && !memberImgFile.isEmpty()) {
            String oriImgName = memberImgFile.getOriginalFilename();
            String imgName = fileService.uploadFile(itemImgLocation, oriImgName, memberImgFile.getBytes());
            String imgUrl = "/images/item/" + imgName;

            memberImg.updateItemImg(oriImgName, imgName, imgUrl);
        }else{
            memberImg.setOriImgName(null);
            memberImg.setImgName(null);
            memberImg.setImgUrl(null);
        }

        memberImgRepository.save(memberImg);
    }



    public void updateMemberImg(Long memberImgId, MultipartFile memberImgFile) throws Exception{

        if(!memberImgFile.isEmpty()){


            MemberImg savedMemberImg = memberImgRepository.findById(memberImgId). // 아이템 세이브에서 세이브 객체를 뺀다.
                    orElseThrow(EntityNotFoundException::new);


            // 기존에 등록된 상품 이미지 파일이 있는 경우 파일 삭제
            if(!StringUtils.isEmpty(savedMemberImg.getImgName())){
                fileService.deleteFile(itemImgLocation+"/"+ savedMemberImg.getImgName()); //원래 있던걸 지우고 다시 쓴다.
            }


            String oriImgName = memberImgFile.getOriginalFilename();
            String imgName = fileService.uploadFile(itemImgLocation, oriImgName, // 업로드에서 다시 파일을 쓴다.
                    memberImgFile.getBytes());
            String imgUrl = "/images/item/"+imgName; // 해당 주소를 통해 수정된 아이템이 표시됨


            savedMemberImg.updateItemImg(oriImgName, imgName, imgUrl); //업데이트 이미로 바꾸고 영속성 객체에서 감지하고 값 들어온거만 바꿈
        }
    }




    public MemberImg getImagesByMemberId(Long memberId) {
        return memberImgRepository.findByMemberId(memberId);
    }

    public void deleteItemImg(Long memberId) throws Exception {
        // 1. 이미지 엔티티 로드
        MemberImg savedMemberImg = memberImgRepository.findById(memberId)
                .orElseThrow(EntityNotFoundException::new);

        // 2. 파일 삭제
        if (!StringUtils.isEmpty(savedMemberImg.getImgName())) {
            fileService.deleteFile(itemImgLocation + "/" + savedMemberImg.getImgName());
        }

        // 3. DB에서 이미지 엔티티 삭제
        memberImgRepository.delete(savedMemberImg);
    }


    //사용자의 이미지를 여러장 뺄 수 있는 기능
    public List<MemberImg> getMemberImgs(List<Follow> followList) {
        List<MemberImg> memberImgs = new ArrayList<>();

        for (Follow follow : followList) {
            // streamer 닉네임으로 해당 Member를 먼저 찾습니다
            Member member = memberRepository.findByNickname(follow.getStreamer());
            if (member != null) {
                // 찾은 Member의 id로 MemberImg를 찾아서 순서대로 추가
                MemberImg memberImg = memberImgRepository.findByMemberId(member.getId());
                if (memberImg != null) {
                    memberImgs.add(memberImg);
                }
            }
        }

        return memberImgs;
    }

    public List<MemberImg> getMemberImgList(List<Member> memberList) {
        // Member 객체 리스트에서 ID 리스트를 추출
        List<Long> memberIds = memberList.stream()
                .map(Member::getId)
                .collect(Collectors.toList());

        // ID 리스트로 이미지 조회
        List<MemberImg> memberImgs = memberImgRepository.findByMemberIdIn(memberIds);

        return memberImgs;
    }

}
