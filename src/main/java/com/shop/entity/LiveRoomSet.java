package com.shop.entity;

import com.shop.constant.Role;
import com.shop.dto.LiveRoomSetDto;
import com.shop.dto.MemberFormDto;
import com.shop.dto.MemberImgDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.crypto.password.PasswordEncoder;

@Entity
@Table(name = "LiveRoomSet")
@Getter
@Setter
@ToString
public class LiveRoomSet {
    @Id
    @Column(name = "liveRoomSet_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long memberId;

    private String roomName;

    private String roomDtl;


    public void updateLiveRoomSet(LiveRoomSetDto liveRoomSetDto) {
        //앤티티에서 업데이트
        this.roomName = liveRoomSetDto.getRoomName();
        this.roomDtl = liveRoomSetDto.getRoomDtl();

    }



}
