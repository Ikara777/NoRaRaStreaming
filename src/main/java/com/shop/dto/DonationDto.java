package com.shop.dto;

import com.shop.entity.Donation;
import com.shop.entity.DonationImg;
import com.shop.entity.Member;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DonationDto {

    private Long id;
    private int DonationId;
    private Member member;

    private int amount;
    private String message;
    private int showTime;
    private String imgUrl;  // 이미지 URL 추가
    private String headColor;
    private String textColor;

    private List<DonationImgDto> donationImgDtoList = new ArrayList<>(); //상품 이미지 정보 리스트

    private List<Long> donationImgIds = new ArrayList<>(); //상품 이미지 아이디
    // ModelMapper
    private static ModelMapper modelMapper = new ModelMapper();


    public static DonationDto of(Donation donation){
        // Item -> ItemFormDto 연결
        return modelMapper.map(donation, DonationDto.class);
    }

    public static DonationDto fromEntity(Donation donation, DonationImg donationImg) {
        return DonationDto.builder()
                .id(donation.getId())
                .amount(donation.getAmount())
                .message(donation.getMessage())
                .showTime(donation.getShowTime())
                .headColor(donation.getHeadColor())
                .textColor(donation.getTextColor())
                .imgUrl(donationImg != null ? donationImg.getImgUrl() : null)
                .build();
    }
}
