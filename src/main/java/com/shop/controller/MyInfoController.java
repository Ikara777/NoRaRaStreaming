package com.shop.controller;

import com.shop.dto.MemberFormDto;
import com.shop.entity.Member;
import com.shop.repository.MemberRepository;
import com.shop.service.MemberImgService;
import com.shop.service.MemberService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;

@RequestMapping("/myInfo")
@Controller
@RequiredArgsConstructor
public class MyInfoController {
    private final MemberService memberService;
    private final MemberImgService memberImgService;
    private final MemberRepository memberRepository;

    @GetMapping(value = "/mypage")
        public String updatePage(Model model, Principal principal){

            Member memberId = memberRepository.findByUserId(principal.getName());
            model.addAttribute("memberId", memberId);
            return "member/updatePage";
        }


    @GetMapping(value = "/update/{memberId}")
    public String member(@PathVariable("memberId") Long memberId, Model model, Principal principal) {

        try {

            MemberFormDto memberFormDto = memberService.getMemberId(memberId);

            model.addAttribute("memberFormDto", memberFormDto);
            model.addAttribute("memberId", memberId);

            return "member/myInfo";

        } catch (EntityNotFoundException e) {

            model.addAttribute("memberFormDto", new MemberFormDto());
            return "member/myInfo";

        }

    }


    @PostMapping(value = "/update/{memberId}")
    public String memberUpdate(@PathVariable("memberId") Long memberId,
                               @ModelAttribute MemberFormDto memberFormDto
                                ,BindingResult bindingResult, Model model,
                               @RequestParam(value = "memberImgFile") MultipartFile memberImgFile) {

        if (bindingResult.hasErrors()) {
            return "member/myInfo";
        }

        try {
            memberService.updateMember(memberFormDto);
            memberImgService.updateMemberImg(memberId, memberImgFile);

            model.addAttribute("updateSuccess", true);
            return "redirect:/?updateSuccess=true";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "수정 중 오류가 발생했습니다.");
            return "member/myInfo";
        }
    }
}

