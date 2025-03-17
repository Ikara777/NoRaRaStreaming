package com.shop.service;

import com.shop.dto.BoardDto;
import com.shop.entity.Board;
import com.shop.entity.Member;
import com.shop.repository.BoardRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;


    public Long saveBoard(BoardDto boardDto, Member member){
        Board board = boardDto.toEntity(member);
        Board savedBoard = boardRepository.save(board);
        return savedBoard.getId();
    }

    // 게시글 조회
    @Transactional(readOnly = true)
    public BoardDto getBoard(Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다."));

        return BoardDto.of(board);  // Board 엔티티를 BoardDto로 변환
    }

    // 게시글 수정
    public Long updateBoard(BoardDto boardDto) {

        Board board = boardRepository.findById(boardDto.getId())
                .orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다."));

        board.updateBoard(boardDto);

        return board.getId();
    }

    // 게시글 삭제
    public void deleteBoard(Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다."));

        boardRepository.delete(board);
    }

    // 게시글 목록 조회 (페이징)
    @Transactional(readOnly = true)
    public Page<BoardDto> getBoardList(Pageable pageable) {
        Page<Board> boardPage = boardRepository.findAll(pageable);
        return boardPage.map(BoardDto::of);
    }

}
