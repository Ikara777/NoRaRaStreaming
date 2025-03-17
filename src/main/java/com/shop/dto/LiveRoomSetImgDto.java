package com.shop.dto;

import com.shop.entity.LiveRoomSetImg;
import com.shop.entity.MemberImg;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

@Getter
@Setter
public class LiveRoomSetImgDto {

    private Long memberId;
    private String imgName;
    private String oriImgName;
    private String imgUrl;
    private static ModelMapper modelMapper = new ModelMapper();

    public static LiveRoomSetImgDto of(LiveRoomSetImg liveRoomSetImg){
        return modelMapper.map(liveRoomSetImg, LiveRoomSetImgDto.class);
    }
}


