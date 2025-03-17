package com.shop.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FollowDto {
    private Long id;

    private String userId;

    private String name;

    private String streamer;

    public FollowDto(Long id, String name, String streamer) {
        this.id = id;
        this.name = name;
        this.streamer = streamer;
    }


}
