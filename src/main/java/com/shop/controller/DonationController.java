package com.shop.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;  // 이걸로 변경
import com.shop.dto.DonationDto;
import com.shop.dto.DonationImgDto;
import com.shop.dto.LiveRoomSetDto;
import com.shop.entity.Donation;
import com.shop.entity.DonationImg;
import com.shop.entity.Member;
import com.shop.repository.MemberRepository;
import com.shop.service.*;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
@Controller
@RequiredArgsConstructor
public class DonationController {

    private final PaymentService paymentService;
    private final MemberService memberService;
    private final MemberRepository memberRepository;
    private final CashSummaryService cashSummaryService;
    private final RentService rentService;
    private final DonationService donationService;
    private final DonationImgService donationImgService;

    // 도네이션 발생시 알림 전달.
    @GetMapping("/donation-alert/{nickname}")
    public String donationAlert(
            @PathVariable String nickname, Model model) {

        Member member = memberRepository.findByNickname(nickname);
        if (member == null) {
            throw new RuntimeException("스트리머를 찾을 수 없습니다.");
        }

        List<DonationDto> donationSettings = donationService.getDonationDtosByMemberId(member.getId());
        
        model.addAttribute("donationSettings", donationSettings);
        model.addAttribute("streamerNickname", nickname);
        return "item/alert";
    }




    @GetMapping("/donation/set")
    public String donationSetting(Principal principal, Model model) {


        Member member = memberRepository.findByUserId(principal.getName());
        String nickname = member.getNickname();

        if (member == null) {
            throw new RuntimeException("스트리머를 찾을 수 없습니다.");
        }


        // 기존 후원 설정과 이미지 불러오기
        List<DonationDto> existingDonations = donationService.getDonationDtosByMemberId(member.getId());

        model.addAttribute("member", member);
        model.addAttribute("streamerNickname", nickname);
        model.addAttribute("donations", existingDonations);



        return "item/donationSet";
    }


    @PostMapping("/donation/set/save")
    @ResponseBody
    public ResponseEntity<?> saveDonations(
            @RequestPart("donationData") String donationDataJson,
            @RequestPart(value = "images", required = false) List<MultipartFile> images) {

        try {
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> payload = mapper.readValue(donationDataJson, Map.class);
            Long memberId = Long.parseLong(payload.get("memberId").toString());
            List<Map<String, Object>> donations = (List<Map<String, Object>>) payload.get("donations");

            // 기존 데이터 모두 삭제
            donationService.deleteAllByMemberId(memberId);

            // 새로운 데이터 저장
            List<Donation> savedDonations = new ArrayList<>();
            for (int i = 0; i < donations.size(); i++) {
                Map<String, Object> donationData = donations.get(i);
                DonationDto dto = new DonationDto();
                dto.setAmount(((Number) donationData.get("amount")).intValue());
                dto.setMessage((String) donationData.get("message"));
                dto.setShowTime(((Number) donationData.get("showTime")).intValue());
                dto.setHeadColor((String) donationData.get("headColor"));
                dto.setTextColor((String) donationData.get("textColor"));

                 // 이미지가 있는 경우에만 전달
                MultipartFile imgFile = (images != null && i < images.size()) ? images.get(i) : null;
                Donation savedDonation = donationService.saveDonation(memberId, dto, imgFile);

                savedDonations.add(savedDonation);
            }

            return ResponseEntity.ok(Map.of("success", true));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }



    @GetMapping("/donation/set/list/{nickname}")
    @ResponseBody
    public ResponseEntity<List<DonationDto>> getDonationList(@PathVariable String nickname) {
        try {
            Member member = memberRepository.findByNickname(nickname);
            List<DonationDto> donations = donationService.getDonationDtosByMemberId(member.getId());
            return ResponseEntity.ok(donations);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }


}
