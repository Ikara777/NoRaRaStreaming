package com.shop.controller;

import com.shop.dto.BoardDto;
import com.shop.entity.Board;
import com.shop.entity.Member;
import com.shop.entity.MemberImg;
import com.shop.repository.BoardRepository;
import com.shop.repository.MemberImgRepository;
import com.shop.repository.MemberRepository;
import com.shop.service.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;
import java.util.List;


@Controller
@RequiredArgsConstructor
public class BoardController {

    private final MemberService memberService;
    private final MemberRepository memberRepository;
    private final BoardService boardService;
    private final BoardRepository boardRepository;
    private final FollowService followService;
    private final MemberImgRepository memberImgRepository;

    @GetMapping("/board/new/{nickname}")
    public String getBoardNew(@PathVariable String nickname, Principal principal, Model model, BoardDto boardDto) {

        Member memberMaster = memberRepository.findByNickname(nickname);
        Member member = memberRepository.findByUserId(principal.getName());


        boolean cheack = member.getNickname() != nickname;
        System.out.println(cheack);


        if (member.getId() != memberMaster.getId()) {
            System.out.println(member.getNickname());
            System.out.println(nickname);
            System.out.println(cheack);
            return "redirect:/";  // 불일치시 메인으로 리다이렉트

        }


        boardDto.setMember(member);
        boardDto.setMemberId(member.getId());
        boardDto.setNickname(member.getNickname());

        model.addAttribute("Border", boardDto);

      return "member/board";
    }




    @PostMapping("/board/new")
    public String createBoard(@Valid BoardDto boardDto,
                              BindingResult bindingResult,
                              Principal principal) {

        if (bindingResult.hasErrors()) {
            return "member/board";
        }

        try {
            Member member = memberRepository.findByUserId(principal.getName());
            boardService.saveBoard(boardDto, member);
        } catch (Exception e) {
            return "member/board";
        }

        return "redirect:/board/list";  // 게시글 목록으로 리다이렉트
    }



    @GetMapping("/board/get/{nickname}/{boardId}")
    public String getBoard(@PathVariable Long boardId,
                           @PathVariable String nickname,
                           Model model, BoardDto boardDto
                            ,Principal principal) {

        boardDto = boardService.getBoard(boardId);

        Member streamer = memberRepository.findByNickname(nickname);

        MemberImg memberImg = memberImgRepository.findByMemberId(streamer.getId());


        // 3. 로그인한 사용자의 팔로우 여부 확인
        boolean isLoggedIn = (principal != null);
        boolean isFollowing = false;
        if (isLoggedIn) {
            isFollowing = followService.isFollowing(principal.getName(), nickname);
        }



        // 4. 스트리머의 팔로워 수 가져오기
        Long followerCount = followService.getFollowerCount(nickname);

        //boolean streamLiveCheck = streamService.streamLiveCheck(streamer.getStreamKey());
        boolean streamLiveCheck = true;

        model.addAttribute("member", streamer);
        model.addAttribute("memberImg", memberImg);
        model.addAttribute("isLoggedIn", isLoggedIn);
        model.addAttribute("isFollowing", isFollowing);
        model.addAttribute("followerCount", followerCount);
        model.addAttribute("isLiveStreaming", streamLiveCheck);
        model.addAttribute("Border", boardDto);

        return "Item/boardRead";
    }

    @GetMapping("/board/refund/{nickname}/{boardId}")
    public String getBoardRest(@PathVariable String nickname,
                               @PathVariable Long boardId, Principal principal, Model model) {

        Member memberMaster = memberRepository.findByNickname(nickname);
        Member member = memberRepository.findByUserId(principal.getName());


        boolean cheack = member.getNickname() != nickname;
        System.out.println("-----------------"+cheack);


        if (member.getId() != memberMaster.getId()) {
            System.out.println(member.getNickname());
            System.out.println(nickname);
            System.out.println(cheack);
            return "redirect:/";  // 불일치시 메인으로 리다이렉트
        }

        BoardDto boardDto = boardService.getBoard(boardId);
        System.out.println("------도달 체크------");
        System.out.println(boardDto.getMessage());
        System.out.println(boardDto.getTitle());

        model.addAttribute("boardDto", boardDto);

        return "member/boardRest";
    }


    @PostMapping("/board/rest/{boardId}")
    public String createBoardRest(@Valid BoardDto boardDto,
                              BindingResult bindingResult,
                              Principal principal) {

        if (bindingResult.hasErrors()) {
            return "member/boardRest";
        }

        try {
            Member member = memberRepository.findByUserId(principal.getName());
            boardService.updateBoard(boardDto);
        } catch (Exception e) {
            return "member/boardRest";
        }

        return "redirect:/board/list";  // 게시글 목록으로 리다이렉트
    }



}

