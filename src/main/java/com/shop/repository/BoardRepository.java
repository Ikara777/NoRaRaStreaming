package com.shop.repository;

import com.shop.entity.Board;
import com.shop.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long> {
    List<Board> findByMemberId(Long member);
    Board findBoardById(Long id);  // 이렇게 하면 Optional이 아닌 Board를 직접 반환
    Page<Board> findAll(Pageable pageable);
}
