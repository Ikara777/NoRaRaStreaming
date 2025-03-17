package com.shop.dto;


import com.shop.constant.PaymentStatus;
import com.shop.entity.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor  // 기본 생성자 추가
public class DonationMessageDto {

    private Member member;

    private Long StreamerId;
    private String streamer;
    private String userNickname;
    private int amount;
    private String message;
    private PaymentStatus paymentStatus;

    public DonationMessageDto(String streamer, String userNickname, int amount , String message) {

        this.streamer=streamer;
        this.userNickname=userNickname;
        this.amount=amount;
        this.message=message;

    }

}