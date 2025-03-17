package com.shop.dto;

import com.shop.entity.DonationImg;
import com.shop.entity.MemberImg;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

@Getter
@Setter
public class DonationImgDto {

    private Long id;
    private String imgName;
    private String oriImgName;
    private String imgUrl;

    private static ModelMapper modelMapper = new ModelMapper();

    public static DonationImgDto of(DonationImg donationImg){
        return modelMapper.map(donationImg, DonationImgDto.class);
    }
}
