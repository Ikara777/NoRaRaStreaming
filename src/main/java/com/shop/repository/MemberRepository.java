package com.shop.repository;

import com.shop.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Member findByUserId(String userId);
    Member findByEmail(String userId);
    Member findByTel(String tel);
    Member findByNickname(String nickname);
    Member findByStreamKey(String streamKey);

    //방송중인 스트리머 정보를 리스트로 찾는 명령
    List<Member> findByStreamKeyIn(List<String> streamKeys);
    List<Member> findByNicknameIn(List<String> nickname);

    //테스트용 올 코드
    @Query("SELECT m.streamKey FROM Member m WHERE m.streamKey IS NOT NULL")
    List<String> findAllStreamKey();
    
    @Query("SELECT m FROM Member m WHERE m.userId IN :userIds")
    List<Member> findMembersByUserIds(@Param("userIds") List<String> userIds);
}
