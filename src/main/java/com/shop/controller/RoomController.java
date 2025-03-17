package com.shop.controller;

import com.shop.dto.LiveRoomSetDto;
import com.shop.dto.LiveRoomSetImgDto;
import com.shop.dto.MemberFormDto;
import com.shop.entity.*;
import com.shop.repository.BoardRepository;
import com.shop.repository.MemberImgRepository;
import com.shop.repository.MemberRepository;
import com.shop.service.*;
import groovy.util.logging.Slf4j;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@Slf4j
@RequiredArgsConstructor
public class RoomController {

    private final FollowService followService;
    private final MemberImgService memberImgService;
    private final MemberImgRepository memberImgRepository;
    private final MemberRepository memberRepository;
    private final StreamService streamService;
    private final LiveRoomSetService liveRoomSetService;
    private final LiveRoomSetImgService liveRoomSetImgService;
    private final BoardRepository boardRepository;

    //사용자가 로그인 하면 팔로우 리스트를 받아오는 명령문
    @GetMapping("/follow/links")
    public String followLinks(Model model, Principal principal) {


        // 로그인한 사람의 userId를 이용해 Follow 리스트를 추출
        String userId = principal.getName();
        List<Follow> followList = followService.followList(userId);

        // 기존 서비스 메소드 활용
        List<MemberImg> followImgList = memberImgService.getMemberImgs(followList);

        model.addAttribute("followImgList", followImgList);
        model.addAttribute("followList", followList);

        return "item/followList";

    }


    //사용자가 로그인 하면 팔로우 리스트를 받아오는 명령문
    @GetMapping("/stream/live/{nickname}")
    public String streamerRoom(@PathVariable String nickname,
                               Model model,
                               Principal principal) {

        // 1. 스트리머 정보 가져오기
        Member streamer = memberRepository.findByNickname(nickname);
        Long memberId = streamer.getId();
        List<Board> board = boardRepository.findByMemberId(memberId);

        // 2. 스트리머 프로필 이미지 가져오기
        MemberImg memberImg = memberImgRepository.findByMemberId(streamer.getId());


        // 3. 로그인한 사용자의 팔로우 여부 확인
        boolean isLoggedIn = (principal != null);
        boolean isFollowing = false;
        if (isLoggedIn) {
            isFollowing = followService.isFollowing(principal.getName(), nickname);
        }

        // 4. 스트리머의 팔로워 수 가져오기
        Long followerCount = followService.getFollowerCount(nickname);

        boolean streamLiveCheck = streamService.streamLiveCheck(streamer.getStreamKey());
        streamLiveCheck = true;

        model.addAttribute("member", streamer);
        model.addAttribute("memberImg", memberImg);
        model.addAttribute("isLoggedIn", isLoggedIn);
        model.addAttribute("isFollowing", isFollowing);
        model.addAttribute("followerCount", followerCount);
        model.addAttribute("isLiveStreaming", streamLiveCheck);
        model.addAttribute("board", board);

        return "item/streamRoom";
    }




    //라이브 방송 세팅창
    @GetMapping("/liveRoom/set")
    public String liveRoomSet (Model model, Principal principal) {

        try {
            Member member = memberRepository.findByUserId(principal.getName());
            Long memberId = member.getId();

            LiveRoomSetDto liveRoomSet = liveRoomSetService.getLiveRoomSet(memberId);
            LiveRoomSetImgDto liveRoomSetImg = liveRoomSetImgService.getLiveRoomImgSet(memberId);

            model.addAttribute("liveRoomSet", liveRoomSet);
            model.addAttribute("liveRoomSetImg", liveRoomSetImg);
            model.addAttribute("memberId", memberId);  // memberId 추가 전달

            return "member/liveRoomSet";

        } catch (EntityNotFoundException e){
            model.addAttribute("liveRoomSet", new LiveRoomSetDto());
            return "member/liveRoomSet";
        }

    }


    @PostMapping("/liveRoom/set")
    public String liveRoomSetPost(@ModelAttribute("liveRoomSet") LiveRoomSetDto liveRoomSetDto,
                              BindingResult bindingResult, Principal principal,
                              @RequestParam(value = "LiveRoomSetImgFile", required = false)
                              MultipartFile LiveRoomSetImgFile, Model model ) {

        System.out.println("======  LiveRoomSetImgFile 변수 ========="+LiveRoomSetImgFile);

        if (bindingResult.hasErrors()) {
            return "member/liveRoomSet";
        }

        try{
            //프린시펄로 memberId를 검색하고 로그인 하지 않은 사용자 접근을 제안함
            Member memberId = memberRepository.findByUserId(principal.getName());
            liveRoomSetDto.setMemberId(memberId.getId());

            liveRoomSetService.updateLiveRoomSet(liveRoomSetDto);
            liveRoomSetImgService.updateLiveRoomSetImg(memberId.getId(), LiveRoomSetImgFile);

            // 추후에 이미지 업데이트 관련 로직도 추가해야함
            return "redirect:/liveRoom/set";

        }catch (Exception e){
            model.addAttribute("errorMessage", "수정 중 오류가 발생했습니다.");
            return "member/liveRoomSet";

        }

    }

}
