package com.shop.dto;


import com.shop.entity.Board;
import com.shop.entity.Member;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class BoardDto {

    private Long id;

    private Member member;
    private Long memberId;
    private String nickname;

    @NotBlank(message = "내용은 필수 입력 값입니다.")
    private String title;

    @NotBlank(message = "내용은 필수 입력 값입니다.")
    private String message;


    public Board toEntity(Member member) {
        return Board.builder()
                .title(title)
                .message(message)
                .nickname(member.getNickname())
                .member(member)
                .build();
    }

    public static BoardDto of(Board board) {
        BoardDto boardDto = new BoardDto();
        boardDto.setId(board.getId());
        boardDto.setTitle(board.getTitle());
        boardDto.setMessage(board.getMessage());
        boardDto.setNickname(board.getMember().getNickname());
        boardDto.setMemberId(board.getMember().getId());
        return boardDto;
    }
}

