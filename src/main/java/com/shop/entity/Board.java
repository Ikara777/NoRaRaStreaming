package com.shop.entity;

import com.shop.dto.BoardDto;
import com.shop.dto.MemberFormDto;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@Entity
@Table(name = "board")
@Getter
@Setter
@NoArgsConstructor
public class Board {

    @Id
    @Column(name = "board_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private String nickname;

    private String title;
    private String message;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime time; // 작성 시간

    @Builder
    public Board(String title, String message, Member member, String nickname) {
        this.title = title;
        this.message = message;
        this.member = member;
        this.nickname = nickname;
        this.time = LocalDateTime.now();  // 현재 시간으로 직접 설정
    }

    public void updateBoard(BoardDto boardDto) {
        // 비밀번호 암호화 처리

        this.title = boardDto.getTitle();
        this.message = boardDto.getMessage();
        this.nickname = boardDto.getNickname();

    }
}
