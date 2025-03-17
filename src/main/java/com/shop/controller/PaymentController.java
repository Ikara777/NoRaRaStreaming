package com.shop.controller;


import com.shop.constant.PaymentStatus;
import com.shop.dto.PaymentRequest;
import com.shop.dto.PaymentResponse;
import com.shop.entity.Member;
import com.shop.service.MemberService;

import com.shop.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.Principal;

@Controller
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentController {

    private static final Logger logger = LoggerFactory.getLogger(PaymentController.class);

    private final PaymentService paymentService;
    private final MemberService memberService;


    @GetMapping("/verify-page")
    public String showVerifyPage(Model model, Principal principal) {

        String userId = principal.getName();

        Member member = memberService.getMemberById(userId);


        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setUserId(member.getUserId());
        paymentRequest.setMerchantUid("ORD" + System.currentTimeMillis()); // 고유 주문번호 생성


        model.addAttribute("member", member.getId());

        model.addAttribute("paymentRequest", paymentRequest);
        model.addAttribute("UserId", member.getId()); // 유저 아이디
        model.addAttribute("email", member.getEmail()); // 이메일
        model.addAttribute("name", member.getName()); // 이름
        model.addAttribute("tel", member.getTel()); // 전화번호
        model.addAttribute("add", member.getAddress()); // 주소

        return "member/payment";
    }


    // 결제 검증용 POST 요청
    @PostMapping("/verify")
    public ResponseEntity<String> verifyPayment(@RequestBody PaymentRequest paymentRequest) {


        try {
            logger.info("결제 요청 정보: " + paymentRequest);

            // 포트원에서 에러가 없음을 신뢰
            boolean hasError = paymentRequest.getImpUid() == null || paymentRequest.getMerchantUid() == null;

            if (hasError) {
                logger.error("결제 요청에 오류가 있습니다.");
                return ResponseEntity.badRequest().body("결제 검증 실패");
            }

            paymentService.savePayment(paymentRequest); // 결제 정보 저장
            return ResponseEntity.ok("결제가 성공적으로 처리되었습니다.");

        } catch (Exception e) {
            logger.error("결제 처리 중 오류 발생", e);
            return ResponseEntity.status(500).body("결제 처리 중 오류 발생");
        }
    }


}