package com.shop.repository;

import com.shop.constant.PaymentStatus;
import com.shop.entity.Member;
import com.shop.entity.Payment;
import com.shop.entity.Rent;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RentRepository extends JpaRepository<Rent, Long> {


    @Query("SELECT SUM(r.rentAmount) FROM Rent r WHERE r.member.id = :memberId AND r.status IN (com.shop.constant.PaymentStatus.PAID, com.shop.constant.PaymentStatus.REFUND)")
    Optional<Integer> sumRentAmountByMemberId(@Param("memberId") Long memberId);



    List<Rent> findByMember(Member member);
    List<Rent> findByNickname(String nickname);
    List<Rent> findByNicknameAndStatus(String nickname, PaymentStatus status);


    // 또는 rent_id 기준 내림차순 정렬
    @Query("SELECT r FROM Rent r WHERE r.member.id = :memberId ORDER BY r.id DESC")
    Page<Rent> findByMemberIdOrderByIdDesc(
            @Param("memberId") Long memberId,
            Pageable pageable
    );


    @Modifying
    @Query("UPDATE Rent r SET r.status = :status WHERE r IN :rentList")
    void updateStatusForRentList(@Param("rentList") List<Rent> rentList, @Param("status") PaymentStatus status);

}

