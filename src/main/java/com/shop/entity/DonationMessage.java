package com.shop.entity;


import com.shop.constant.PaymentStatus;
import com.shop.dto.DonationMessageDto;
import com.shop.dto.MemberFormDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.crypto.password.PasswordEncoder;

@Entity
@Table(name = "DonationMessage")
@Getter
@Setter
public class DonationMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // 기본키 추가

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private Long StreamerId;
    private String streamer;
    private String userNickname;
    private int amount;
    private String message;
    private PaymentStatus paymentStatus;




    public void updateDonationMessage(DonationMessageDto donationMessageDto) {
        // 비밀번호 암호화 처리
        this.streamer = donationMessageDto.getStreamer();
        this.userNickname = donationMessageDto.getUserNickname();
        this.paymentStatus = donationMessageDto.getPaymentStatus();

    }

}
