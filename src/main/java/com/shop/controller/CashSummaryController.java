package com.shop.controller;

import com.shop.dto.DonationMessageDto;
import com.shop.dto.RentDto;
import com.shop.entity.CashSummary;
import com.shop.entity.Member;
import com.shop.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/cash-summary") // API 요청 경로로 설정
@RequiredArgsConstructor
public class CashSummaryController {

    private final SimpMessagingTemplate messagingTemplate;
    private final PaymentService paymentService;
    private final MemberService memberService;
    private final CashSummaryService cashSummaryService;
    private final RentService rentService;


    // 로그인된 사용자의 캐시 잔액 계산
    @GetMapping("/balance")
    public String getCashBalance(@AuthenticationPrincipal UserDetails userDetails, Principal principal, Model model) {
        if (userDetails != null) {

            // 로그인된 사용자의 이메일과 이름 가져오기
            String userId = principal.getName();
            Member member = memberService.getMemberById(userId);


            Long memberId = member.getId();
            model.addAttribute("memberId", memberId);


            String email = member.getEmail();
            String name = member.getName(); // 이름도 가져온다고 가정 (수정 필요)

            // 계산된 잔액을 CashSummary 테이블에 저장 또는 업데이트
            CashSummary cashSummary = cashSummaryService.UpdateCashSummary(email, name);
            Integer totalAmount = cashSummary.getTotalAmount();


            model.addAttribute("totalAmount", totalAmount);

            return "member/cash";
        }


        model.addAttribute("totalAmount", 0);
        return "member/cash";
    }


    @PostMapping("/api/donation")
    @ResponseBody
    public ResponseEntity<?> sendDonationMessage(
            @RequestParam Integer amount,
            @RequestParam String message,
            @RequestParam String streamer,
            Principal principal) {

        System.out.println("------포스트 맵핑 호출 테스트-------");
        System.out.println("------포스트 맵핑 호출 테스트-------");
        System.out.println(amount);
        System.out.println(message);
        System.out.println(streamer);
        System.out.println("------포스트 맵핑 호출 테스트-------");
        System.out.println("------포스트 맵핑 호출 테스트-------");

        try {
            String userId = principal.getName();
            Member member = memberService.getMemberById(userId);
            RentDto rentDto = new RentDto(member.getId(), amount, message, streamer);

            rentService.submitRent(rentDto, member);
            System.out.println("저장 완료");

            String email = member.getEmail();
            String name = member.getName();
            cashSummaryService.UpdateCashSummary(email, name);
            System.out.println("캐시 업데이트 완료");

            String userNickname = member.getNickname();

            // messagingTemplate이 제대로 주입되었는지 확인
            if (messagingTemplate == null) {
                throw new RuntimeException("messagingTemplate이 주입되지 않았습니다.");
            }

            DonationMessageDto donationMessageDto = new DonationMessageDto(
                    streamer,
                    userNickname,
                    amount,
                    message
            );
            System.out.println("DTO 생성 완료: " + donationMessageDto);

            try {
                messagingTemplate.convertAndSend(
                        "/topic/donation/" + streamer,
                        donationMessageDto
                );
                System.out.println("웹소켓 메시지 전송 완료");
            } catch (Exception e) {
                System.out.println("웹소켓 메시지 전송 중 오류: " + e.getMessage());
                e.printStackTrace();
            }

            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "후원이 완료되었습니다.");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            System.out.println("전체 처리 중 오류 발생: " + e.getMessage());
            e.printStackTrace();

            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "후원 처리 중 오류가 발생했습니다: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

}

