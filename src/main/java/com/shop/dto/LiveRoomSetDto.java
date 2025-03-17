package com.shop.dto;


import com.shop.entity.LiveRoomSet;
import com.shop.entity.Member;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

@Getter
@Setter
public class LiveRoomSetDto {
    private Long memberId;

    @NotBlank(message = "방 이름은 필수 입력입니다.")
    private String roomName;

    private String roomDtl;

    private LiveRoomSetImgDto liveRoomSetImgDto;


    private static ModelMapper modelMapper = new ModelMapper();

    public static LiveRoomSetDto of(LiveRoomSet liveRoomSet){return modelMapper.map(liveRoomSet, LiveRoomSetDto.class);}

}
