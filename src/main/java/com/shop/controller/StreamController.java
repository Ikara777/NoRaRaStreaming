package com.shop.controller;

import com.shop.constant.Role;
import com.shop.dto.LiveRoomSetDto;
import com.shop.entity.*;
import com.shop.repository.MemberRepository;
import com.shop.service.*;
import groovy.util.logging.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Document;
import org.w3c.dom.Element;  // Element 추가
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@Slf4j
@RequiredArgsConstructor
public class StreamController {
    private final MemberService memberService;
    private final MemberImgService memberImgService;
    private final MemberRepository memberRepository;
    private final ChatService chatService;
    private final FollowService followService;
    private final LiveRoomSetService liveRoomSetService;
    private final LiveRoomSetImgService liveRoomSetImgService;
    private final CashSummaryService cashSummaryService;

    @GetMapping(value = "/")
    public String testRtmpStat(Model model) {


        String rtmpStatUrl = "http://localhost:8080/stat";
        List<String> streamKeys = new ArrayList<>();

        try {
            RestTemplate restTemplate = new RestTemplate();
            String xmlStats = restTemplate.getForObject(rtmpStatUrl, String.class);  // result를 xmlStats로 변경

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new InputSource(new StringReader(xmlStats)));

            NodeList streamNodes = doc.getElementsByTagName("stream");
            for (int i = 0; i < streamNodes.getLength(); i++) {
                Node streamNode = streamNodes.item(i);
                if (streamNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) streamNode;
                    String streamKey = element.getElementsByTagName("name").item(0).getTextContent();
                    streamKeys.add(streamKey);
                }
            }



            // 방송 중인 스트리머의 스트림키를 이용해 스트리밍 중인 스트리머를 전부 조회
            List<Member> liveMembers = memberService.findMembersByStreamKeys(streamKeys);
            System.out.println("현재 방송 중인 스트림 키: " + liveMembers);

            List<LiveRoomSet> liveRoomSetList = liveRoomSetService.findLiveRoomsList(liveMembers);
            List<LiveRoomSetImg> liveRoomSetImgList = liveRoomSetImgService.getStreamImgs(liveMembers);
            List<MemberImg> memberImgList = memberImgService.getMemberImgList(liveMembers);

            System.out.println("============ 맴버 리스트 목록 =============");
            for(int i = 0; i < memberImgList.size(); i++){
                System.out.println(memberImgList.get(i).getImgUrl());
            }

            model.addAttribute("memberImgList", memberImgList);
            model.addAttribute("streamKeys", liveMembers);
            model.addAttribute("liveRoomSetList", liveRoomSetList);
            model.addAttribute("liveRoomSetImgList", liveRoomSetImgList);

        } catch (Exception e) {
            System.out.println("RTMP 상태 조회 실패: "+ e);
        }


        return "item/stream";
    }



    @GetMapping("/api/streams/{nickname}")
    public String streamTest(@PathVariable String nickname, Model model, Principal principal) {

        //방송 여부 조회
        Member member = memberRepository.findByNickname(nickname);
        boolean isLoggedIn = (principal != null);
        boolean isLiveStreaming = false;

        // 방송중인 사람의 방송 설명 / 방송 프로필 이미지 전달
        LiveRoomSetDto liveRoomSet = liveRoomSetService.getLiveRoomSet(member.getId());
        MemberImg memberImg = memberImgService.getImagesByMemberId(member.getId());

        // 현재 방송 중인 스트림 키 목록 가져오기
        try {
            String rtmpStatUrl = "http://localhost:8080/stat";
            RestTemplate restTemplate = new RestTemplate();
            String xmlStats = restTemplate.getForObject(rtmpStatUrl, String.class);

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new InputSource(new StringReader(xmlStats)));

            NodeList streamNodes = doc.getElementsByTagName("stream");
            for (int i = 0; i < streamNodes.getLength(); i++) {
                Node streamNode = streamNodes.item(i);
                if (streamNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) streamNode;
                    String streamKey = element.getElementsByTagName("name").item(0).getTextContent();
                    if (streamKey.equals(member.getStreamKey())) {
                        isLiveStreaming = true;
                        break;
                    }
                }
            }


        } catch (Exception e) {
            System.out.println("RTMP 상태 조회 실패: " + e);
        }


        if( isLoggedIn ) {
            model.addAttribute("isFollowing", followService.isFollowing(principal.getName(), nickname));
            //케시 합산을 불러옴
            Member user = memberRepository.findByUserId(principal.getName());
            String email = user.getEmail();
            String name = user.getName(); // 이름도 가져온다고 가정 (수정 필요)
            CashSummary cashSummary = cashSummaryService.UpdateCashSummary(email, name);
            model.addAttribute("cashSummary", cashSummary);
        }else {
            model.addAttribute("cashSummary", null);
        }

        model.addAttribute("isLoggedIn", isLoggedIn);
        model.addAttribute("member", member);
        model.addAttribute("memberImg", memberImg);
        model.addAttribute("LiveRoomSet", liveRoomSet);

        model.addAttribute("isLiveStreaming", isLiveStreaming);

        return "item/streamLive";
    }


    @GetMapping("/api/cash-summary")
    @ResponseBody
    public ResponseEntity<Integer> getCashSummary(Principal principal) {

        if (principal == null) {
            Integer cashSummary = 0;
            return ResponseEntity.ok(cashSummary);
        }

        try {
            Member user = memberRepository.findByUserId(principal.getName());
            CashSummary cashSummary = cashSummaryService.UpdateCashSummary(user.getEmail(), user.getName());

            // 금액만 직접 반환
            return ResponseEntity.ok(cashSummary.getTotalAmount());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }



    @MessageMapping("/stream/chat/{nickname}")  // WebSocketConfig 에서 prefix를 app로 설정하여 웹소켓시 app가 자동 주입됨
    @SendTo("/topic/chat/{nickname}")
    public ChatMessage handleStreamChat(
            @DestinationVariable String nickname,
            @Payload ChatMessage chatMessage,
            Principal principal) {
        try {
            Member sender = memberService.getMemberById(principal.getName());
            chatMessage.setSenderId(sender.getName());
            chatMessage.setTimestamp(LocalDateTime.now());  // 시간 추가


            // 채팅 메시지 저장
            chatService.saveMessage(
                    sender.getName(),
                    nickname,
                    chatMessage.getMessage(),
                    chatMessage.getColor(),
                    sender.getRole()
            );

            return chatMessage;
        } catch (Exception e) {
            return null;
        }
    }


}
