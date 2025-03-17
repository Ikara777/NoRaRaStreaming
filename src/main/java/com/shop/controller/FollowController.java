package com.shop.controller;


import com.shop.repository.MemberRepository;
import com.shop.service.FollowService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.security.Principal;

@Controller
@RestController
@RequiredArgsConstructor
public class FollowController {

    private final FollowService followService;
    private final MemberRepository memberRepository;

    @PostMapping("/api/follow/{nickname}")
    public RedirectView follow(@PathVariable String nickname,
                               Principal principal,
                               HttpServletRequest request) {
        followService.follow(principal.getName(), nickname);
        return new RedirectView(request.getHeader("Referer"));
    }

    @PostMapping("/api/unfollow/{nickname}")
    public RedirectView unfollow(@PathVariable String nickname,
                                 Principal principal,
                                 HttpServletRequest request) {
        followService.unfollow(principal.getName(), nickname);
        return new RedirectView(request.getHeader("Referer"));
    }



}

