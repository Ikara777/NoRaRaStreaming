package com.shop.controller;

import com.shop.constant.PaymentStatus;
import com.shop.dto.StarRequestDto;
import com.shop.entity.Member;
import com.shop.entity.Rent;
import com.shop.repository.MemberRepository;
import com.shop.repository.RentRepository;
import com.shop.service.MemberService;
import com.shop.service.RentService;
import com.shop.service.StarRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/refund")
@RequiredArgsConstructor  // 추가
public class StarRequestController {
    private final MemberService memberService;
    private final MemberRepository memberRepository;
    private final RentService rentService;
    private final RentRepository rentRepository;
    private final StarRequestService starRequestService;


    @Value("${portone.api.key}")
    private String apiKey;

    @Value("${portone.api.secret}")
    private String apiSecret;




    // 환전 기능 사이트
    @GetMapping("/star")
    public String showTestForm(Model model, Principal principal, StarRequestDto starRequestDto) {

        Member member = memberRepository.findByUserId(principal.getName());

        // 조건이 PAID 인 것만 찾음
        List<Rent> rentList = rentRepository.findByNicknameAndStatus(
                member.getNickname(),
                PaymentStatus.PAID
        );

        starRequestDto.setMember(member);

        model.addAttribute("starRequestDto", starRequestDto);
        model.addAttribute("rentList", rentList);

        return "member/test";

    }


    // 환전 정보 POST 맵핑
    @PostMapping("/request")
    public ResponseEntity<String> requestRefund(@RequestBody StarRequestDto starRequest,
            Principal principal) {
        try {
            // 계좌 검증
            boolean isValidAccount = verifyAccount(
                    starRequest.getBankCode(),
                    starRequest.getAccountNumber(),
                    starRequest.getAccountHolder()
            );


            if (!isValidAccount) {
                return ResponseEntity.badRequest().body("계좌 정보가 유효하지 않습니다.");
            }

            Member member = memberRepository.findByUserId(principal.getName());
            String nickName = member.getNickname();
                    // 현재 PAID 상태인 Rent 목록 조회
            List<Rent> rentList = rentRepository.findByNicknameAndStatus(
                    nickName,
                    PaymentStatus.PAID
            );

            starRequestService.saveStarRequest(starRequest);
            rentService.updateToREFUND(rentList);

            // 테스트용이므로 회원정보 대조는 생략
            return ResponseEntity.ok("계좌 확인되었습니다." +
                    "별사탕 환전 시간은 최대 30일이 소요됩니다.");



        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("오류 발생: " + e.getMessage());
        }

    }






    private boolean verifyAccount(String bankCode, String accountNumber, String accountHolder) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            String accessToken = getPortOneAccessToken();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(accessToken);

            String url = "https://api.iamport.kr/vbanks/holder";

            // URL 파라미터 추가
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
                    .queryParam("bank_code", bankCode)
                    .queryParam("bank_num", accountNumber);

            ResponseEntity<Map> response = restTemplate.exchange(
                    builder.toUriString(),
                    HttpMethod.GET,
                    new HttpEntity<>(headers),
                    Map.class
            );

            // 응답 처리
            if (response.getStatusCode() == HttpStatus.OK) {
                Map<String, Object> responseBody = response.getBody();
                if (responseBody != null && responseBody.get("code") != null && (int)responseBody.get("code") == 0) {
                    Map<String, Object> data = (Map<String, Object>) responseBody.get("response");
                    String returnedHolder = (String) data.get("bank_holder");
                    return accountHolder.equals(returnedHolder);
                }
            }
            return false;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    private String getPortOneAccessToken() {
        try {
            RestTemplate restTemplate = new RestTemplate();
            String url = "https://api.iamport.kr/users/getToken";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // JSON 형식으로 직접 작성
            String jsonBody = String.format(
                    "{\"imp_key\":\"%s\",\"imp_secret\":\"%s\"}",
                    apiKey,
                    apiSecret
            );

            System.out.println("전송할 JSON: " + jsonBody);  // 로그 확인용

            HttpEntity<String> request = new HttpEntity<>(jsonBody, headers);

            ResponseEntity<Map> response = restTemplate.postForEntity(
                    url,
                    request,
                    Map.class
            );

            System.out.println("포트원 응답: " + response.getBody());  // 응답 로그

            Map<String, Object> responseBody = response.getBody();
            if (responseBody != null && responseBody.get("response") != null) {
                Map<String, String> tokenData = (Map<String, String>) responseBody.get("response");
                return tokenData.get("access_token");
            }
            throw new RuntimeException("토큰 발급 실패");

        } catch (Exception e) {
            System.out.println("에러 상세: " + e.getMessage());  // 에러 상세 로그
            throw new RuntimeException("토큰 발급 중 오류 발생: " + e.getMessage());
        }
    }


}