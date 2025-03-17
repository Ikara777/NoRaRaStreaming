package com.shop.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shop.entity.CashSummary;
import com.shop.entity.Member;
import com.shop.entity.Payment;
import com.shop.entity.Rent;
import com.shop.service.CashSummaryService;
import com.shop.service.MemberService;
import com.shop.service.PaymentService;
import com.shop.service.RentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.ui.Model;
import com.shop.dto.RefundRequestDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


@Controller
@RequestMapping("/api")
@RequiredArgsConstructor
public class RefundController {

    private final PaymentService paymentService;
    private final MemberService memberService;
    private final CashSummaryService cashSummaryService;
    private final RentService rentService;


    @Value("${portone.api.key}")
    private String apiKey;


    @Value("${portone.api.secret}")
    private String apiSecret;


    private String getAccessToken() {

        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper objectMapper = new ObjectMapper();

        Map<String, String> request = new HashMap<>();
        request.put("imp_key", apiKey);
        request.put("imp_secret", apiSecret);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        try {
            String jsonRequest = objectMapper.writeValueAsString(request); // JSON 직렬화

            HttpEntity<String> entity = new HttpEntity<>(jsonRequest, headers); // JSON 데이터를 HttpEntity에 설정

            ResponseEntity<Map> response = restTemplate.postForEntity(
                    "https://api.iamport.kr/users/getToken",
                    entity,
                    Map.class
            );

            Map<String, Object> responseBody = (Map<String, Object>) response.getBody().get("response");
            return (String) responseBody.get("access_token");
        } catch (HttpClientErrorException e) {
            System.err.println("오류 응답: " + e.getResponseBodyAsString());
            throw new RuntimeException("액세스 토큰 발급 실패: " + e.getResponseBodyAsString());
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON 직렬화 실패: " + e.getMessage());
        }
    }




    @GetMapping(value = {"/refund", "/refund/{page}"})
    public String getRefundPage(@PathVariable("page") Optional<Integer> page, Principal principal, Model model) {
        Pageable pageable = PageRequest.of(page.orElse(0), 5);


        String userId = principal.getName();

        Member member = memberService.getMemberById(userId);

        String email = member.getEmail();
        String merchantUid = "Cache-plus";
        Page<Payment> paymentRequestsList = paymentService.getPaymentList(email, pageable, merchantUid);


        model.addAttribute("member", member.getId());

        model.addAttribute("orders", paymentRequestsList);
        model.addAttribute("page", pageable.getPageNumber());
        model.addAttribute("maxPage", 5);

        model.addAttribute("refundRequestDto", new RefundRequestDto());
        return "member/paymentRefund"; // Thymeleaf 템플릿 경로
    }



    @PostMapping("/refund/process")
    public ResponseEntity<String> cancelPayment(@RequestBody RefundRequestDto refundRequestDto, Principal principal) {

        String userId = principal.getName();
        Member member = memberService.getMemberById(userId);
        String userEmail = member.getEmail();
        CashSummary cashSummary = cashSummaryService.getCashSummaryByEmail(userEmail);
        int refundAmount = refundRequestDto.getAmount();

        if(refundAmount > cashSummary.getTotalAmount()){
            return ResponseEntity.ok("보유중인 캐시 잔액 : "+cashSummary.getTotalAmount()+" 보다 환불 금액이 더 높습니다");
        }

        System.out.println("환불 imp코드 : "+refundRequestDto.getImpUid());
        System.out.println("환불 사유 : "+refundRequestDto.getReason());
        System.out.println("환불 금액 : "+refundRequestDto.getAmount());

        String impUid = refundRequestDto.getImpUid();
        String reason = refundRequestDto.getReason();
        Integer amount = refundRequestDto.getAmount();

        RestTemplate restTemplate = new RestTemplate();


        try {
            String accessToken = getAccessToken();

            Map<String, Object> cancelRequest = new HashMap<>();
            cancelRequest.put("imp_uid", impUid);
            cancelRequest.put("reason", reason);
            if (amount != null) {
                cancelRequest.put("amount", amount);
            }

            ObjectMapper objectMapper = new ObjectMapper();
            String jsonRequest = objectMapper.writeValueAsString(cancelRequest);

            HttpHeaders cancelHeaders = new HttpHeaders();
            cancelHeaders.setContentType(MediaType.APPLICATION_JSON);
            cancelHeaders.set("Authorization", "Bearer " + accessToken);

            HttpEntity<String> cancelEntity = new HttpEntity<>(jsonRequest, cancelHeaders);
            ResponseEntity<Map> cancelResponse = restTemplate.postForEntity(
                    "https://api.iamport.kr/payments/cancel",
                    cancelEntity,
                    Map.class
            );


            Map<String, Object> responseBody = cancelResponse.getBody();
            if (responseBody == null) {
                return ResponseEntity.status(500).body("환불 요청 응답이 비어 있습니다.");
            }


            Integer code = (Integer) responseBody.get("code");
            if (code != null && code == 0) {

                paymentService.updatePaymentStatusToCancel(impUid); //값을 변경할 Uid를 수신

                System.out.println("환불 정보 로그 : " + responseBody.get("response"));
                return ResponseEntity.ok("환불이 완료 되었습니다.");

            } else {
                String message = (String) responseBody.get("message");
                return ResponseEntity.status(400).body("환불 요청 실패: " + (message != null ? message : "알 수 없는 오류"));
            }


        } catch (Exception e) {
            return ResponseEntity.status(500).body("환불 요청 중 오류 발생: " + e.getMessage());
        }
    }




    @GetMapping(value = {"/refund/star", "/refund/star/{page}"})
    public String getStarCandyPage(@PathVariable("page") Optional<Integer> page, Principal principal, Model model) {
        Pageable pageable = PageRequest.of(page.orElse(0), 5);


        String userId = principal.getName();

        Member member = memberService.getMemberById(userId);

        Long memberNum = member.getId();


        Page<Rent> rentRequestsList = rentService.getRentList(memberNum, pageable);


        model.addAttribute("member", member.getId());
        model.addAttribute("orders", rentRequestsList);
        model.addAttribute("page", pageable.getPageNumber());
        model.addAttribute("maxPage", 5);


        model.addAttribute("refundRequestDto", new RefundRequestDto());
        return "Item/starRefund"; // Thymeleaf 템플릿 경로
    }




    @PostMapping("/refund/process/star")
    public String cancelStarRefund(@RequestParam("rentId") Long rentId, Principal principal) {

        String userId = principal.getName();
        Member member = memberService.getMemberById(userId);
        String userEmail = member.getEmail();
        String userName = member.getName();

        // 환불 처리
        rentService.processRefund(rentId, userEmail, userName);
        cashSummaryService.UpdateCashSummary(userEmail, userName);

            return "redirect:/api/refund/star";
        }


}




