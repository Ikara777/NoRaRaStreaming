package com.shop.service;

import com.shop.dto.MemberFormDto;
import com.shop.entity.LiveRoomSet;
import com.shop.entity.LiveRoomSetImg;
import com.shop.entity.Member;
import com.shop.entity.MemberImg;
import com.shop.repository.LiveRoomSetImgRepository;
import com.shop.repository.LiveRoomSetRepository;
import com.shop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.security.SecureRandom;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor

public class MemberService implements UserDetailsService {
    @Value("${itemImgLocation}")
    private String itemImgLocation;

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final MemberImgService memberImgService;
    private final FileService fileService;
    private final LiveRoomSetRepository liveRoomSetRepository;
    private final LiveRoomSetImgService liveRoomSetImgService;


    public Long saveMember(Member member, MultipartFile memberImgFile) throws Exception {
        validateDuplicateMember(member);

        // 스트림 키 생성 후 회원 등록
        String streamKey = generateStreamKey();
        member.setStreamKey(streamKey);
        memberRepository.save(member);


        MemberImg memberImg = new MemberImg();
        memberImg.setMember(member);
        memberImgService.saveMemberImg(memberImg, memberImgFile);

        // LiveRoomSet 생성
        LiveRoomSet liveRoomSet = new LiveRoomSet();
        liveRoomSet.setMemberId(member.getId());  // 저장된 member 사용
        liveRoomSet.setRoomName(member.getNickname() + "의 방송");
        liveRoomSet.setRoomDtl("방송 설명을 입력해주세요");
        liveRoomSetRepository.save(liveRoomSet);


        // 일단 아무것도 없으면 안되니 기본 프로필 사진을 썸네일로 사용
        LiveRoomSetImg liveRoomSetImg = new LiveRoomSetImg();
        liveRoomSetImg.setMember(member);
        liveRoomSetImgService.saveLiveRoomSetImg(liveRoomSetImg, memberImgFile);

        return member.getId();
    }

    // 랜덤 스트림 키 생성 메서드
    private String generateStreamKey() {
        String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789-_";
        SecureRandom random = new SecureRandom();
        String streamKey;

        while (true) {
            // 새로운 스트림 키 생성
            StringBuilder sb = new StringBuilder(20);
            for (int i = 0; i < 20; i++) {
                sb.append(chars.charAt(random.nextInt(chars.length())));
            }
            streamKey = sb.toString();

            // DB에서 해당 스트림 키가 존재하는지 확인
            Member member = memberRepository.findByStreamKey(streamKey);
            if (member == null) { // 중복되지 않으면 루프 종료
                break;
            }
        }
        return streamKey;
    }


    private void validateDuplicateMember(Member member) {
        Member findMember;
        findMember = memberRepository.findByEmail(member.getEmail());
        if (findMember != null) {
            throw new IllegalStateException("이미 가입된 회원입니다.");
        }
        findMember = memberRepository.findByTel(member.getTel());
        if (findMember != null) {
            throw new IllegalStateException("이미 가입된 전화번호입니다.");
        }
        findMember = memberRepository.findByUserId(member.getUserId());
        if (findMember != null) {
            throw new IllegalStateException("이미 가입된 이름입니다.");
        }
        findMember = memberRepository.findByNickname(member.getNickname());
        if (findMember != null) {
            throw new IllegalStateException("이미 가입된 닉네임 입니다..");
        }


    }

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        Member member = memberRepository.findByUserId(userId);

        if (member == null) {
            throw new UsernameNotFoundException(userId);
        }

        return User.builder()
                .username(member.getUserId())
                .password(member.getPassword())
                .roles(member.getRole().toString())
                .build();
    }

    // 현재 로그인한 사용자의 Member 정보를 가져오는 메서드
    public Member getMemberById(String userId) {
        Member member = memberRepository.findByUserId(userId);

        if (member == null) {
            throw new RuntimeException("로그인된 사용자를 찾을 수 없습니다.");
        }
        return member;
    }

    @Transactional(readOnly = true)
    public MemberFormDto getMemberId(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        MemberFormDto memberFormDto = MemberFormDto.of(member);
        return memberFormDto;
    }


    @Transactional
    public Long updateMember(MemberFormDto memberFormDto) throws Exception {
        // 회원 정보 조회
        Member member = memberRepository.findById(memberFormDto.getId())
                .orElseThrow(() -> new RuntimeException("회원 정보가 존재하지 않습니다."));

        // 회원 정보 업데이트
        member.updateMember(memberFormDto, passwordEncoder);

        // DB에 저장
        memberRepository.save(member);

        return member.getId();
    }



    // rtmp 서버에서 streamKey를 받아와서 user 목록을 찾음
    public List<Member> findMembersByStreamKeys(List<String> streamKeys) {

        return memberRepository.findByStreamKeyIn(streamKeys);
    }


    public Map<String, String> getNamesByUserIds(List<String> userIds) {
        List<Member> members = memberRepository.findMembersByUserIds(userIds);

        // userId와 name의 매핑 생성
        return members.stream()
                .collect(Collectors.toMap(Member::getUserId, Member::getName));
    }



    // 현재 로그인한 사용자의 Member 정보를 가져오는 메서드
    public Member getMemberByNullTest(String userId) {
        Member member = memberRepository.findByUserId(userId);


        if(member==null){
            return null;
        }

        return member;
    }
}
