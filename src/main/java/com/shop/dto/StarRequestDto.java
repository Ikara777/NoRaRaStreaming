package com.shop.dto;

import com.shop.entity.Member;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StarRequestDto {

    private Long Id;
    private Member member;
    private String bankCode;
    private String accountNumber;
    private String accountHolder;
    private int amount;
}