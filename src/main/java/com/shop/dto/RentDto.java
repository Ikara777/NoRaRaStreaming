package com.shop.dto;

import com.shop.constant.PaymentStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RentDto {
    private Long userId;      // 경매 아이템 ID
    private int rentAmount;
    private String massage;
    private PaymentStatus paymentStatus;
    private String nickName;

    public RentDto(Long userId, int rentAmount, String massage, String nickName) {
        this.userId = userId;
        this.rentAmount = rentAmount;
        this.massage = massage;
        this.nickName = nickName;

    }
}
